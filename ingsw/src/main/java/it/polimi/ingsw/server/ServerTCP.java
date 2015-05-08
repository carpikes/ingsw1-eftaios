package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerTCP implements Runnable{
    private static final Logger mLog = Logger.getLogger(ServerTCP.class.getName());
    private ExecutorService mCachedPool;
    private ServerSocket mServer;
    public final int mPort;

    public ServerTCP(int port) {
        mPort = port;
        mCachedPool = Executors.newCachedThreadPool();
    }
    
    public void run() {
        try {
            mServer = new ServerSocket(mPort);
            mLog.log(Level.INFO, "TCP Server is running");
            while (true) {
                try {
                    Socket s = mServer.accept();
                    ClientConn c = new ClientTCPConn(s);
                    
                    mCachedPool.submit(c);
                    Server.getInstance().addClient(c);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        } catch(IOException e){
        	mLog.log(Level.SEVERE, "TCP Server is down");
            e.printStackTrace();
        }
    }
}
