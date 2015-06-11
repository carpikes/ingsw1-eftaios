package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.DebugException;
import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.InfoOpcode;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.Human;
import it.polimi.ingsw.game.player.Role;
import it.polimi.ingsw.game.player.RoleBuilder;
import it.polimi.ingsw.game.state.AwayState;
import it.polimi.ingsw.game.state.DiscardingObjectCardState;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.LoserState;
import it.polimi.ingsw.game.state.NotMyTurnState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.StartTurnState;
import it.polimi.ingsw.game.state.WinnerState;
import it.polimi.ingsw.server.GameManager;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Class representing the current state of the game. It is the beating heart of the game: it holds
 * a list of all players, the mGameManager and an event queue of messages coming from the clients.
 * @author Michele Albanese (michele.albanese@mail.polimi.it) 
 * @since  May 21, 2015
 */
public class GameState {
    private static final Logger LOG = Logger.getLogger(GameState.class.getName());
    
    /** Game Manager */
    private final GameManager mManager;
    
    /** Input and output queue */
    private final Queue<GameCommand> mInputQueue;
    private final Queue<Map.Entry<Integer,GameCommand>> mOutputQueue;
    
    /** Game map */
    private final GameMap mMap;
    
    /** Players in game */
    private ArrayList<GamePlayer> mPlayers;
    
    /** Current turn id */
    private int mTurnId = 0;

    /** Number of rounds played */
    private int mRoundsPlayed = 0;
    
    /** Debug mode */
    private final boolean dDebugMode;

    private boolean dGameOver = false;

    private int dForceNextTurn = -1;
    
    public static enum LastThings {
        NEVERMIND,
        KILLED_HUMAN,
        GONE_OFFLINE,
        HUMAN_USED_HATCH
    };
    
    private LastThings mLastThing = LastThings.NEVERMIND;
    
    /**
     * Constructs a new game.
     * @param gameManager The GameManager that created this game.
     * @param mapId Id of the map
     * @param clients List of connections of all players
     */
    public GameState(GameManager gameManager, int mapId) {
        GameMap tmpMap = null;
        try {
            tmpMap = GameMap.createFromId(mapId);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Missing map files: " + e.toString(), e);
        }
        mMap = tmpMap;
        
        dDebugMode = false;
        mManager = gameManager;
        
        mInputQueue = new LinkedList<>();
        mOutputQueue = new LinkedList<>();
        
        mPlayers = new ArrayList<>();
        List<Role> roles = RoleBuilder.generateRoles(gameManager.getNumberOfClients(), true);
        
        for(int i = 0;i<gameManager.getNumberOfClients(); i++) {
            Role role = roles.get(i);
            boolean isMyTurn = (i == mTurnId);
            GamePlayer player = new GamePlayer(i, role, getMap().getStartingPoint(role instanceof Human));
            mPlayers.add(player);
            if(isMyTurn)
                player.setCurrentState(new StartTurnState(this));
            else
                player.setCurrentState(new NotMyTurnState(this));
        }
        
        if(mMap == null)
            gameManager.shutdown();
    }
    
    /** DEBUG MODE CONSTRUCTOR
     * Constructs a new game.
     */
    public GameState(String areYouSureToEnableDebugMode, int mapId, int numberOfPlayers, int startPlayerId, boolean randomizePlayers) {
        GameMap tmpMap = null;
        try {
            tmpMap = GameMap.createFromId(mapId);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Missing map files: " + e.toString(), e);
        }
        
        if(!areYouSureToEnableDebugMode.equalsIgnoreCase("YES"))
            throw new DebugException("Cannot enable debug mode.");
        
        mMap = tmpMap;
        
        dDebugMode = true;
        mManager = null;
        
        mInputQueue = new LinkedList<>();
        mOutputQueue = new LinkedList<>();
        
        mPlayers = new ArrayList<>();
        
        mTurnId = startPlayerId;
        List<Role> roles = RoleBuilder.generateRoles(numberOfPlayers, randomizePlayers);
        
        for(int i = 0;i<numberOfPlayers; i++) {
            Role role = roles.get(i);
            boolean isMyTurn = (i == mTurnId);
            GamePlayer player = new GamePlayer(i, role, getMap().getStartingPoint(role instanceof Human));
            mPlayers.add(player);
            if(isMyTurn)
                player.setCurrentState(new StartTurnState(this));
            else
                player.setCurrentState(new NotMyTurnState(this));
        }
        
        if(mMap == null)
            throw new DebugException("Invalid map file");
    }
    
