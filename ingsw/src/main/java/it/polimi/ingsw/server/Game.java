package it.polimi.ingsw.server;

import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Game manager
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 9, 2015
 */
class Game {
    private static final Logger LOG = Logger.getLogger(Game.class.getName());
    
    /** Game is running. New players can't connect to this game */
    private boolean mIsRunning = false;
    
    /** Game start time */
    private final long mStartTime;
    
    /** Clients connected */
    private List<Client> mClients = new ArrayList<Client>(); 

    public Game() {
        mStartTime = System.currentTimeMillis();
    }

    /** Add new clients to this game
     * 
     * @param client    New client instance
     * @return          True if the player is added correctly
     */
    public synchronized boolean addPlayer(Client client) {
        if(mIsRunning || mClients.size() >= Config.GAME_MAX_PLAYERS)
            return false;

        mClients.add(client);
        client.sendPacket(new NetworkPacket(GameCommands.CMD_SC_TIME, String.valueOf(getRemainingTime())));
        broadcastPacket(new NetworkPacket(GameCommands.CMD_SC_STAT, String.valueOf(mClients.size())));

        return true;
    }

    /** Check if number of players is greater or equal to the maximum number of players per game
     * 
     * @return True if this game is full
     */
    public synchronized boolean isFull() {
        return mClients.size() >= Config.GAME_MAX_PLAYERS;
    }

    /** Check if the game is ready to start
     * 
     * @return True if the game is ready to start
     */
    public synchronized boolean isReady() {
        if((getNumberOfPlayers() < Config.GAME_MIN_PLAYERS || getRemainingTime() > 0) && !mIsRunning)
            return false;
        
        if(mIsRunning)
            return true;
        
        for(Client i : mClients)
            if(!i.hasUsername())
                return false;

        return true;
    }

    /** Check if the game is running
     * 
     * @return True if the game is running (new players can't join)
     */
    public synchronized boolean isRunning() {
        return mIsRunning;
    }

    /** Get number of connected players
     * 
     * @return Number of connected players
     */
    public synchronized int getNumberOfPlayers() {
        return mClients.size();
    }

    /** Get how many seconds are left to start the game
     * 
     * @return Remaining time
     */
    public synchronized long getRemainingTime() {
        long rem = Config.GAME_WAITING_SECS - (System.currentTimeMillis() - mStartTime)/1000;
        return rem > 0 ? rem : 0;
    }

    /** Check if a player can set the name he wants
     * 
     * @param name      The name
     * @return          True if the player can set this name
     */
    public synchronized boolean canSetName(String name) {
        for(Client c : mClients) {
            if(name.equalsIgnoreCase(c.getUsername()))
                return false;
        } 
        return true;
    }

    /** This method is called on each game update cycle
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
            LOG.log(Level.INFO, "Players ready! Rolling the dice and starting up...");

            broadcastPacket(GameCommands.CMD_SC_RUN);

            mIsRunning = true;
        } else {
            // Game is running here
        }
    }
   
    /** Send a packet without arguments to all players connected
     * 
     * @param opcode Packet opcode
     */
    public void broadcastPacket(int opcode) {
        for(Client c : mClients)
            c.sendPacket(opcode);
    }
    
    /** Send a packet to all players connected
     * 
     * @param pkt Packet
     */
    public synchronized void broadcastPacket(NetworkPacket pkt) {
        for(Client c : mClients)
            c.sendPacket(pkt);
    }
    
    /** Remove a client from this game
     * 
     * @param client The client
     */
    public synchronized void removeClient(Client client) {
        if(mClients.remove(client) == false)
            throw new RuntimeException("Are you trying to remove a non-existent client?");
        
        // Decrement global user counter
        Server.getInstance().removeClient();
        
        broadcastPacket(new NetworkPacket(GameCommands.CMD_SC_STAT, String.valueOf(mClients.size())));
        if(mClients.size() == 0 || (mClients.size() < Config.GAME_MIN_PLAYERS && mIsRunning)) {
            broadcastPacket(GameCommands.CMD_SC_WIN);
            Server.getInstance().removeGame(this);
        }
    }
}
