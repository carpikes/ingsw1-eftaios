package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    private static final Logger mLog = Logger.getLogger(Game.class.getName());

    private static enum States {
        WAITING_FOR_USERS,
        GAME_RUNNING,
    };
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

    public void setRunning() {

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
                c.sendMessage("RUN"); // TODO: something better here

            mIsRunning = true;
        } else {
            // Game is running here
        }
    }
}
