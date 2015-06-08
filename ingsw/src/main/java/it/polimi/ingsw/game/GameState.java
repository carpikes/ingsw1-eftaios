package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.player.GamePlayer;
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
    
    private final GameManager mGameManager;
    
    private final Queue<GameCommand> mInputQueue;
    private final Queue<Map.Entry<Integer,GameCommand>> mOutputQueue;
    
    private final GameMap mMap;
    private ArrayList<GamePlayer> mPlayers;
    private int mTurnId = 0;

    private int mNumberRoundsPlayed;

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
        
        mGameManager = gameManager;
        
        mInputQueue = new LinkedList<>();
        mOutputQueue = new LinkedList<>();
        
        mPlayers = new ArrayList<>();
        List<Role> roles = RoleBuilder.generateRoles(gameManager.getNumberOfClients());
        
        for(int i = 0;i<gameManager.getNumberOfClients(); i++) {
            Role role = roles.get(i);
            GamePlayer player = new GamePlayer(i, role, this, (i == mTurnId));
            mPlayers.add(player);
        }
        
        if(mMap == null)
            gameManager.shutdown();
    }
    
    /**
     * Method called by the server hosting the games. According to the current player's state, it lets the game flow.
     */
    public void update() {
        if(!mGameManager.isRunning())
            return;
        
        GamePlayer player = getCurrentPlayer();
        PlayerState nextState = null;
        
        try {
            nextState = player.getCurrentState().update();
            player.setCurrentState(nextState);
        } catch( IllegalStateOperationException e) {
            LOG.log(Level.INFO, e.toString(), e);
        }

        // broadcast messages at the end of the turn
        flushOutputQueue();
        
        if(nextState != null && (nextState instanceof NotMyTurnState || !nextState.stillInGame()))
            moveToNextPlayer();
    }
    
    
    public void flushOutputQueue() {
        if( !mOutputQueue.isEmpty() ) {
            
            for( Map.Entry<Integer, GameCommand> pkt : mOutputQueue )
                if(pkt.getKey().equals(-1))
                    mGameManager.broadcastPacket(pkt.getValue());
                else
                    mGameManager.sendDirectPacket(pkt.getKey(), pkt.getValue());
            
            mOutputQueue.clear();
        }
    }
    
    // TODO: refactoring of the following methods
    /* -----------------------------------------------*/
    
    /**
     * Method invoked when a player draws an object card.  
     * @return Next PlayerState for current player
     */
    public PlayerState getObjectCard( ) {
        GamePlayer player = getCurrentPlayer();
        ObjectCard newCard = ObjectCardBuilder.getRandomCard( this, getCurrentPlayer() );
        PlayerState nextState;
        
        // FIXME maybe an id is better here!
        player.addObjectCard(newCard);
        sendPacketToCurrentPlayer( new GameCommand(GameOpcode.CMD_SC_OBJECT_CARD_OBTAINED, newCard.getId(), newCard.getName()) );
        
        // We're ok, proceed
        if( player.getNumberOfCards() < Config.MAX_NUMBER_OF_OBJ_CARDS ) {
            broadcastPacket( GameOpcode.INFO_GOT_A_NEW_OBJ_CARD );
            nextState = new EndingTurnState(this, getCurrentPlayer());
        } else {
            // tell the user he has to drop or use a card
            sendPacketToCurrentPlayer( GameOpcode.CMD_SC_DISCARD_OBJECT_CARD );
            nextState = new DiscardingObjectCardState(this, getCurrentPlayer());
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
                broadcastPacket( new GameCommand(GameOpcode.INFO_OBJ_CARD_USED, objectCard.getId(), objectCard.getName()) );
            
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
        boolean killedHumans = false;
        for(int i = 0; i < mPlayers.size(); i++) {
            GamePlayer player = mPlayers.get(i);
            if( player != getCurrentPlayer() && player.getCurrentPosition().equals(currentPosition) && player.stillInGame()) {
                if( player.isDefenseEnabled() ) {
                    player.dropDefense();
                    defendedPlayers.add(i);
                } else {
                    if(player.isHuman())
                        killedHumans = true;
                    player.setCurrentState( new LoserState(this, player) );        
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
        
        broadcastPacket( new GameCommand(GameOpcode.INFO_PLAYER_ATTACKED, currentPosition, killedPlayers, defendedPlayers) );
        
        checkEndGame(killedHumans);
    }
    
    /** End game conditions: [<end|cont>/<who win?>]
     * 
     * (1) [END/ALIENS] If aliens eliminate last living human
     * (2) [END/ALIENS] No spaceships left && some humans are still in game
     * (3) [END/ALIENS] 39 rounds have been played before any human escape.
     * (4) [CONT/HUMAN] Every human that leaves the spaceship
     * (5) [END/ALIENS] Players connected <= 2
     * 
     * @param justKilledHumans True if this function is called after an attack and there are killed humans
     */
    private void checkEndGame(boolean justKilledHumans) {
        int aliveHumans = 0;
        int remainingHatches = mMap.getRemainingHatches();
        for(GamePlayer p : mPlayers)
            if(p.isHuman() && p.stillInGame())
                aliveHumans++;
        
        
        // (1) + (2) + (3)
        if((aliveHumans == 0 && justKilledHumans) || (aliveHumans > 0 && remainingHatches == 0) || (mNumberRoundsPlayed >= Config.MAX_NUMBER_OF_TURNS)) {
            
            for(int i = 0; i < mPlayers.size(); i++) {
                GamePlayer p = mPlayers.get(i);
                
                if((p.stillInGame() && p.isAlien()))
                    p.setCurrentState(new WinnerState(this, p));
                else if(p.getCurrentState() instanceof AwayState || (p.isHuman() && p.stillInGame()))
                    p.setCurrentState(new LoserState(this, p));
                else
                    throw new RuntimeException("Unknown player state. What's happening?");
            }
            
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
            
            broadcastPacket( new GameCommand(GameOpcode.INFO_END_GAME, winnersList, loserList));
            flushOutputQueue();
            mGameManager.shutdown();
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
        
        ArrayList<Integer> caughtPlayers = new ArrayList<>();
        ArrayList<Point> playerPositions = new ArrayList<>();
        
        for(int i = 0; i < mPlayers.size(); i++) {
            GamePlayer player = mPlayers.get(i);
            if( sectors.contains( player.getCurrentPosition() ) ) {
                caughtPlayers.add(i);
                playerPositions.add( player.getCurrentPosition() );
            }
        }
        
        broadcastPacket( new GameCommand( GameOpcode.INFO_SPOTLIGHT, caughtPlayers, playerPositions ) );
    }
    /* -----------------------------------------------*/
    
    public GameCommand getPacketFromQueue( ) {
        synchronized(mInputQueue) {
            return mInputQueue.poll();
        }
    }
    
    private synchronized GamePlayer getCurrentPlayer() {
        return mPlayers.get( mTurnId ); 
    }

    public GameMap getMap() {
        return mMap;
    }
    
    public void queuePacket(GameCommand gameCommand) {
        synchronized(mInputQueue) {
            mInputQueue.add(gameCommand);
        }
    }
    
    public GameStartInfo buildInfoContainer(EnemyInfo[] userList, int i) {
        GameStartInfo info = new GameStartInfo(userList, i, mPlayers.get(i).isHuman(), mMap);
        return info;
    }

    public List<GamePlayer> getPlayers() {
        return mPlayers;
    }

    public GameManager getGameManager() {
        return mGameManager;
    }
    
    public Set<Point> getCellsWithMaxDistance() {
        GamePlayer player = getCurrentPlayer();
        return getMap().getCellsWithMaxDistance( 
                player.getCurrentPosition(), 
                player.getMaxMoves(), player.isHuman()
        );
    }

    public boolean areTherePeopleStillPlaying() {
        int counter = 0;
        
        for( GamePlayer p : mPlayers )
            if( p.stillInGame() )
                ++counter;
        
        return counter >= Config.GAME_MIN_PLAYERS;
    }
    
    public void moveToNextPlayer() {
        if( areTherePeopleStillPlaying() ) {
            mTurnId = findNextPlayer();
            GamePlayer nextPlayer = mPlayers.get(mTurnId);
            nextPlayer.setCurrentState( new StartTurnState( this, nextPlayer) );
        } else {
            // just one or zero players left -> the game has ended
            checkEndGame(false);
        }
    }

    private int findNextPlayer() {
        for( int currId = mTurnId + 1; currId < mTurnId + mPlayers.size(); ++currId ) {
            if( mPlayers.get(currId % mPlayers.size()).getCurrentState() instanceof NotMyTurnState )
                return currId % mPlayers.size();
        }

        throw new RuntimeException("No players. What's happening?");
    }
    
    public void broadcastPacket( GameCommand pkt ) {
        synchronized(mOutputQueue) {
            mOutputQueue.add( new AbstractMap.SimpleEntry<Integer,GameCommand>(-1,pkt) );
        }
    }
    
    public void broadcastPacket( GameOpcode command ) {
        broadcastPacket( new GameCommand( command ) );
    }

    public void sendPacketToCurrentPlayer(GameOpcode command) {
        sendPacketToCurrentPlayer(new GameCommand(command));
    }

    public void sendPacketToCurrentPlayer(GameCommand networkPacket) {
        synchronized(mOutputQueue) {
            mOutputQueue.add( new AbstractMap.SimpleEntry<Integer,GameCommand>(mTurnId, networkPacket));
        }
    }
    
    public int getNumberOfPlayersInSector( Point p ) {
        int counter = 0;
        
        for( GamePlayer player : mPlayers )
            if( player.stillInGame() && player.getCurrentPosition().equals(p) )
                counter++;
        
        return counter;
    }

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
            player.setCurrentState(new AwayState(this, player));
        }
        
        if(!areTherePeopleStillPlaying()) {
            checkEndGame(false);
        }
    }

}