    /**
     * Method called by the server hosting the games. According to the current player's state, it lets the game flow.
     */
    public void update() {
        if( !dDebugMode && !mManager.isRunning() )
            return;
        
        if(dDebugMode && dGameOver == true)
            throw new DebugException("Game over");
        
        GamePlayer player = getCurrentPlayer();
        PlayerState nextState = null;
        
        try {
            nextState = player.getCurrentState().update();
            player.setCurrentState(nextState);
        } catch( IllegalStateOperationException e) {
            LOG.log(Level.INFO, e.toString());
        }

        // broadcast messages at the end of the turn
        flushOutputQueue();
        
        if(nextState != null && (nextState instanceof NotMyTurnState || !nextState.stillInGame()))
            moveToNextPlayer();
    }
    
    /** Flush the output queue */
    public void clearOutputQueue() {
        if(dDebugMode)
            return;
        
        mOutputQueue.clear();
    }
    
    /** Flush the output queue */
    public void flushOutputQueue() {
        if(dDebugMode)
            return;
        
        if( !mOutputQueue.isEmpty() ) {
            
            for( Map.Entry<Integer, GameCommand> pkt : mOutputQueue )
                if(pkt.getKey().equals(-1))
                    mManager.broadcastPacket(pkt.getValue());
                else
                    mManager.sendDirectPacket(pkt.getKey(), pkt.getValue());
            
            mOutputQueue.clear();
        }
    }

    /**
     * Method invoked when a player draws an object card.  
     * @return Next PlayerState for current player
     */
    public PlayerState getObjectCard( ) {
        GamePlayer player = getCurrentPlayer();
        ObjectCard newCard = ObjectCardBuilder.getRandomCard( this );
        PlayerState nextState;

        player.addObjectCard(newCard);
        
        broadcastPacket( new GameCommand(InfoOpcode.INFO_GOT_A_NEW_OBJ_CARD, player.getNumberOfCards()));
        sendPacketToCurrentPlayer( new GameCommand(GameOpcode.CMD_SC_OBJECT_CARD_OBTAINED, (Integer)newCard.getId()) );
        
        // We're ok, proceed
        if( player.getNumberOfCards() <= Config.MAX_NUMBER_OF_OBJ_CARDS ) {
            nextState = new EndingTurnState(this);
        } else {
            // tell the user he has to drop or use a card
            nextState = new DiscardingObjectCardState(this);
        }
        
        return nextState;
    }
    
    /** Method invoked when someone sends a CMD_CS_USE_OBJ_CARD command. Invoke the correct underlying method 
     * (attack() for Attack card..., moveTo() for Teleport card...)
     * @param objectCard The card the user wants to use
     * @return Next PlayerState for current player
     */
    public PlayerState startUsingObjectCard(Integer objectCardPos) {
        GamePlayer player = getCurrentPlayer();
        PlayerState nextState = player.getCurrentState();
        
        if(player.isHuman() && !player.isObjectCardUsed() && player.getNumberOfUsableCards() > objectCardPos && objectCardPos >= 0) {   
            ObjectCard objectCard = player.useObjectCard(objectCardPos);
            if(objectCard != null) {
                broadcastPacket( new GameCommand(InfoOpcode.INFO_OBJ_CARD_USED, objectCard.getId(), objectCard.getName()) );
            
                getCurrentPlayer().setObjectCardUsed(true);
                nextState = objectCard.doAction();
                return nextState;
            }
        } 
        
        sendPacketToCurrentPlayer( GameOpcode.CMD_SC_CANNOT_USE_OBJ_CARD ); 
        
        return nextState;
    }
    
