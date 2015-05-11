package it.polimi.ingsw.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class TCPConnection extends Connection {
    private String mHost;
    private int mPort = -1;
    private boolean mInited = false;

    private Socket mSocket = null;
    private PrintWriter mOut = null;
    private BufferedReader mIn = null;
    private ReadRunnable mRead = null;

    /* This thing is only used if mRead is not initialized yet */
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
        if(mInited) {
            if(mSocket == null) {
                mSocket = new Socket(mHost, mPort);
                mOut = new PrintWriter(mSocket.getOutputStream());
                mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mRead = new ReadRunnable(mSocket, mIn);
                if(mTempRecv != null) {
                    mRead.setListener(mTempRecv);
                    mTempRecv = null;
                }
                new Thread(mRead).start();
            } else
                throw new RuntimeException("Socket already created");
        } else
            throw new RuntimeException("TCPConnection must be configured before use");
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
        if(mRead != null) {
            mRead.setListener(listener);
        } else {
            mTempRecv = listener;
        }
    }

    /* Handle incoming messages and dispatch them to the proper listener */
    private class ReadRunnable implements Runnable {
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
                    if(mListener != null) {
                        synchronized(mListener) {
                            if(mListener != null)
                                mListener.onReceive(line.trim());
                        }
                    }
                }
            } catch (Exception e) {
                // Connection closed.
            } finally {
                System.out.println("Closing socket");
                try { if(mIn != null) mIn.close();} catch(Exception e) {}
                try { if(mOut != null) mOut.close();} catch(Exception e) {}
                try { if(mSocket != null) mSocket.close();} catch(Exception e) {}
            }
        }

    }

    @Override
    public boolean isOnline() {
        if(mSocket != null && mSocket.isConnected())
            return true;
        return false;
    }
}
