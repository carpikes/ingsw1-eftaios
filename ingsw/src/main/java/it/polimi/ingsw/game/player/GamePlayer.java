package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.DefenseCard;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.state.NotMyTurnState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.StartTurnState;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 19, 2015
 */
public class GamePlayer {
    
    /** Position on board */
    private Point mPosition;
    
    /** Defense enabled? (defense card used or not) */
    private boolean defense;
    
    /** Object card already used in current turn */
    private boolean objectCardUsed;
    
    /** Sedatives card used? */
    private boolean drawDangerousCard;
    
    /** Object cards the player owns */
    private ArrayList < ObjectCard > objectCards;
    
    /** Alien or human? */
    private final Role role;
    
    /** Current state in game for the player */
    private PlayerState currentState;
    
    /** State before using SpotlightCard */
    private PlayerState stateBeforeSpotlightCard;
    
    /** ID in game */
    private int mId;
    
    /** Number of current move */
    private int moveCounter;

    /** The game manager */
    private GameState mGame;
    
    public int getMoveCounter() {
		return moveCounter;
	}

	public void incrementMoveCounter( ) {
		++moveCounter;
	}

	public GamePlayer( int id, Role playerRole, Point startPosition, GameState game, boolean isMyTurn) {
        resetValues();
    	objectCards = new ArrayList<>();
        role = playerRole;

        mPosition = startPosition; 
        mId = id; 
        moveCounter = 0;
        mGame = game;
        
        if(isMyTurn)
            setCurrentState(new StartTurnState(game, this));
        else
            setCurrentState(new NotMyTurnState(game, this));
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PlayerState currentState) {
        this.currentState = currentState;
    }
    
    public boolean isDefenseEnable() {
        return defense;
    }

    public void setDefense(boolean defense) {
        this.defense = defense;
    }

    /**
     * @return
     */
    public Point getCurrentPosition() {
        return mPosition;
    }

    /**
     * @param distance
     * @return
     */
    public boolean isValidDistance(int distance) {
        return (distance > 0 && distance <= this.getMaxMoves());
    }

    /**
     * @param destination
     */
    public void setCurrentPosition(Point position) {
        this.mPosition = position;
    }

    /**
     * @param p
     */
    public void notifyChange(GamePlayer p) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @return
     */
    public Role getRole() {
       return role;
    }

    /**
     * @return
     */
    public boolean isAlien() {
        return role instanceof Alien;
    }

    /**
     * @return
     */
    public boolean isHuman() {
        return role instanceof Human;
    }

    public ArrayList<ObjectCard> getObjectCards() {
        return objectCards;
    }
    
    public int getNumberOfCards() {
        return objectCards.size();
    }

    public boolean isObjectCardUsed() {
        return objectCardUsed;
    }

    public void setObjectCardUsed(boolean objectCardUsed) {
        this.objectCardUsed = objectCardUsed;
    }

    /**
     * @return
     */
    public int getId() {
        return mId;
    }
    
    // TODO: AGGIUNGERE IL CONTROLLO PRIMA DI OGNI PRELIEVO CARTA 
    public boolean shouldDrawDangerousCard() {
        return drawDangerousCard;
    }

    public void setShouldDrawDangerousCard(boolean shouldDrawDangerousCard) {
        this.drawDangerousCard = shouldDrawDangerousCard;
    }

    public PlayerState getStateBeforeSpotlightCard() {
        return stateBeforeSpotlightCard;
    }

    public void setStateBeforeSpotlightCard(PlayerState stateBefore) {
        this.stateBeforeSpotlightCard = stateBefore;
    }
    
    public void resetValues() {
    	defense = hasDefenseCard();
    	objectCardUsed = false;
    	drawDangerousCard = true;
    	stateBeforeSpotlightCard = null;
    	setAdrenaline(false);
    }

	private boolean hasDefenseCard() {
	    if(objectCards == null)
	        return false;
		for( ObjectCard card : objectCards ) 
			if( card instanceof DefenseCard )
				return true;
		
		return false;
	}
	
	public int getMaxMoves() {
		return role.getMaxMoves();
	}
    
	public boolean isFull() {
		if( role instanceof Alien )
			return ((Alien) role).hasEaten();
		else
			return false;
	}
	
	public void setFull(boolean isFull) {
		if( role instanceof Alien )
			((Alien) role).setHasEaten(isFull);
	}
	
	public void setAdrenaline(boolean adrenaline) {
		if( role instanceof Human) 
			((Human) role).setAdrenaline(adrenaline);
	}
    
}