    /** Kills all players in a position. It is used when an alien sends an attack command or 
     * when a human player draws an Attack object card. 
     * @param currentPosition The point where the players wants to attack
     */
    public void attack(Point currentPosition) {
        ArrayList<Integer> killedPlayers = new ArrayList<>();
        ArrayList<Integer> defendedPlayers = new ArrayList<>();
        
        for(int i = 0; i < mPlayers.size(); i++) {
            GamePlayer player = mPlayers.get(i);
            if( player != getCurrentPlayer() && player.getCurrentPosition().equals(currentPosition) && player.stillInGame()) {
                if( player.isDefenseEnabled() ) {
                    player.dropDefense();
                    defendedPlayers.add(i);
                } else {
                    if(player.isHuman())
                        setLastThingDid(LastThings.KILLED_HUMAN);
                    player.setCurrentState( new LoserState(this, i) );        
                    killedPlayers.add(i); 
                }
            }
        }

        // set the player as full if he has an alien role
        if( !killedPlayers.isEmpty() ) {
            GamePlayer player = getCurrentPlayer();
            if( player.isAlien() ) {
                player.setFull(true);
                // TODO how about a notification here?
            }
        }
        
        broadcastPacket( new GameCommand(InfoOpcode.INFO_PLAYER_ATTACKED, currentPosition, killedPlayers, defendedPlayers) );
        
        checkEndGame();
    }
    
    /** End game conditions: [<who win?>]
     * 
     * (1) [ALIENS] If aliens eliminate last living human
     * (2) [ALIENS] No spaceships left && some humans are still in game
     * (3) [ALIENS] 39 rounds have been played before any human escape.
     * (4) [?ALIVE] Players connected <= 2
     * 
     * @param justKilledHumans True if this function is called after an attack and there are killed humans
     */
    private void checkEndGame() {
        boolean allWinnersMode = false; // set to true if inGamePlayers < MIN PLAYERS
        int aliveHumans = 0;
        int inGamePlayers = 0;
        int remainingHatches = mMap.getRemainingHatches();
        
        for(GamePlayer p : mPlayers)
            if(p.stillInGame()) {
                inGamePlayers++;
                if(p.isHuman())
                    aliveHumans++;
            }
        
        if(inGamePlayers < Config.GAME_MIN_PLAYERS && (mLastThing == LastThings.GONE_OFFLINE || mLastThing == LastThings.NEVERMIND))
            allWinnersMode = true;
        
        if((aliveHumans == 0 && mLastThing == LastThings.KILLED_HUMAN)   ||     // (1)
           (aliveHumans > 0 && remainingHatches == 0)                    ||     // (2)
           (mRoundsPlayed > Config.MAX_NUMBER_OF_TURNS)                  ||     // (3)
           (inGamePlayers < Config.GAME_MIN_PLAYERS)) {                         // (4)
            
            // So, the game will end.
            // Let's gather some stats
            
            // Move all players either into WinnerState or LoserState 
            for(int i = 0; i < mPlayers.size(); i++) {
                GamePlayer p = mPlayers.get(i);
                if((p.stillInGame() && ((p.isAlien() && mLastThing != LastThings.HUMAN_USED_HATCH)|| allWinnersMode)))
                    p.setCurrentState(new WinnerState(this, i));
                else if(!(p.getCurrentState() instanceof WinnerState))
                    p.setCurrentState(new LoserState(this, i));
            }
            
            clearOutputQueue();
            
            // Fill this two arrays
            ArrayList<Integer> winnersList = new ArrayList<>();
            ArrayList<Integer> loserList = new ArrayList<>();
            
            for(int i = 0; i < mPlayers.size(); i++) {
                GamePlayer p = mPlayers.get(i);
                PlayerState s = p.getCurrentState();
                if(s instanceof WinnerState)
                    winnersList.add(i);
                else if(s instanceof LoserState)
                    loserList.add(i);
                else
                    throw new RuntimeException("There are players who are neither winner nor loser. What's happening?");
            }
            
            broadcastPacket( new GameCommand(InfoOpcode.INFO_END_GAME, winnersList, loserList));
            flushOutputQueue();
            
            if(!dDebugMode)
                mManager.shutdown();
            else
                dGameOver = true;
        }
    }

