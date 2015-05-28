package it.polimi.ingsw.client.network;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public class TCPConnection extends Connection {
    private static final Logger LOG = Logger.getLogger(TCPConnection.class.getName());

    /** Server host */
    private String mHost;

    /** Server port */
    private int mPort = Config.SERVER_TCP_LISTEN_PORT;

    /** Is all initialized? */
    private boolean mInited = false;

    /** Socket connection */
    private Socket mSocket = null;

    /** Input and output streams */
    private ObjectOutputStream mOut = null;
    private ObjectInputStream mIn = null;

    /** Two working threads */
    private ReadRunnable mReader = null;
    private PingRunnable mPinger = null;

    /** The listener */
    private OnReceiveListener mListener = null;

    public TCPConnection() {
        super();
    }

    /** Set the host 
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
            throw new RuntimeException("TCPConnection must be configured before use");
        
        if(mSocket != null)
            throw new RuntimeException("Socket already created");
    
        mSocket = new Socket(mHost, mPort);
        mOut = new ObjectOutputStream(mSocket.getOutputStream());
        mIn = new ObjectInputStream(mSocket.getInputStream());
        mReader = new ReadRunnable(this, mIn);
        mPinger = new PingRunnable(this);
        
        if(mListener != null)
            mReader.setListener(mListener);
        new Thread(mReader).start();
        new Thread(mPinger).start();
    }

    /** Send a packet to the server
     *
     * @param pkt the packet
     */
    @Override
    public void sendPacket(NetworkPacket pkt) {
        if(mSocket == null || !mSocket.isConnected() || mOut == null)
            throw new RuntimeException("Cannot send message. Socket is closed.");
        
        try {
            mOut.writeObject(pkt);
            mOut.flush();
        }catch(IOException e) {
            LOG.log(Level.FINER, "Connection closed: " + e.toString(), e);
            disconnect();
        }
    }

    /* Sets the listener
     * 
     * @param listener The new listener
     */
    @Override
    public void setOnReceiveListener(OnReceiveListener listener) {
        mListener = listener;
        if(mReader != null)
            mReader.setListener(mListener);
    }
    
    /** Check if the client is correctly connected
     * 
     * @return True if is online
     */
    @Override
    public boolean isOnline() {
        if(mSocket != null && mSocket.isConnected())
            return true;
        return false;
    }

    /** Disconnect */
    @Override
    public synchronized void disconnect() {
        if(mListener != null)
            mListener.onDisconnect();
        if(mSocket != null && mSocket.isConnected())
            try {
                mOut.close();
                mIn.close();
                mSocket.close();                
            } catch(IOException e) {
                LOG.log(Level.FINER, e.toString(), e);
            }
        mSocket = null;
    }

    /** Handle incoming messages and dispatch them to the proper listener */
    private class ReadRunnable implements Runnable {
        private final Logger LOG = Logger.getLogger(ReadRunnable.class.getName());
        private OnReceiveListener mListener = null;
        private final TCPConnection mParent;
        private final ObjectInputStream mReader;

        public ReadRunnable(TCPConnection parent, ObjectInputStream reader) {
            mParent = parent;
            mReader = reader;
        }

        public void setListener(OnReceiveListener listener) {
            mListener = listener;
        }

        @Override
        public void run() {
            try  {
                while(mSocket != null && mSocket.isConnected()) {
                    Object obj = mReader.readObject();
                    if(obj != null && mListener != null && obj instanceof NetworkPacket)
                        mListener.onReceive((NetworkPacket) obj);
                }
            } catch (Exception e) {
                LOG.log(Level.FINER, "Connection closed:" + e.toString(), e);
            } finally {
                mParent.disconnect();
            }
        }
    }
    
    /** Ping the server each Config.CLIENT_TCP_PING_TIME milliseconds */
    private class PingRunnable implements Runnable {
        private final Logger LOG = Logger.getLogger(ReadRunnable.class.getName());
        private final TCPConnection mParent;
        
        public PingRunnable(TCPConnection parent) {
            mParent = parent;
        }

        @Override
        public void run() {
            try  {
                while(mParent.isOnline()) {
                    mParent.sendPacket(GameCommand.CMD_PING);
                    Thread.sleep(Config.CLIENT_TCP_PING_TIME);
                }
            } catch (Exception e) {
                LOG.log(Level.FINER, "Ping thread stopped: " + e.toString(), e);
            }
        }
    }
}
