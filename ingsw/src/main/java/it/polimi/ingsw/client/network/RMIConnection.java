package it.polimi.ingsw.client.network;

import it.polimi.ingsw.exception.RMIException;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.ServerRMIMask;
import it.polimi.ingsw.game.config.Config;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 16, 2015
 */
public class RMIConnection extends Connection {
    private static final Logger LOG = Logger.getLogger(RMIConnection.class.getName());

    /** Thread that reads data */
    private ReadRunnable mReader = null;

    /** Receive Listener */
    private OnReceiveListener mTempRecv = null;

    /** Common RMI Mask */
    private ServerRMIMask mServerMask = null;

    /** My unique id used to communicate with server */
    private String mUniqueId = null;

    /** Server host */
    private String mHost;

    /** Is initialized */
    private boolean mInited = false;

    public RMIConnection() {
        super();
    }

    /** Set the server host 
     *
     * @param host The host
     */
    @Override
    public void setHost(String host) {
        mHost = host;
        mInited = true;
    }

    /** Start the connection
     * 
     * @throws IOException
     */
    @Override
    public void connect() throws IOException {
        if(!mInited)
            throw new RMIException("RMIConnection must be configured before use");

        if(mServerMask != null)
            throw new RMIException("Socket already created");

        Registry registry = LocateRegistry.getRegistry(mHost);

        try {
            mServerMask = (ServerRMIMask) registry.lookup(Config.RMISERVER_STRING);
            mUniqueId = mServerMask.registerAndGetId();
            if(mUniqueId == null)
                throw new RMIException("Cannot receive a unique id");

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

    /* Sets the listener
     * 
     * @param listener The new listener
     */
    @Override
    public void setOnReceiveListener(OnReceiveListener listener) {
        if(mReader != null) {
            mReader.setListener(listener);
        } else {
            mTempRecv = listener;
        }
    }

    /** Send a packet to the server
     *
     * @param pkt the packet
     */
    @Override
    public void sendPacket(GameCommand pkt) {
        if(mServerMask == null || mUniqueId == null)
            throw new RMIException("RMI Connection is offline");

        try {
            mServerMask.onRMICommand(mUniqueId, pkt);
        } catch (RemoteException e) {
            LOG.log(Level.FINE, e.toString(), e);
            disconnect();
        }
    }

    /** Check if the client is correctly connected
     * 
     * @return True if is online
     */
    @Override
    public boolean isOnline() {
        if(mServerMask == null)
            return false;

        return true;
    }

    /** Handle incoming messages and dispatch them to the proper listener */
    private class ReadRunnable implements Runnable {
        private final Logger LOG = Logger.getLogger(ReadRunnable.class.getName());

        /** The listener */
        private OnReceiveListener mListener = null;

        /** Common mask */
        private final ServerRMIMask mServer;

        /** My id */
        private final String mClientId;
        private boolean mOnline = true;

        public ReadRunnable(ServerRMIMask server, String id) {
            mServer = server;
            mClientId = id;
        }

        /** Set the listener
         *
         * @param listener The listener 
         */
        public void setListener(OnReceiveListener listener) {
            mListener = listener;
        }

        /** Close the thread */
        public void shutdown() {
            mOnline = false;
        }

        @Override
        public void run() {
            try  {
                while(mServer != null && mOnline) {
                    GameCommand[] commands = mServer.readCommands(mClientId);
                    if(mListener != null) {
                        for(GameCommand i : commands)
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

    /** Close the connection to the server */
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
