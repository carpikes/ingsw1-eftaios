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

class ServerTCP implements Listener {
    private static final Logger LOG = Logger.getLogger(ServerTCP.class.getName());
    private ExecutorService mCachedPool;
    private ServerSocket mServer;
    private boolean mStopEvent = false;
    private final int mPort;

    public ServerTCP(int port) {
        mPort = port;
        mCachedPool = Executors.newCachedThreadPool();
    }
    
    private void acceptConnection() {
        try {
            Socket s = mServer.accept();
            ClientConn c = new ClientConnTCP(s);

            if(Server.getInstance().addClient(c))
                mCachedPool.submit(c);
            else 
                c.disconnect();
        } catch(Exception e){
            LOG.log(Level.WARNING, "TCP Connection closed: " + e.toString(), e);
        }
    }

    public void run() {
        try {
            mServer = new ServerSocket(mPort);
            LOG.log(Level.INFO, "TCP Server is running");
            while (!mStopEvent) {
                acceptConnection();
            }
            mServer.close();
        } catch(IOException e){
            LOG.log(Level.SEVERE, "TCP Server is down: " + e.toString(), e);
        }
    }

    @Override
    public synchronized void tearDown() {
        mStopEvent = true;
        try {
            if(mServer != null)
                mServer.close();
        } catch (IOException e) {
            LOG.log(Level.FINEST, e.toString());
        }
    }

    @Override
    public synchronized boolean isDown() {
        if(mServer == null)
            return true;
        if(mServer.isClosed())
            return true;
        return false;
    }

    @Override
    public boolean isUp() {
        if(mServer == null)
            return false;
        if(mServer.isBound() && !mServer.isClosed())
            return true;
        return false;
    }
}
