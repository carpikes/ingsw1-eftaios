package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class ClientTCPConn extends ClientConn {
    private static final Logger mLog = Logger.getLogger(ClientTCPConn.class.getName());
    private final PrintWriter mOut;
    private final BufferedReader mIn;
    private final Socket mSocket;

    public ClientTCPConn(Socket socket) throws IOException{
        mOut = new PrintWriter(socket.getOutputStream());
        mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        mSocket = socket;
    }

    public void run() {
        try {
            while(mSocket.isConnected()) {
                String msg = mIn.readLine().trim();
                mClient.handleMessage(msg);
            }
        } catch(IOException e) {
            mLog.log(Level.FINE, "Connection closed: " + e.toString());
        }

        mClient.handleDisconnect();
    }

    @Override
    public void sendCommand(String cmd) {
        mOut.println(cmd.trim());
        mOut.flush();
    }

    @Override
    public void disconnect() {
        if(mOut != null) {
            mOut.println("BYE");
            mOut.flush();
            mOut.close();
        }
        try {
            mIn.close();
            mSocket.close();
        } catch (IOException e) {
            mLog.log(Level.FINE, "Sockets are already closed: " + e.toString());
        }
    }
}
