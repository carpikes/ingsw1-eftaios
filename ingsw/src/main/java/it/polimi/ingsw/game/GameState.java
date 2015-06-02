package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameInfoContainer;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.Role;
import it.polimi.ingsw.game.player.RoleFactory;
import it.polimi.ingsw.game.state.DiscardingObjectCardState;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.LoserState;
import it.polimi.ingsw.game.state.NotMyTurnState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.StartTurnState;
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
    
    private final Queue<NetworkPacket> mInputQueue;
    private final Queue<Map.Entry<Integer,NetworkPacket>> mOutputQueue;
    
    private final GameMap mMap;
    private ArrayList<GamePlayer> mPlayers;
    private int mTurnId = 0;

    /**
     * Constructs a new game.
     * @param gameManager The GameManager that created this game.
     * @param mapId Id of the map
     * @param clients List of connections of all players
     */
    public GameState(GameManager gameManager, int mapId) {
        GameMap tmpMap;
        try {
            tmpMap = GameMap.createFromId(mapId);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Missing map files: " + e.toString(), e);
            tmpMap = GameMap.generate();
        }
        mMap = tmpMap;
        
        mGameManager = gameManager;
        
        mInputQueue = new LinkedList<>();
        mOutputQueue = new LinkedList<>();
        
        mPlayers = new ArrayList<>();
        List<Role> roles = RoleFactory.generateRoles(gameManager.getNumberOfPlayers());
        
        for(int i = 0;i<gameManager.getNumberOfPlayers(); i++) {
            Role role = roles.get(i);
            GamePlayer player = new GamePlayer(i, role, this, (i == mTurnId));
            mPlayers.add(player);
        }
    }
    
    /**
     * Method called by the server hosting the games. According to the current player's state, it lets the game flow.
     */
    public void update() {
        if(!mGameManager.isRunning())
            return;
        
        GamePlayer player = getCurrentPlayer();
                
        try {
            PlayerState nextState = player.getCurrentState().update();
            player.setCurrentState(nextState);
        } catch( IllegalStateOperationException e) {
            LOG.log(Level.INFO, e.toString(), e);
        }

        // broadcast messages at the end of the turn
        flushOutputQueue();
    }
    
    
    private void flushOutputQueue() {
		if( !mOutputQueue.isEmpty() ) {
			
			for( Map.Entry<Integer, NetworkPacket> pkt : mOutputQueue )
			    if(pkt.getKey() == -1)
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
        
        // Send card just obtained
        player.getObjectCards().add( newCard );
        sendPacketToCurrentPlayer( new NetworkPacket(GameCommand.CMD_SC_OBJECT_CARD_OBTAINED, newCard) );
        
        // We're ok, proceed
        if( player.getNumberOfCards() < Config.MAX_NUMBER_OF_OBJ_CARDS ) {
            broadcastPacket( GameCommand.INFO_GOT_A_NEW_OBJ_CARD );
            nextState = new EndingTurnState(this, getCurrentPlayer());
        } else {
            // tell the user he has to drop or use a card
            sendPacketToCurrentPlayer( GameCommand.CMD_SC_DISCARD_OBJECT_CARD );
            nextState = new DiscardingObjectCardState(this, getCurrentPlayer());
        }
        
        return nextState;
    }
    
	/** Method invoked when someone sends a CMD_CS_USE_OBJ_CARD command. Invoke the correct underlying method 
     * (attack() for Attack card..., moveTo() for Teleport card...)
     * @param objectCard The card the user wants to use
     * @return Next PlayerState for current player
     */
    public PlayerState startUsingObjectCard(ObjectCard objectCard) {
        GamePlayer player = getCurrentPlayer();
        PlayerState nextState = player.getCurrentState();
        
        if( player.isHuman() && !player.isObjectCardUsed() ) {        	
        	this.broadcastPacket( new NetworkPacket(GameCommand.INFO_OBJ_CARD_USED, objectCard.toString()) );
        	
            getCurrentPlayer().setObjectCardUsed(true);
            nextState = objectCard.doAction();
        } else {
            sendPacketToCurrentPlayer( GameCommand.CMD_SC_CANNOT_USE_OBJ_CARD ); 
        }
        
        return nextState;
    }
    
    /** Kills all players in a position. It is used when an alien sends an attack command or 
     * when a human player draws an Attack object card. 
     * @param currentPosition The point where the players wants to attack
     */
    public void attack(Point currentPosition) {
    	ArrayList<Integer> killedPlayers = new ArrayList<>();
    	
    	for(int i = 0; i < mPlayers.size(); i++) {
    	    GamePlayer player = mPlayers.get(i);
    		if( player != getCurrentPlayer() && player.getCurrentPosition().equals(currentPosition) ) {
    			if( player.isDefenseEnabled() ) {
    			    // TODO: VUOI AVVISARE in modo speciale il giocatore?
    			    player.dropDefense();
    			} else {
                    player.setCurrentState( new LoserState(this, player) );        
                    killedPlayers.add( i ); 
    			}
    		}
    	}

    	// set the player as full if he has an alien role
    	if( !killedPlayers.isEmpty() ) {
    		GamePlayer player = getCurrentPlayer();
    		if( player.isAlien() ) {
    			player.setFull(true);
    		}
    	}
    	
    	broadcastPacket( new NetworkPacket(GameCommand.INFO_KILLED_PEOPLE, killedPlayers) );
    }
    
    /**
     * Moves current player in a position. It is used by the Teleport card and when moving during
     * the normal flow of the game.
     * @param src Where to move 
     * @param dest TODO
     */
    public void rawMoveTo(Point src, Point dest) {
        if( getMap().isWithinBounds(src) && !src.equals(dest) ) {
        	getCurrentPlayer().setCurrentPosition(dest);
        }
    }
    
    /** Invoked in SpotLightCardState. It lists all people in the set position and in the 6 surrounding it.
     * @param point The position where the card takes effect.
     */
    public void spotlightAction(Point point) {
    	ArrayList<Point> sectors = getMap().getNeighbourAccessibleSectors(point);
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
        
        broadcastPacket( new NetworkPacket( GameCommand.INFO_SPOTLIGHT, caughtPlayers, playerPositions ) );
    }
    /* -----------------------------------------------*/
    
    public NetworkPacket getPacketFromQueue( ) {
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
    
    public void queuePacket(NetworkPacket pkt) {
        synchronized(mInputQueue) {
            mInputQueue.add(pkt);
        }
    }
    
    public GameInfoContainer buildInfoContainer(EnemyInfo[] userList, int i) {
        GameInfoContainer info = new GameInfoContainer(userList, mPlayers.get(i).isHuman(), mMap);
        return info;
    }

    public List<GamePlayer> getPlayers() {
        return mPlayers;
    }

    public GameManager getGameManager() {
        return mGameManager;
    }
    
    public Set<Point> getCellsWithMaxDistance() {
        return getMap().getCellsWithMaxDistance( 
                getCurrentPlayer().getCurrentPosition(), 
                getCurrentPlayer().getMaxMoves()
        );
    }

    /**
     * Invoked when someone exceeds the MAX_NUMBER_OF_TURNS limit in a game
     * or there are no more players in game.
     * Kills all humans and make aliens winners.
     */
	public void endGame() {
		this.broadcastPacket( GameCommand.INFO_END_GAME );
	}
	
	public boolean areTherePeopleStillPlaying() {
		int counter = 0;
		
		for( GamePlayer p : mPlayers )
			if( p.getCurrentState().stillInGame() )
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
			endGame();
		}
	}

	private int findNextPlayer() {
		for( int currId = mTurnId; currId < mTurnId + mPlayers.size(); ++mTurnId ) {
			if( mPlayers.get(currId % mPlayers.size()).getCurrentState() instanceof NotMyTurnState )
				return currId;
		}

		throw new RuntimeException("No players. What's happening?");
	}
	
	public void broadcastPacket( NetworkPacket pkt ) {
		mOutputQueue.add( new AbstractMap.SimpleEntry<Integer,NetworkPacket>(-1,pkt) );
	}
	
	public void broadcastPacket( GameCommand command ) {
	    broadcastPacket( new NetworkPacket( command ) );
	}

    public void sendPacketToCurrentPlayer(GameCommand command) {
        sendPacketToCurrentPlayer(new NetworkPacket(command));
    }

    public void sendPacketToCurrentPlayer(NetworkPacket networkPacket) {
        mOutputQueue.add( new AbstractMap.SimpleEntry<Integer,NetworkPacket>(mTurnId, networkPacket));
    }

}