    /**
     * Moves current player in a position. It is used by the Teleport card and when moving during
     * the normal flow of the game.
     * @param dest Where to move 
     */
    public void rawMoveTo(GamePlayer player, Point dest) {
        if( getMap().isWithinBounds(dest) && !player.getCurrentPosition().equals(dest) ) {
            player.setCurrentPosition(dest);
        }
    }
    
    /** Invoked in SpotLightCardState. It lists all people in the set position and in the 6 surrounding it.
     * @param point The position where the card takes effect.
     */
    public void spotlightAction(Point point) {
        ArrayList<Point> sectors = getMap().getNeighbourAccessibleSectors(point, getCurrentPlayer().isHuman());
        sectors.add(point);
        
        Point[] caughtPlayers = new Point[mPlayers.size()];
        for(int i = 0; i < mPlayers.size(); i++) {
            GamePlayer player = mPlayers.get(i);
            caughtPlayers[i] = null;
            if(sectors.contains( player.getCurrentPosition()))
                caughtPlayers[i] = player.getCurrentPosition();
            
        }
        
        broadcastPacket( new GameCommand( InfoOpcode.INFO_SPOTLIGHT, (Serializable[]) caughtPlayers) );
    }
    
    /* -----------------------------------------------*/
    
    /** Read a packet from the input queue
     * 
     * @return The read packet
     */
    public GameCommand getPacketFromQueue( ) {
        synchronized(mInputQueue) {
            return mInputQueue.poll();
        }
    }
    
    /** Get the current player
     * 
     * @return The current player
     */
    public synchronized GamePlayer getCurrentPlayer() {
        if(mTurnId == -1)
            throw new RuntimeException("CurrentPlayer == -1. What's happening?");
        return mPlayers.get( mTurnId ); 
    }

    /** Get the game map
     * 
     * @return The game map
     */
    public GameMap getMap() {
        return mMap;
    }
    
    /** Enqueue a command
     * 
     * @param gameCommand The command
     */
    public void enqueuePacket(GameCommand gameCommand) {
        synchronized(mInputQueue) {
            mInputQueue.add(gameCommand);
        }
    }
    
    /** Build the info container used to send info to the client
     * 
     * @param userList Username list
     * @param i Player id (to send this packet)
     * @return Game infos
     */
    public GameStartInfo buildInfoContainer(EnemyInfo[] userList, int i) {
        GameStartInfo info = new GameStartInfo(userList, i, mPlayers.get(i).isHuman(), mMap);
        return info;
    }
    
    /** Get the cells the current player can go into
     * 
     * @return The cells
     */
    public Set<Point> getCellsWithMaxDistance() {
        GamePlayer player = getCurrentPlayer();
        return getMap().getCellsWithMaxDistance( 
                player.getCurrentPosition(), 
                player.getMaxMoves(), player.isHuman()
        );
    }
    
    /** Switch to the next player */
    public void moveToNextPlayer() {
        int newTurnId = -1;
        int lastTurnId = mTurnId;
        
        if(dDebugMode && dForceNextTurn != -1) {
            checkEndGame();
            
            mTurnId = dForceNextTurn;
            dForceNextTurn = -1;
        } else {
            for( int currId = mTurnId + 1; currId < mTurnId + mPlayers.size(); ++currId ) {
                if( mPlayers.get(currId % mPlayers.size()).getCurrentState() instanceof NotMyTurnState ) {
                    newTurnId = currId % mPlayers.size();
                    break;
                }
            }
            
            if(newTurnId <= lastTurnId)
                mRoundsPlayed ++;
            
            checkEndGame();
            if(newTurnId == -1)
                return;
            
            mTurnId = newTurnId;
        }
        
        getCurrentPlayer().setCurrentState( new StartTurnState( this ) );
    }
    
