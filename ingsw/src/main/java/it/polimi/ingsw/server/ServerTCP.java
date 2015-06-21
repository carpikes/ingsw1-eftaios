package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/** TCP Server Listener
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 8, 2015
 */
class ServerTCP implements Listener {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(ServerTCP.class.getName());

    /** Pool of threads */
    private ExecutorService mCachedPool;

    /** The listening server */
    private ServerSocket mServer;

    /** True if the listener should be shutted down */
    private boolean mStopEvent = false;

    /** Listening port */
    private final int mPort;

    /** The constructor
     * 
     * @param port Listening port
     */
    public ServerTCP(int port) {
        mPort = port;
        mCachedPool = Executors.newCachedThreadPool();
    }

    /** Server loop */
    private void acceptConnection() {
        try {
            Socket s = mServer.accept();
            if(s != null && !mStopEvent) {
                ClientConn c = new ClientConnTCP(s);

                if(Server.getInstance().addClient(c))
                    mCachedPool.submit(c);
                else 
                    c.disconnect();
            }
        } catch(Exception e){
            LOG.log(Level.FINEST, "TCP Connection closed: " + e.toString(), e);
        }
    }

    /** Run the TCP listener
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            mServer = new ServerSocket(mPort);
            LOG.log(Level.INFO, "TCP Server is running");
            while (!mStopEvent) {
                acceptConnection();
            }
            mServer.close();
        } catch(IOException e){
            LOG.log(Level.SEVERE, "TCP Server is down: " + e.toString());
            LOG.log(Level.FINEST, "", e);
            Server.getInstance().tearDown();
        }
    }

    /** Shut down this listener
     * @see it.polimi.ingsw.server.Listener#tearDown()
     */
    @Override
    public synchronized void tearDown() {
        mStopEvent = true;
        try {
            if(mServer != null)
                mServer.close();
        } catch (IOException e) {
            LOG.log(Level.FINEST, e.toString(), e);
        }
    }

    /** True if this listener is shut down
     * @see it.polimi.ingsw.server.Listener#isDown()
     * @return True if is down
     */
    @Override
    public synchronized boolean isDown() {
        if(mServer == null)
            return true;
        if(mServer.isClosed())
            return true;
        return false;
    }

    /** True if this listener is loaded successfully
     * @see it.polimi.ingsw.server.Listener#isUp()
     * @return True if is loaded
     */
    @Override
    public boolean isUp() {
        if(mServer == null)
            return false;
        if(mServer.isBound() && !mServer.isClosed())
            return true;
        return false;
    }
}
