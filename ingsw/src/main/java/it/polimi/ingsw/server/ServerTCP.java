package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 8, 2015
 */

class ServerTCP implements Runnable{
    private static final Logger LOG = Logger.getLogger(ServerTCP.class.getName());
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
            LOG.log(Level.INFO, "TCP Server is running");
            while (true) {
                try {
                    Socket s = mServer.accept();
                    ClientConn c = new ClientConnTCP(s);

                    if(Server.getInstance().addClient(c))
                        mCachedPool.submit(c);
                    else 
                        c.disconnect();
                } catch(Exception e){
                    LOG.log(Level.WARNING, "TCP Connection closed: " + e.toString());
                }
            }
        } catch(IOException e){
            LOG.log(Level.SEVERE, "TCP Server is down: " + e.toString());
            e.printStackTrace();
        }
    }
}
