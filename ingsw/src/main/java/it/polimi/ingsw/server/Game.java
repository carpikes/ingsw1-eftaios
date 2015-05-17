package it.polimi.ingsw.server;

import it.polimi.ingsw.config.Config;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

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

    private boolean mIsReady = false;
    private boolean mIsRunning = false;
    private final long mStartTime;
    private List<Client> mClients = new ArrayList<Client>(); 

    public Game() {
        mStartTime = System.currentTimeMillis();
    }

    public synchronized boolean addPlayer(Client client) {
        if(mIsRunning || mClients.size() >= Config.GAME_MAX_PLAYERS)
            return false;

        mClients.add(client);
        client.sendPacket(new NetworkPacket(GameCommands.CMD_SC_TIME, String.valueOf(getRemainingTime())));
        broadcastPacket(new NetworkPacket(GameCommands.CMD_SC_STAT, String.valueOf(mClients.size())));

        return true;
    }

    public synchronized boolean isFull() {
        return mClients.size() >= Config.GAME_MAX_PLAYERS;
    }

    // Check if the game is ready to start
    public synchronized boolean isReady() {
        if(getNumberOfPlayers() < Config.GAME_MIN_PLAYERS || getRemainingTime() > 0)
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
        long rem = Config.GAME_WAITING_SECS - (System.currentTimeMillis() - mStartTime)/1000;
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
    public synchronized void update() {
        
        for(Client c : mClients)
            if(c != null)
                c.update();
        
        if(!mIsRunning) {
            // Game is ready but it is not running 
            for(Client client : mClients)
                if(!client.hasUsername()) {
                    // Some slow users still haven't typed their name
                    return;
                }
            // Yeah, let's start
            mLog.log(Level.INFO, "Players ready! Rolling the dice and starting up...");

            broadcastPacket(GameCommands.CMD_SC_RUN);

            mIsRunning = true;
        } else {
            // Game is running here
        }
    }
   
    public void broadcastPacket(int opcode) {
        for(Client c : mClients)
            c.sendPacket(opcode);
    }
    
    public synchronized void broadcastPacket(NetworkPacket pkt) {
        for(Client c : mClients)
            c.sendPacket(pkt);
    }
    
    public synchronized void removeClient(Client client) {
        if(mClients.remove(client) == false)
            throw new RuntimeException("Are you trying to remove a non-existent client?");
        
        // Decrement global user counter
        Server.getInstance().removeClient();
        
        broadcastPacket(new NetworkPacket(GameCommands.CMD_SC_STAT, String.valueOf(mClients.size())));
        if(mClients.size() < Config.GAME_MIN_PLAYERS && mIsRunning) {
            broadcastPacket(GameCommands.CMD_SC_WIN);
            Server.getInstance().removeGame(this);
        }
    }
}
