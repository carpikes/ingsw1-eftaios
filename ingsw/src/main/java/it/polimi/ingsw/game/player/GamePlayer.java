package it.polimi.ingsw.game.player;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.card.object.DefenseCard;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.state.PlayerState;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an in-game player. It contains all the cards, the role, the current state...
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
    private List < ObjectCard > mObjectCards;
    private List < ObjectCard > mUsableObjectCards;

    /** Alien or human? */
    private final Role role;

    /** Current state in game for the player */
    private PlayerState currentState;

    /** State before using SpotlightCard */
    private PlayerState stateBeforeSpotlightCard;

    /** ID in game */
    private int mId;
    
    /**
     * Create a new player for the game
     * @param id ID in game
     * @param playerRole Role of the player (alien or human)
     * @param game The game he's playing
     * @param isMyTurn Is he the first player?
     */
    public GamePlayer( int id, Role playerRole, Point startPosition ) {
        resetValues();
        mObjectCards = new ArrayList<>();
        mUsableObjectCards = new ArrayList<>();
        role = playerRole;

        mPosition = startPosition; 
        mId = id; 
    }

    /**
     * Get current player state
     * @return Player state
     */
    public PlayerState getCurrentState() {
        return currentState;
    }

    /**
     * Set current player state
     * @param currentState The state
     */
    public void setCurrentState(PlayerState currentState) {
        this.currentState = currentState;
    }

    /**
     * Check if defense is enabled (if you have a defense card)
     * @return True if defense enabled
     */
    public boolean isDefenseEnabled() {
        return defense;
    }

    /**
     * Set defense to enabled / disabled
     * @param defense The value to use
     */
    public void setDefense(boolean defense) {
        // only humans can activate a defense system!
        if( this.isHuman() )
            this.defense = defense;
        else
            this.defense = false;
    }

    /**
     * Get current position on board
     * @return The position
     */
    public Point getCurrentPosition() {
        return mPosition;
    }

    /**
     * Calculate if the given distance is valid based on getMaxMoves() value
     * @param distance The number of sectors you have to cross
     * @return True if it is valid
     */
    public boolean isValidDistance(int distance) {
        return (distance > 0 && distance <= this.getMaxMoves());
    }

    /**
     * Set current position to the specified one
     * @param position The position where to move the player
     */
    public void setCurrentPosition(Point position) {
        this.mPosition = position;
    }

    /**
     * Get role of the player (alien or human)
     * @return The role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Check if player is an alien
     * @return True if alien
     */
    public boolean isAlien() {
        return role instanceof Alien;
    }

    /**
     * Check if player is a human
     * @return True if human
     */
    public boolean isHuman() {
        return role instanceof Human;
    }
    
    /**
     * Check if the player has already used an object card during this turn
     * @return True if he has already used one
     */
    public boolean isObjectCardUsed() {
        return objectCardUsed;
    }

    /** 
     * Set the value of objectCardUsed to the value specified by the parameter
     * @param objectCardUsed The value to use
     */
    public void setObjectCardUsed(boolean objectCardUsed) {
        this.objectCardUsed = objectCardUsed;
    }

    /**
     * Get ID for this player
     * @return The ID
     */
    public int getId() {
        return mId;
    }

    /**
     * Check if this player has to draw a dangerous card during this turn
     * @return True if he has to draw a dangerous card
     */
    public boolean shouldDrawDangerousCard() {
        return drawDangerousCard;
    }

    /** 
     * Set the value of shouldDrawDangerousCard.
     * @param shouldDrawDangerousCard The value to use
     */
    public void setShouldDrawDangerousCard(boolean shouldDrawDangerousCard) {
        this.drawDangerousCard = shouldDrawDangerousCard;
    }

    /** 
     * Get the player state before using Spotlight Card during this turn. Always null
     * unless he has used a Spotlight Card. 
     * @return The state beforce using a spotlight card
     */
    public PlayerState getStateBeforeSpotlightCard() {
        return stateBeforeSpotlightCard;
    }

    /** 
     * Set the state before using Spotlight Card
     * @param stateBefore The state before using Spotlight Card
     */
    public void setStateBeforeSpotlightCard(PlayerState stateBefore) {
        this.stateBeforeSpotlightCard = stateBefore;
    }

    /**
     * Reset values at the beginning of each turn.
     */
    public void resetValues() {
        defense = hasDefenseCard();
        objectCardUsed = false;
        drawDangerousCard = true;
        stateBeforeSpotlightCard = null;
        setAdrenaline(false);
    }

    /**
     * Check if the player has a defense card in his own deck
     * @return True if has at least one defense card.
     */
    private boolean hasDefenseCard() {
        if(mObjectCards == null)
            return false;
        for( ObjectCard card : mObjectCards ) 
            if( card instanceof DefenseCard )
                return true;

        return false;
    }

    /**
     * Get how many sectors he can cross based on the role specified.
     * @return The maximum number of moves
     */
    public int getMaxMoves() {
        return role.getMaxMoves();
    }

    /** 
     * Check if this player has eaten during this turn. Always returns false for a human
     * @return True if he has eaten
     */
    public boolean isFull() {
        if( role instanceof Alien )
            return ((Alien) role).hasEaten();
        else
            return false;
    }

    /**
     * Set the player to full or not. Ignored when used on a human.
     * @param isFull
     */
    public void setFull(boolean isFull) {
        if( role instanceof Alien )
            ((Alien) role).setHasEaten(isFull);
    }

    /**
     * Set adrenaline to used or not. Only valid on humans, ignored on aliens.
     * @param adrenaline The value to use
     */
    public void setAdrenaline(boolean adrenaline) {
        if( role instanceof Human ) 
            ((Human) role).setAdrenaline(adrenaline);
    }

    /**
     * Used when someone attacks this player but the defense is enabled.
     */
    public void dropDefense() {        
        for( int i = 0; i < mObjectCards.size(); ++i ) {
            if( mObjectCards.get(i) instanceof DefenseCard ) {
                mObjectCards.remove(i);
                break;
            }
        } 

        // there could be more than one defense card
        setDefense( this.hasDefenseCard() );
    }

    /**
     * Check if this player is still in game.
     * @return True if still playing (not in winner, loser, away state)
     */
    public boolean stillInGame() {
        return currentState.stillInGame();
    }
    
    
    /** ===== OBJECT CARDS ===== */

    /**
     * @param objectCardPos
     * @return
     */
    public ObjectCard useObjectCard(int index) {
        ObjectCard c = mUsableObjectCards.get(index);
        if( c == null || !mObjectCards.remove(c) || !mUsableObjectCards.remove(c))
            throw new InvalidCardException("Invalid card. What's happening?");
        
        return c;
    }

    /**
     * Add an object cards for this player
     * @return The deck of object cards
     */
    public void addObjectCard(ObjectCard c) {
        mObjectCards.add(c);
        if(c.isUsable()) {
            mUsableObjectCards.add(c);
        }
    }
    
    /**
     * Get how many cards the player is holding at the moment
     * @return The number of cards
     */
    public int getNumberOfCards() {
        return mObjectCards.size();
    }
    
    /**
     * Get how many *usable* cards the player is holding at the moment
     * @return The number of cards
     */
    public int getNumberOfUsableCards() {
        return mUsableObjectCards.size();
    }
    
    /**
     * @param objectCard
     */
    public void removeObjectCard(ObjectCard objectCard) {
        mObjectCards.remove(objectCard);
        mUsableObjectCards.remove(objectCard);
    }
    
    public String[] getNamesOfUsableCards() {
        return cardsToString(mUsableObjectCards);
    }
    
    public String[] getNamesOfCards() {
        return cardsToString(mObjectCards);
    }
    
    /**
     * @param index
     */
    public void removeObjectCard(int index) {
        ObjectCard c = mObjectCards.remove(index);
        mUsableObjectCards.remove(c);
    }
    
    private String[] cardsToString(List<ObjectCard> cards) {
        String[] cardNames = new String[cards.size()];
        
        for(int i = 0; i<cards.size(); i++) {
            ObjectCard card = cards.get(i);
            cardNames[i] = card.getName();
        }
        
        return cardNames;
    }
    
    public boolean getAdrenaline() {
        if(role instanceof Human)
            return ((Human)role).hasUsedAdrenaline();

        return false;
    }
}
