package it.polimi.ingsw.server;

import it.polimi.ingsw.game.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 8, 2015
 */

public class Server {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private static class Holder {
        private static final Server INSTANCE = new Server();
    }

    public static Server getInstance() {
        return Holder.INSTANCE;
    }

    /* TODO: mTCPPort must not be hardcoded here
     * Why port 3834? 3834 = 0xEFA -> Escape From Aliens
     * Note: ports < 1024 are privileges 
     */
    private static final int mTCPPort = 3834;
    private List<Runnable> mServers;
    private List<Game> mGamesRunning;
    
    private Integer mConnectedClients;

    /* New players will be added here */
    private Game mCurGame = null;

    private Server() { 
        mServers = new ArrayList<Runnable>();
        mGamesRunning = new ArrayList<Game>();
        mConnectedClients = 0;

        ServerTCP tcp = new ServerTCP(mTCPPort);
        ServerRMI rmi = new ServerRMI();

        mServers.add(tcp);
        mServers.add(rmi);
    }

    public synchronized boolean addClient(ClientConn conn) {
        // Invariant: mCurGame cannot be full here
        
        if(mConnectedClients >= Config.SERVER_MAX_CLIENTS) 
            return false;
    
        if(mCurGame == null) {
            mCurGame = new Game();
        }

        Client client = new Client(conn,mCurGame);

        if(!mCurGame.addPlayer(client)) {
            // No game may be full here
            LOG.log(Level.SEVERE, "Game full in a wrong way. What's Happening?");
            client.handleDisconnect();
            return false;
        }

        if(mCurGame.isFull()) {
            mGamesRunning.add(mCurGame);
            mCurGame = null;
        }
        
        mConnectedClients++;
        return true;
    }
    
    public synchronized void removeClient() {
        if(mConnectedClients > 0)
            mConnectedClients--;
        else
            throw new RuntimeException("0 Clients connected. What's Happening?");
    }
    
    public synchronized void removeGame(Game g) {
        if(mCurGame != null && g.equals(mCurGame))
            mCurGame = null;
        else
            mGamesRunning.remove(g);
    }

    public void runServer() {
        for(Runnable server : mServers)
            new Thread(server).start();

        try {
            for(;;) {
                if(mCurGame != null) {
                    if(mCurGame.isReady()) {
                        LOG.log(Level.INFO, "Game ready! Stopping new incoming connections");
                        mGamesRunning.add(mCurGame);
                        mCurGame = null;
                    }
                }
                synchronized(mGamesRunning) {
                    for (Game g : mGamesRunning)
                        g.update();
                }
                
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized int getConnectedClients() {
        return mConnectedClients;
    }
}
