package it.polimi.ingsw.client.network;

import it.polimi.ingsw.server.ServerRMIMask;
import it.polimi.ingsw.server.ServerRMI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 16, 2015
 */

public class RMIConnection extends Connection{
    private static final Logger mLog = Logger.getLogger(ServerRMI.class.getName());
    private static final String RMISERVER_STRING = "eftaiosRMI";
    
    private ReadRunnable mReader = null;
    private OnReceiveListener mTempRecv = null;
    private ServerRMIMask mServerMask = null;
    private String mUniqueId = null;
    private String mHost;
    private boolean mInited = false;

    public RMIConnection() {
        super();
        mConfigParams.put("Registry Host", Connection.ParametersType.TYPE_STRING);
    }
    
    @Override
    public void setConfiguration(Map<String, Object> obj) {
        Object objHost = obj.get("Registry Host");
        if(objHost != null && objHost instanceof String) {
            mHost = ((String) objHost).trim();

            if(mHost.length() == 0)
                throw new RuntimeException("Invalid host");
            mInited = true;
        } else
            throw new RuntimeException("Invalid parameters");
    }

    @Override
    public void connect() throws IOException {
        if(!mInited)
            throw new RuntimeException("RMIConnection must be configured before use");
        
        if(mServerMask != null)
            throw new RuntimeException("Socket already created");
    
        Registry registry = LocateRegistry.getRegistry(mHost);
        
        try {
            mServerMask = (ServerRMIMask) registry.lookup(RMISERVER_STRING);
            mUniqueId = mServerMask.registerAndGetId();
            if(mUniqueId == null)
                throw new RuntimeException("Cannot receive a unique id");
            
            mReader = new ReadRunnable(mServerMask, mUniqueId);
            if(mTempRecv != null) {
                mReader.setListener(mTempRecv);
                mTempRecv = null;
            }
            new Thread(mReader).start();
        } catch (NotBoundException e) {
            throw new RuntimeException("Cannot find my server on the RMI Registry");
        }
    }
    
    @Override
    public void setOnReceiveListener(OnReceiveListener listener) {
        if(mReader != null) {
            mReader.setListener(listener);
        } else {
            mTempRecv = listener;
        }
    }

    @Override
    public void sendMessage(String msg) {
        if(mServerMask == null || mUniqueId == null)
            throw new RuntimeException("RMI Connection is offline");
        
        try {
            mServerMask.onRMICommand(mUniqueId, msg);
        } catch (RemoteException e) {
            disconnect();
        }
    }

    @Override
    public boolean isOnline() {
        if(mServerMask == null)
            return false;

        return true;
    }
    
    /* Handle incoming messages and dispatch them to the proper listener */
    private class ReadRunnable implements Runnable {
        private final Logger mLog = Logger.getLogger(ReadRunnable.class.getName());
        private OnReceiveListener mListener = null;
        private final ServerRMIMask mServer;
        private final String mClientId;
        private boolean mOnline = true;

        public ReadRunnable(ServerRMIMask server, String id) {
            mServer = server;
            mClientId = id;
        }

        public void setListener(OnReceiveListener listener) {
            mListener = listener;
        }
        
        public void shutdown() {
            mOnline = false;
        }

        @Override
        public void run() {
            try  {
                while(mServer != null && mOnline) {
                    String [] commands = mServer.readCommands(mClientId);
                    if(commands != null && mListener != null) {
                        for(String i : commands)
                            if(i != null && mListener != null)
                                mListener.onReceive(i.trim());
                    }
                    Thread.sleep(250);
                }
            } catch (Exception e) {
                mLog.log(Level.INFO, "Connection closed:" + e.toString());
            } finally {
                System.out.println("Closing socket");
                if(mListener != null)
                    mListener.onDisconnect();
            }
        }

    }

    @Override
    public void disconnect() {
        mServerMask = null;
        if(mReader != null) {
            mReader.shutdown();
            mReader = null;
        }
        
        if(mListener != null)
            mListener.onDisconnect();
    }
}