    /** Broadcast a packet
     * 
     * @param pkt The packet
     */
    public void broadcastPacket( GameCommand pkt ) {
        synchronized(mOutputQueue) {
            mOutputQueue.add( new AbstractMap.SimpleEntry<Integer,GameCommand>(-1,pkt) );
        }
    }
    
    /** Broadcast a packet
     * 
     * @param command Packet opcode
     */
    public void broadcastPacket( InfoOpcode command ) {
        broadcastPacket( new GameCommand( command ) );
    }

    /** Send a packet to the current player
     * 
     * @param command The packet command
     */
    public void sendPacketToCurrentPlayer(GameOpcode command) {
        sendPacketToCurrentPlayer(new GameCommand(command));
    }

    /** Send a packet to the current player
     * 
     * @param command The packet command
     */
    public void sendPacketToCurrentPlayer(GameCommand networkPacket) {
        synchronized(mOutputQueue) {
            mOutputQueue.add( new AbstractMap.SimpleEntry<Integer,GameCommand>(mTurnId, networkPacket));
        }
    }
    
    /** Get the number of player in this sector
     * 
     * @param p Sector
     * @return Number of players
     */
    public int getNumberOfPlayersInSector( Point p ) {
        int counter = 0;
        
        for( GamePlayer player : mPlayers )
            if( player.stillInGame() && player.getCurrentPosition().equals(p) )
                counter++;
        
        return counter;
    }

    /** Get the current turn id
     * 
     * @return The current turn
     */
    public int getTurnId() {
        return mTurnId;
    }

    /** Called when a client disconnects
     * @param id
     */
    public void onPlayerDisconnect(int id) {
        GamePlayer player = mPlayers.get(id);
        if(player != null) {
            if(mTurnId == id) 
                moveToNextPlayer();
            player.setCurrentState(new AwayState(this));
            setLastThingDid(LastThings.GONE_OFFLINE);
        }
        
        checkEndGame();
    }

    /** Send a packet to the specified player
     * @param player The player
     * @param pkt The packet
     */
    public void sendPacketToPlayer(Integer player, GameCommand pkt) {
        synchronized(mOutputQueue) {
            mOutputQueue.add( new AbstractMap.SimpleEntry<Integer,GameCommand>(player, pkt));
        }
    }

    public synchronized void setLastThingDid(LastThings ltd) {
        mLastThing = ltd;
    }
    
    /** ====== DEBUG ====== */
    
    
    /** [DEBUG] Get the output queue
     *
     * @return The output packet queue
     */
    public Queue<Map.Entry<Integer,GameCommand>> debugGetOutputQueue() {
        if(!dDebugMode)
            throw new DebugException("Cannot use this method in normal mode");
        
        Queue<Map.Entry<Integer,GameCommand>> q = new LinkedList<>();
        
        for( Map.Entry<Integer, GameCommand> pkt : mOutputQueue )
            q.add(pkt);
        
        mOutputQueue.clear();
        
        return q;
    }
    
    /** Change next player
     * 
     * @param id Who's the next?
     */
    public void debugSetNextTurnId(int id) {
        if(!dDebugMode)
            throw new DebugException("Cannot use this method in normal mode");
        dForceNextTurn = id;
    }

    /** Get the player object. Useful to add card or change values
     * 
     * @param playerId The player id [0-7]
     * @return The player object
     */
    public GamePlayer debugGetPlayer(int playerId) {
        if(!dDebugMode)
            throw new DebugException("Cannot use this method in normal mode");
        
        return mPlayers.get(playerId);
    }
    
    public boolean debugGameEnded() {
        if(!dDebugMode)
            throw new DebugException("Cannot use this method in normal mode");
        
        return this.dGameOver;
    }
    
    
}
