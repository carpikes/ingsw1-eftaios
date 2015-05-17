package it.polimi.ingsw.client.network;

import it.polimi.ingsw.config.Config;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

public class TCPConnection extends Connection {
    private static final Logger mLog = Logger.getLogger(TCPConnection.class.getName());
    private String mHost;
    private int mPort = -1;
    private boolean mInited = false;

    private Socket mSocket = null;
    private ObjectOutputStream mOut = null;
    private ObjectInputStream mIn = null;
    private ReadRunnable mReader = null;
    private PingRunnable mPinger = null;
    private OnReceiveListener mListener = null;

    public TCPConnection() {
        super();
        mConfigParams.put("Host", Connection.ParametersType.TYPE_STRING);
        mConfigParams.put("Port", Connection.ParametersType.TYPE_INTEGER);
    }

    @Override
    public void setConfiguration(Map<String, Object> obj) {
        Object o1 = obj.get("Host");
        Object o2 = obj.get("Port");

        if(o1 != null && o2 != null && o1 instanceof String && o2 instanceof Integer) {
            mHost = ((String) o1).trim();
            mPort = (Integer) o2;

            if(mHost.length() == 0)
                throw new RuntimeException("Invalid host");
            if(mPort < 0 || mPort > 65535)
                throw new RuntimeException("Invalid port");
            mInited = true;
        } else
            throw new RuntimeException("Invalid parameters");
    }

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

    @Override
    public void sendPacket(NetworkPacket pkt) {
        if(mSocket == null || !mSocket.isConnected() || mOut == null)
            throw new RuntimeException("Cannot send message. Socket is closed.");
        
        try {
            mOut.writeObject(pkt);
            mOut.flush();
        }catch(IOException e) {
            mLog.log(Level.FINER, "Connection closed: " + e.toString());
            disconnect();
        }
    }

    @Override
    public void setOnReceiveListener(OnReceiveListener listener) {
        mListener = listener;
        if(mReader != null)
            mReader.setListener(mListener);
    }
    
    @Override
    public boolean isOnline() {
        if(mSocket != null && mSocket.isConnected())
            return true;
        return false;
    }

    @Override
    public void disconnect() {
        if(mListener != null)
            mListener.onDisconnect();
        if(mSocket != null && mSocket.isConnected())
            try {
                mOut.close();
                mIn.close();
                mSocket.close();                
            } catch(IOException e) {
                e.printStackTrace();
            }
        mSocket = null;
    }

    // Handle incoming messages and dispatch them to the proper listener
    private class ReadRunnable implements Runnable {
        private final Logger mLog = Logger.getLogger(ReadRunnable.class.getName());
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
                mLog.log(Level.INFO, "Connection closed:" + e.toString());
            } finally {
                mParent.disconnect();
            }
        }
    }
    
    // Ping the server each Config.CLIENT_TCP_PING_TIME milliseconds
    private class PingRunnable implements Runnable {
        private final Logger mLog = Logger.getLogger(ReadRunnable.class.getName());
        private final TCPConnection mParent;
        
        public PingRunnable(TCPConnection parent) {
            mParent = parent;
        }

        @Override
        public void run() {
            try  {
                while(mParent.isOnline()) {
                    mParent.sendPacket(GameCommands.CMD_PING);
                    Thread.sleep(Config.CLIENT_TCP_PING_TIME);
                }
            } catch (Exception e) {
                mLog.log(Level.FINE, "Ping thread stopped: " + e.toString());
            }
        }
    }
}
