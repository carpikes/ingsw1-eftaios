package it.polimi.ingsw.server;

import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.GameCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** The server
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 8, 2015
 */
public class Server {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    /** Singleton Holder */
    private static class Holder {
        private static final Server INSTANCE = new Server();
    }

    public static Server getInstance() {
        return Holder.INSTANCE;
    }

    /** This list contains all listeners running (e.g. Socket listener, RMI, ...) */
    private List<Listener> mServers;
    
    /** This list contains all games active and running */
    private List<GameManager> mGamesRunning;
    
    /** Number of connected clients */
    private Integer mConnectedClients;
    
    /** Stop event. If true, the server will stop running */
    private boolean mStopEvent = false;

    /** This game is waiting for new players */
    private GameManager mCurGame = null;

    private Server() { 
        mServers = new ArrayList<Listener>();
        mGamesRunning = new ArrayList<GameManager>();
        mConnectedClients = 0;

        ServerTCP tcp = new ServerTCP(Config.SERVER_TCP_LISTEN_PORT);
        ServerRMI rmi = new ServerRMI();

        mServers.add(tcp);
        mServers.add(rmi);
    }

    /** New client connected: call this method to add him in a new game
     * 
     * @param conn  The client connection
     * @return      True if the client is correctly added in a game
     */
    public synchronized boolean addClient(ClientConn conn) {
        // Invariant: mCurGame cannot be full here
        
        if(mConnectedClients >= Config.SERVER_MAX_CLIENTS) {
            conn.sendPacket(GameCommand.CMD_SC_FULL);
            return false;
        }
        
        mConnectedClients++;
    
        if(mCurGame == null) {
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
            throw new RuntimeException("0 Clients connected. What's Happening?");
    }
    
    /** Removes a game from the games-to-update list 
     * 
     * @param game  Game you want to remove
     */
    public void removeGame(GameManager game) {
        synchronized(mGamesRunning) {
            if(mCurGame != null && game.equals(mCurGame))
                mCurGame = null;
            else
                mGamesRunning.remove(game);
        }
    }

    /** Start the server and put listeners online. */
    public void runServer() {
        for(Listener server : mServers)
            new Thread(server).start();

        try {
            while(!mStopEvent) {
                if(mCurGame != null) {
                    if(mCurGame.isReady()) {
                        LOG.log(Level.INFO, "Game ready! Stopping new incoming connections");
                        synchronized(mGamesRunning) {
                            mGamesRunning.add(mCurGame);
                        }
                        mCurGame = null;
                    }
                }
                synchronized(mGamesRunning) {
                    for (GameManager g : mGamesRunning)
                        g.update();
                }
                
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            LOG.log(Level.FINER, e.toString(), e);
        }
        
        for(Listener server : mServers)
            server.tearDown();
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
        if(mServers == null)
            return false;
        
        for(Listener server : mServers)
            if(!server.isUp())
                return false;
        return true;
    }
}
