package it.polimi.ingsw.server;

import it.polimi.ingsw.exception.DebugException;
import it.polimi.ingsw.exception.ServerException;
import it.polimi.ingsw.game.common.CoreOpcode;
import it.polimi.ingsw.game.config.Config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/** The server
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 8, 2015
 */
public class Server {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    /** This list contains all listeners running (e.g. Socket listener, RMI, ...) */
    private final List<Listener> mServers;

    /** This list contains all games active and running */
    private final List<GameManager> mGamesRunning;

    /** Number of connected clients */
    private Integer mConnectedClients;

    /** Stop event. If true, the server will stop running */
    private boolean mStopEvent = false;

    /** This game is waiting for new players */
    private GameManager mCurGame = null;

    /** All games queued to removal are here */ 
    private Queue<GameManager> mGamesToRemove;

    /** Debug mode */
    private boolean dDebugMode = false;

    /** Singleton Holder */
    private static class Holder {
        private Holder() {

        }

        /** The singleton instance */
        private static final Server INSTANCE = new Server();
    }

    /** Constructor */
    private Server() { 
        mServers = new ArrayList<Listener>();
        mGamesRunning = new ArrayList<GameManager>();
        mGamesToRemove = new LinkedList<GameManager>();
        mConnectedClients = 0;

        ServerTCP tcp = new ServerTCP(Config.SERVER_TCP_LISTEN_PORT);
        ServerRMI rmi = new ServerRMI();

        mServers.add(tcp);
        mServers.add(rmi);
    }

    /** Returns the current server instance
     * 
     * @return The instance
     */
    public static Server getInstance() {
        return Holder.INSTANCE;
    }

    /** New client connected: call this method to add him in a new game
     * 
     * @param conn  The client connection
     * @return      True if the client is correctly added in a game
     */
    public synchronized boolean addClient(ClientConn conn) {
        // Invariant: mCurGame cannot be full here

        if(mConnectedClients >= Config.SERVER_MAX_CLIENTS) {
            conn.sendPacket(CoreOpcode.CMD_SC_FULL);
            return false;
        }

        mConnectedClients++;

        if(mCurGame == null || mCurGame.getEffectiveClients() == 0) {
            mCurGame = new GameManager();
        }

        Client client = new Client(conn,mCurGame);

        if(!mCurGame.addPlayer(client)) {
            // No game may be full here
            client.handleDisconnect();
            return false;
        }

        synchronized(mGamesRunning) {
            if(mCurGame != null && mCurGame.isFull()) {
                mGamesRunning.add(mCurGame);
                mCurGame = null;
            } 
        }

        return true;
    }

    /** Decrement client counter */
    public synchronized void removeClient() {
        if(mConnectedClients > 0)
            mConnectedClients--;
        else
            throw new ServerException("0 Clients connected. What's Happening?");
    }

    /** Removes a game from the games-to-update list 
     * 
     * @param game  Game you want to remove
     */
    public void enqueueRemoveGame(GameManager game) {
        synchronized(mGamesRunning) {
            if(mCurGame != null && game.equals(mCurGame))
                mCurGame = null;
            else {
                if(!mGamesToRemove.contains(game))
                    mGamesToRemove.add(game);
            }
        }
    }

    /** Start the server and put listeners online. */
    public void runServer(boolean debugMode) {
        dDebugMode = debugMode;
        if(!dDebugMode) {  
            LOG.log(Level.FINE, "Starting up...");
            for(Listener server : mServers)
                new Thread(server).start();
        }

        try {
            while(!mStopEvent) {
                if(mCurGame != null && mCurGame.isReady()) {
                    LOG.log(Level.INFO, "Game ready! Stopping new incoming connections");
                    synchronized(mGamesRunning) {
                        mGamesRunning.add(mCurGame);
                    }
                    mCurGame = null;
                }

                synchronized(mGamesRunning) {
                    for (GameManager g : mGamesRunning)
                        g.update();

                    while(!mGamesToRemove.isEmpty()) {
                        GameManager g = mGamesToRemove.remove();
                        mGamesRunning.remove(g);
                    }
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            LOG.log(Level.FINER, e.toString(), e);
        }

        if(!dDebugMode) {
            for(Listener server : mServers)
                server.tearDown();
        }
    }

    /** Get number of connected clients
     * 
     * @return Number of connected clients
     */
    public synchronized int getConnectedClients() {
        return mConnectedClients;
    }

    /** Shut down the server */
    public synchronized void tearDown() {
        mStopEvent = true;
    }

    /** Check if the server is shutted down
     * 
     * @return True if the server is correctly shutted down
     */
    public synchronized boolean isDown() {
        if(dDebugMode)
            throw new DebugException("Unsupported command in debug mode");

        if(mServers != null)
            for(Listener server : mServers)
                if(!server.isDown())
                    return false;
        return true;
    }

    /** Check if the server is correctly started up
     * 
     * @return True if the server is up and running
     */
    public synchronized boolean isUp() {
        if(dDebugMode)
            throw new DebugException("Unsupported command in debug mode");
        if(mServers == null)
            return false;

        for(Listener server : mServers)
            if(!server.isUp())
                return false;
        return true;
    }
}
