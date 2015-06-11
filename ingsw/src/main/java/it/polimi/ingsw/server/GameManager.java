package it.polimi.ingsw.server;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.CoreOpcode;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameStartInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    private List<Client> mClients = new ArrayList<Client>();

    /** ChosenMap */
    private Integer mChosenMapId = null;
    
    /** Game state */
    private GameState mState = null;

    private int mAwayClients = 0;

    private Queue<Client> mClientsToRemove = new LinkedList<Client>();
    
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
        client.sendPacket(new GameCommand(CoreOpcode.CMD_SC_TIME, (Integer) getRemainingLoginTime()));
        client.sendPacket(CoreOpcode.CMD_SC_CHOOSEUSER);
        broadcastPacket(new GameCommand(CoreOpcode.CMD_SC_STAT, (Integer) mClients.size()));
        
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
    public synchronized int getNumberOfClients() {
        return mClients.size();
    }

    /** Get how many seconds are left before the game starts
     * 
     * @return Remaining time
     */
    public synchronized int getRemainingLoginTime() {
        long rem = Config.GAME_WAITING_SECS - (System.currentTimeMillis() - mStartTime)/1000;
        return (int)rem > 0 ? (int)rem : 0;
    }

    /** Check if a player can set the name he wants
     * 
     * @param name      The name
     * @return          True if the player can set this name
     */
    public synchronized boolean canSetName(String name) {
        if(name == null)
            return false;
        
        if(name.length() > 32)
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
        
        // check connection for every client
        for(Client c : mClients)
            if(c != null)
                c.update();
        
        // remove client if needed
        while(!mClientsToRemove.isEmpty()) {
            Client c = mClientsToRemove.remove();
            removeClient(c);
        }
        
        if(!mIsRunning) {
            int mFinalConnClients = 0;
            // Game is ready but it is not running 
            for(Client client : mClients) {
                if(!client.isConnected())
                    throw new RuntimeException("Client can't be inactive before the game is started. What's happening?");
                
                if(!client.hasUsername()) {
                    // Some slow users still haven't typed their name
                    return;
                }
                mFinalConnClients++;
            }
            
            // map not chosen yet
            if(mChosenMapId == null)
                return;
            
            // game is ready to start
            if(mFinalConnClients >= Config.GAME_MIN_PLAYERS)
                startGame();
            else
                shutdown();
        } else {
            if(mState != null)
                // update game
                mState.update();
        }
    }

    /** Start a game */
    private void startGame() {
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
            mClients.get(i).sendPacket(new GameCommand(CoreOpcode.CMD_SC_RUN, info));
        }

        mIsRunning = true;
    }
   
    /** Send a packet to all players connected
     * 
     * @param pkt Packet
     */
    public synchronized void broadcastPacket(GameCommand pkt) {
        LOG.log(Level.FINE, "Broadcasting " + pkt.getOpcode().toString());
        for(Client c : mClients)
            if(c.isConnected())
                c.sendPacket(pkt);
    }
    
    /** Do not call this guy directly! Use enqueueRemoveGame.
     * Remove a client from this game, but if this method is called inside
     * a for(Client c : mClients) will throw an exception.
     * 
     * @param client The client
     */
    private void removeClient(Client client) {
        boolean mustAskForMap = false;
        int index = mClients.indexOf(client);
        if(index == 0 && mChosenMapId == null)
            mustAskForMap = true;
        
        // DO NOT REMOVE THE CLIENT IF THE GAME IS RUNNING.
        if(mIsRunning)
            mAwayClients++;
        else
            if(mClients.remove(index) == null)
                throw new RuntimeException("Are you trying to remove a non-existent client?");
        
        if(mAwayClients > getNumberOfClients())
            throw new RuntimeException("AwayClients >= ConnectedClients. What's Happening?");
        
        // Decrement global user counter
        Server.getInstance().removeClient();
        
        broadcastPacket(new GameCommand(CoreOpcode.CMD_SC_STAT, getNumberOfClients() - mAwayClients));
        
        // FIXME: Could be possible that a client joins the game while enqueued for removal?
        if(getNumberOfClients() - mAwayClients == 0)
            Server.getInstance().enqueueRemoveGame(this);

        boolean canAskAtLeastOne = false;
        // Game is still alive
        if(mustAskForMap && !mClients.isEmpty()) {
            for(Client c : mClients)
                if(c.isConnected()) {
                    askForMap(c);
                    canAskAtLeastOne = true;
                    break;
                }
        }
        
        if((mIsRunning && mState != null) || (!canAskAtLeastOne && mustAskForMap))
            mState.onPlayerDisconnect(index);
        
        LOG.log(Level.INFO, "Player disconnected. Game Running? " + String.valueOf(mIsRunning) + ". Clients connected: " + (getNumberOfClients() - mAwayClients));
    }

    /** Remove a client from the game
     * @param client The client
     */
    public void enqueueRemoveClient(Client client) {
        synchronized(mClientsToRemove) {
            if(!mClientsToRemove.contains(client))
                mClientsToRemove.add(client);
        }
    }

    /** Ask to client which map to use
     * 
     * @param client The client
     */
    private void askForMap(Client client) {
        GameCommand pkt;
        
        if(!client.isConnected())
            throw new RuntimeException("I am trying to ask a map to an inactive client. What's happening?");
        pkt = new GameCommand(CoreOpcode.CMD_SC_CHOOSEMAP, (Serializable[]) GameMap.getListOfMaps());
        client.sendPacket(pkt);
    }

    /** Handle a packet
     * @param client
     * @param pkt
     */
    public void handlePacket(Client client, GameCommand pkt) {
        if(!mIsRunning)
            LOG.log(Level.SEVERE, "Game is not started yet. What's happening?");
        
        if(mState == null)
        	return;
        
        int turnId = mState.getTurnId();
        if(turnId >=0 && turnId <= mClients.size() && mClients.get(turnId).equals(client)) {
            if(!mClients.get(turnId).isConnected())
                throw new RuntimeException("GameState is broken: mTurnId -> AwayPlayer. What's happening?");
            mState.enqueuePacket(pkt);
        }
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

    /** Send a packet to an user
     * 
     * @param id User id
     * @param networkPacket Packet content
     */
    public void sendDirectPacket(int id, GameCommand networkPacket) {
    	LOG.log(Level.FINE, "Sending packet to " + id + ": " + networkPacket.getOpcode().toString());
    	Client c = mClients.get(id); 
    	if(c.isConnected())
    	    c.sendPacket(networkPacket);
    }

    /** Shutdown the game */
	public void shutdown() {
		for(Client c : mClients)
		    if(c.isConnected())
		        c.handleDisconnect();
		
		Server.getInstance().enqueueRemoveGame(this);
	}
    
}
