package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger mLog = Logger.getLogger(Server.class.getName());

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
    private static final int mMaxPlayers = 8;
    private List<Runnable> mServers;
    private List<Game> mGamesRunning;
    private Game mCurGame = null;
    
    private Server() { 
    	mServers = new ArrayList<Runnable>();
    	mGamesRunning = new ArrayList<Game>();
    	
        ServerTCP tcp = new ServerTCP(mTCPPort);
        mServers.add(tcp);
    }
    
    public synchronized void addClient(ClientConn conn) {
        /* Invariant: mCurGame cannot be full here */

    	if(mCurGame == null) {
    		mCurGame = new Game();
        }

    	Client client = new Client(conn,mCurGame);

        if(!mCurGame.addPlayer(client)) {
            /* No game may be full here */
            mLog.log(Level.SEVERE, "Game full in a wrong way. What's Happening?");
            client.handleDisconnect();
            return;
        }

        if(mCurGame.isFull()) {
        	mGamesRunning.add(mCurGame);
        	mCurGame = null;
        }
    }

    public void runServer() {
    	for(Runnable server : mServers)
    		new Thread(server).start();
    	
		try {
			for(;;)
				Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
