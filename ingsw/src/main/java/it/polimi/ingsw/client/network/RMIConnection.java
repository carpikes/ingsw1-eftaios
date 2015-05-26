package it.polimi.ingsw.client.network;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.network.ServerRMIMask;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 16, 2015
 */

public class RMIConnection extends Connection {
    private static final Logger LOG = Logger.getLogger(RMIConnection.class.getName());
    private static final String RMISERVER_STRING = "eftaiosRMI";
    
    private ReadRunnable mReader = null;
    private OnReceiveListener mTempRecv = null;
    private ServerRMIMask mServerMask = null;
    private String mUniqueId = null;
    private String mHost;
    private boolean mInited = false;

    public RMIConnection() {
        super();
    }
    
    @Override
    public void setHost(String host) {
        mHost = host;
        mInited = true;
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
            LOG.log(Level.WARNING, e.toString(), e);
            throw new IOException("Cannot find my server on the RMI Registry");
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
    public void sendPacket(NetworkPacket pkt) {
        if(mServerMask == null || mUniqueId == null)
            throw new RuntimeException("RMI Connection is offline");
        
        try {
            mServerMask.onRMICommand(mUniqueId, pkt);
        } catch (RemoteException e) {
            LOG.log(Level.FINE, e.toString(), e);
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
        private final Logger LOG = Logger.getLogger(ReadRunnable.class.getName());
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
                    NetworkPacket[] commands = mServer.readCommands(mClientId);
                    if(mListener != null) {
                        for(NetworkPacket i : commands)
                            if(i != null && mListener != null)
                                mListener.onReceive(i);
                    }
                    Thread.sleep(250);
                }
            } catch (Exception e) {
                LOG.log(Level.INFO, "Connection closed: " + e.toString(), e);
            } finally {
                if(mListener != null)
                    mListener.onDisconnect();
            }
        }

    }

    @Override
    public void disconnect() {
        mServerMask = null;
        
        if(mListener != null)
            mListener.onDisconnect();
        
        if(mReader != null) {
            mReader.shutdown();
            mReader = null;
        }
    }
}
