package it.polimi.ingsw.client.network;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

public class TCPConnection extends Connection {
    private String mHost;
    private int mPort = -1;
    private boolean mInited = false;

    private Socket mSocket = null;
    private PrintWriter mOut = null;
    private BufferedReader mIn = null;
    private ReadRunnable mReader = null;

    /* This thing is only used if mReader is not initialized yet */
    private OnReceiveListener mTempRecv = null;

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
        mOut = new PrintWriter(mSocket.getOutputStream());
        mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        mReader = new ReadRunnable(mSocket, mIn);
        if(mTempRecv != null) {
            mReader.setListener(mTempRecv);
            mTempRecv = null;
        }
        new Thread(mReader).start();
    }

    @Override
    public void sendMessage(String msg) {
        if(mSocket == null || !mSocket.isConnected() || mOut == null)
            throw new RuntimeException("Cannot send message. Socket is closed.");
        mOut.println(msg);
        mOut.flush();
    }

    @Override
    public void setOnReceiveListener(OnReceiveListener listener) {
        if(mReader != null) {
            mReader.setListener(listener);
        } else {
            mTempRecv = listener;
        }
    }

    /* Handle incoming messages and dispatch them to the proper listener */
    private class ReadRunnable implements Runnable {
        private final Logger mLog = Logger.getLogger(ReadRunnable.class.getName());
        private OnReceiveListener mListener = null;
        private final Socket mSocket;
        private final BufferedReader mReader;

        public ReadRunnable(Socket socket, BufferedReader reader) {
            mSocket = socket;
            mReader = reader;
        }

        public void setListener(OnReceiveListener listener) {
            mListener = listener;
        }

        @Override
        public void run() {
            try  {
                while(mSocket != null && mSocket.isConnected()) {
                    String line = mReader.readLine();
                    if(mListener != null)
                        mListener.onReceive(line.trim());
                }
            } catch (Exception e) {
                mLog.log(Level.INFO, "Connection closed:" + e.toString());
            } finally {
                System.out.println("Closing socket");
                try { if(mIn != null) mIn.close();} catch(Exception e) {}
                try { if(mOut != null) mOut.close();} catch(Exception e) {}
                try { if(mSocket != null) mSocket.close();} catch(Exception e) {}
                if(mListener != null)
                    mListener.onDisconnect();
            }
        }

    }

    @Override
    public boolean isOnline() {
        if(mSocket != null && mSocket.isConnected())
            return true;
        return false;
    }

    @Override
    public void disconnect() {
        if(mSocket != null && mSocket.isConnected())
            try {
                mSocket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
    }
}
