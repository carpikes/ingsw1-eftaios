package it.polimi.ingsw.server;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.GameCommand;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Game manager
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 9, 2015
 */
public class GameManager {
    private static final Logger LOG = Logger.getLogger(GameManager.class.getName());
    
    /** Game is running. New players can't connect to this game */
    private boolean mIsRunning = false;
    
    /** Game start time */
    private final long mStartTime;
    
    /** Clients connected */
    private List<Client> mClients = new LinkedList<Client>();

    /** ChosenMap */
    private Integer mChosenMapId = null;
    
    /** Game state */
    private GameState mState = null;
    
    public GameManager() {
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
        client.sendPacket(new GameCommand(GameOpcode.CMD_SC_TIME, String.valueOf(getRemainingLoginTime())));
        client.sendPacket(GameOpcode.CMD_SC_CHOOSEUSER);
        broadcastPacket(new GameCommand(GameOpcode.CMD_SC_STAT, String.valueOf(mClients.size())));
        
        // @first client: ask for map
        if(mClients.size() == 1)
            askForMap(client);

        return true;
    }

    /** Check if number of players is greater or equal to the maximum number of players per game
     * 
     * @return True if this game is full
     */
    public synchronized boolean isFull() {
        return mClients.size() >= Config.GAME_MAX_PLAYERS;
    }

    /** Check if the game is ready to start and can't accept new connections
     * 
     * @return True if the game is ready to start
     */
    public synchronized boolean isReady() {
        if((getRemainingLoginTime() == 0 && mClients.size() >= Config.GAME_MIN_PLAYERS) || mClients.size() == Config.GAME_MAX_PLAYERS)
            return true;
        
        if(mIsRunning)
            return true;
        
        return false;
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

    /** Get how many seconds are left before the game starts
     * 
     * @return Remaining time
     */
    public synchronized long getRemainingLoginTime() {
        long rem = Config.GAME_WAITING_SECS - (System.currentTimeMillis() - mStartTime)/1000;
        return rem > 0 ? rem : 0;
    }

    /** Check if a player can set the name he wants
     * 
     * @param name      The name
     * @return          True if the player can set this name
     */
    public synchronized boolean canSetName(String name) {
        if(name == null)
            return false;
        
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
            
            if(mChosenMapId == null)
                return;
            
            // Let the game begin
            LOG.log(Level.INFO, "Players ready! Rolling the dice and starting up...");

            Collections.shuffle(mClients);
            
            mState = new GameState(this, mChosenMapId);
            
            LOG.log(Level.INFO, mClients.get(0).getUsername() + " is the first player");
            
            // Send infos to all players
            EnemyInfo[] userList = new EnemyInfo[mClients.size()];
            
            for(int i = 0; i < mClients.size(); i++)
                userList[i] = new EnemyInfo(mClients.get(i).getUsername());
            
            for(int i = 0; i < mClients.size(); i++) {
                GameStartInfo info = mState.buildInfoContainer(userList, i);
                mClients.get(i).sendPacket(new GameCommand(GameOpcode.CMD_SC_RUN, info));
            }

            mIsRunning = true;
        } else {
            if(mState != null)
                mState.update();
        }
    }

    /** Send a packet without arguments to all players connected
     * 
     * @param opcode Packet opcode
     */
    public void broadcastPacket(GameOpcode opcode) {
        for(Client c : mClients)
            if(!c.isInactive())
                c.sendPacket(opcode);
    }
    
    /** Send a packet to all players connected
     * 
     * @param pkt Packet
     */
    public synchronized void broadcastPacket(GameCommand pkt) {
        for(Client c : mClients)
            if(!c.isInactive())
                c.sendPacket(pkt);
    }
    
    /** Remove a client from this game
     * 
     * @param client The client
     */
    public synchronized void removeClient(Client client) {
        boolean mustAskForMap = false;
        if(mClients.get(0).equals(client) && mChosenMapId == null)
            mustAskForMap = true;
        
        // DO NOT REMOVE THE CLIENT IF THE GAME IS RUNNING.
        if(!mIsRunning)
            if(mClients.remove(client) == false)
                throw new RuntimeException("Are you trying to remove a non-existent client?");
        
        // Decrement global user counter
        Server.getInstance().removeClient();
        
        broadcastPacket(new GameCommand(GameOpcode.CMD_SC_STAT, String.valueOf(mClients.size())));
        if(mClients.size() == 0)
            Server.getInstance().enqueueRemoveGame(this);

        // Game is still alive
        if(mustAskForMap && mClients.size() > 0)
            askForMap(mClients.get(0));
        
        if(mIsRunning && mState != null)
            mState.onPlayerDisconnect(mClients.indexOf(client));
    }

    /** Ask to client which map to use
     * 
     * @param client The client
     */
    private void askForMap(Client client) {
        GameCommand pkt;
        
        pkt = new GameCommand(GameOpcode.CMD_SC_CHOOSEMAP, (Serializable[]) GameMap.getListOfMaps());
        client.sendPacket(pkt);
    }

    /**
     * @param client
     * @param pkt
     */
    public void handlePacket(Client client, GameCommand pkt) {
        if(!mIsRunning)
            LOG.log(Level.SEVERE, "Game is not started yet. What's happening?");
        
        if(mState == null)
        	return;
        
        int turnId = mState.getTurnId();
        if(turnId >=0 && turnId <= mClients.size() && mClients.get(turnId).equals(client))
            mState.queuePacket(pkt);
    }

    /** Save the map chosen by the first player
     * 
     * @param client The player
     * @param chosenMap The chosen map
     * @return True if the choice is valid
     */
    public boolean setMap(Client client, Integer chosenMap) {
        if(mChosenMapId != null)
            return false;
        
        if(mClients.size() > 0 && mClients.get(0).equals(client) && GameMap.isValidMap(chosenMap)) {
            mChosenMapId = chosenMap;
            return true;
        }
        
        return false;
    }
    
    /** Get a connection object
     * 
     * @param i Connection id
     * @return
     */
    public Client getPlayerConnection(int i) {
        return mClients.get(i);
    }

    public void sendDirectPacket(int id, GameCommand networkPacket) {
    	LOG.log(Level.FINE, "Sending packet to " + id + ": " + networkPacket.getOpcode().toString());
        mClients.get(id).sendPacket(networkPacket);
    }

    public GameState getGameState() {
        return mState;
    }

	public void shutdown() {
		for(Client c : mClients)
		    if(!c.isInactive())
		        c.handleDisconnect();
		
		Server.getInstance().enqueueRemoveGame(this);
	}
    
}
