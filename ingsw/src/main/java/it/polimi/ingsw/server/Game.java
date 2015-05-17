package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 9, 2015
 */

class Game {
    private static final Logger mLog = Logger.getLogger(Game.class.getName());

    // TODO: another hardcoded constant here
    private static final int mMinPlayers = 2;
    private static final int mMaxPlayers = 8;
    private static final int mWaitingSecs = 30;

    private boolean mIsReady = false;
    private boolean mIsRunning = false;
    private final long mStartTime;
    private List<Client> mClients = new ArrayList<Client>(); 

    public Game() {
        mStartTime = System.currentTimeMillis();
    }

    public synchronized boolean addPlayer(Client client) {
        if(mIsRunning || mClients.size() >= mMaxPlayers)
            return false;

        mClients.add(client);
        client.sendMessage("TIME " + getRemainingTime());

        for(Client c : mClients)
            c.sendMessage("STAT " + mClients.size());

        return true;
    }

    public synchronized boolean isFull() {
        return mClients.size() >= mMaxPlayers;
    }

    // Check if the game is ready to start
    public synchronized boolean isReady() {
        if(getNumberOfPlayers() < mMinPlayers || getRemainingTime() > 0)
            return false;

        return true;
    }

    public synchronized boolean isRunning() {
        return mIsReady;
    }

    public synchronized int getNumberOfPlayers() {
        return mClients.size();
    }

    public synchronized long getRemainingTime() {
        long rem = mWaitingSecs - (System.currentTimeMillis() - mStartTime)/1000;
        return rem > 0 ? rem : 0;
    }

    public synchronized boolean canSetName(String name) {
        for(Client c : mClients) {
            if(name.equalsIgnoreCase(c.getUsername()))
                return false;
        } 
        return true;
    }

    /**
     * This method is called on each game update cycle
     * @todo notify users about someone without username
     * @todo generate a random username if someone won't choose it
     */
    public void update() {
        if(!mIsRunning) {
            // Game is ready but it is not running 
            for(Client client : mClients)
                if(!client.hasUsername()) {
                    // Some slow users still haven't typed their name
                    return;
                }
            // Yeah, let's start
            mLog.log(Level.INFO, "Players ready! Rolling the dice and starting up...");

            for(Client c : mClients)
                c.sendMessage("RUN");

            mIsRunning = true;
        } else {
            // Game is running here
        }
    }
    
    public void removeClient(Client client) {
        mClients.remove(client);
        if(mClients.size() < mMinPlayers) {
            for(Client c : mClients)
                c.sendMessage("WIN");
            Server.getInstance().removeGame(this);
        }
    }
}
