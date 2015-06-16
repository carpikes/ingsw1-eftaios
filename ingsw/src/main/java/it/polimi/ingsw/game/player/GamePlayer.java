package it.polimi.ingsw.game.player;

import it.polimi.ingsw.exception.DefenseException;
import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.card.object.DefenseCard;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.state.PlayerState;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Class representing an in-game player. 
 * It contains all the cards, the role, the current state...
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 19, 2015
 */
public class GamePlayer {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(GamePlayer.class.getName());

    /** Position on board */
    private Point mPosition;

    /** Defense enabled? (defense card used or not) */
    private boolean mDefense;

    /** Object card already used in current turn */
    private boolean mObjectCardUsed;

    /** Sedatives card used? */
    private boolean mDrawDangerousCard;

    /** Object cards the player owns */
    private List < ObjectCard > mObjectCards;

    /** Usable object cards the player owns */
    private List < ObjectCard > mUsableObjectCards;

    /** Alien or human? */
    private final Role role;

    /** Current state in game for the player */
    private PlayerState mCurrentState;

    /** State before using SpotlightCard */
    private PlayerState mStateBeforeSpotlightCard;

    /** ID in game */
    private int mId;

    /** Create a new player for the game
     * 
     * @param id ID in game
     * @param playerRole Role of the player (alien or human)
     * @param startPosition Initial point
     */
    public GamePlayer( int id, Role playerRole, Point startPosition ) {
        resetValues();
        mObjectCards = new ArrayList<>();
        mUsableObjectCards = new ArrayList<>();
        role = playerRole;

        mPosition = startPosition; 
        mId = id; 
    }

    /** Get current player state
     *
     * @return Player state
     */
    public PlayerState getCurrentState() {
        return mCurrentState;
    }

    /** Set current player state
     *
     * @param currentState The state
     */
    public void setCurrentState(PlayerState currentState) {
        mCurrentState = currentState;
    }

    /** Check if defense is enabled (if you have a defense card)
     *
     * @return True if defense enabled
     */
    public boolean isDefenseEnabled() {
        return mDefense;
    }

    /** Set defense to enabled / disabled
     *
     * @param defense The value to use
     */
    public void setDefense(boolean defense) {
        /** only humans can activate a defense system! */
        if( isHuman() )
            mDefense = defense;
        else
            mDefense = false;
    }

    /** Get current position on board
     * @return The position
     */
    public Point getCurrentPosition() {
        return mPosition;
    }

    /** Calculate if the given distance is valid based on getMaxMoves() value
     *
     * @param distance The number of sectors you have to cross
     * @return True if it is valid
     */
    public boolean isValidDistance(int distance) {
        return distance > 0 && distance <= getMaxMoves();
    }

    /** Set current position to the specified one
     *
     * @param position The position where to move the player
     */
    public void setCurrentPosition(Point position) {
        mPosition = position;
    }

    /** Get role of the player (alien or human)
     *
     * @return The role
     */
    public Role getRole() {
        return role;
    }

    /** Check if player is an alien
     *
     * @return True if alien
     */
    public boolean isAlien() {
        return role instanceof Alien;
    }

    /** Check if player is a human
     *
     * @return True if human
     */
    public boolean isHuman() {
        return role instanceof Human;
    }

    /** Check if the player has already used an object card during this turn
     *
     * @return True if he has already used one
     */
    public boolean isObjectCardUsed() {
        return mObjectCardUsed;
    }

    /** Set the value of objectCardUsed to the value specified by the parameter
     *
     * @param objectCardUsed The value to use
     */
    public void setObjectCardUsed(boolean objectCardUsed) {
        mObjectCardUsed = objectCardUsed;
    }

    /** Get ID for this player
     *
     * @return The ID
     */
    public int getId() {
        return mId;
    }

    /** Check if this player has to draw a dangerous card during this turn
     *
     * @return True if he has to draw a dangerous card
     */
    public boolean shouldDrawDangerousCard() {
        return mDrawDangerousCard;
    }

    /** Set the value of shouldDrawDangerousCard.
     *
     * @param shouldDrawDangerousCard The value to use
     */
    public void setShouldDrawDangerousCard(boolean shouldDrawDangerousCard) {
        mDrawDangerousCard = shouldDrawDangerousCard;
    }

    /** Get the player state before using Spotlight Card during this turn. 
     * Always null unless he has used a Spotlight Card. 
     *
     * @return The state beforce using a spotlight card
     */
    public PlayerState getStateBeforeSpotlightCard() {
        return mStateBeforeSpotlightCard;
    }

    /** Set the state before using Spotlight Card
     *
     * @param stateBefore The state before using Spotlight Card
     */
    public void setStateBeforeSpotlightCard(PlayerState stateBefore) {
        mStateBeforeSpotlightCard = stateBefore;
    }

    /** Reset values at the beginning of each turn. */
    public void resetValues() {
        mDefense = hasDefenseCard();
        mObjectCardUsed = false;
        mDrawDangerousCard = true;
        mStateBeforeSpotlightCard = null;
        setAdrenaline(false);
    }

    /** Check if the player has a defense card in his own deck
     *
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

    /** Get how many sectors he can cross based on the role specified.
     *
     * @return The maximum number of moves
     */
    public int getMaxMoves() {
        return role.getMaxMoves();
    }

    /** Check if this player has eaten during this turn. 
     * Always returns false for a human
     *
     * @return True if he has eaten
     */
    public boolean isFull() {
        if( role instanceof Alien )
            return ((Alien) role).hasEaten();
        else
            return false;
    }

    /** Set the player to full or not. 
     * Ignored when used on a human.
     *
     * @param isFull Full status
     */
    public void setFull(boolean isFull) {
        if( role instanceof Alien )
            ((Alien) role).setHasEaten(isFull);
    }

    /** Set adrenaline to used or not. 
     * Only valid on humans, ignored on aliens.
     *
     * @param adrenaline The value to use
     */
    public void setAdrenaline(boolean adrenaline) {
        if( role instanceof Human ) 
            ((Human) role).setAdrenaline(adrenaline);
        else if(adrenaline)
            LOG.log(Level.SEVERE, "Used arenaline on an alien. What's Happening");
    }

    /** Used when someone attacks this player but the defense is enabled.
     *
     * @return index of the card dropped
     */
    public int dropDefense() {    
        int i = 0;
        int deckSize = mObjectCards.size();
        
        for( ; i < deckSize; ++i ) {
            if( mObjectCards.get(i) instanceof DefenseCard ) {
                mObjectCards.remove(i);
                break;  
            }
        } 
        
        if( i == deckSize ) 
            throw new DefenseException("Trying to remove a defense card, but none were found in your deck!");

        /** there could be more than one defense card */
        setDefense( hasDefenseCard() );
        
        return i;
    }

    /** Check if this player is still in game.
     *
     * @return True if still playing (not in winner, loser, away state)
     */
    public boolean stillInGame() {
        return mCurrentState.stillInGame();
    }


    /** ===== OBJECT CARDS ===== */

    /** Use an object card
     *
     * @param index The index
     * @return The object card
     */
    public ObjectCard useObjectCard(int index) {
        ObjectCard c = mUsableObjectCards.get(index);
        if( c == null || !mObjectCards.remove(c) || !mUsableObjectCards.remove(c))
            throw new InvalidCardException("Invalid card. What's happening?");

        return c;
    }
    
    /** Find the id of this card in the main array
     *
     * @param index Index of this card in the USABLE deck
     * @return Index of this card in the MAIN deck
     */
    public Integer findObjectCardId(int index) {
        ObjectCard c = mUsableObjectCards.get(index);
        int n = mObjectCards.indexOf(c);
        if(n == -1)
            return null;
        return n;
    }


    /** Add an object cards for this player
     *
     * @param c Object Card
     */
    public void addObjectCard(ObjectCard c) {
        mObjectCards.add(c);
        if(c.isUsable())
            mUsableObjectCards.add(c);
    }

    /** Get how many cards the player is holding at the moment
     *
     * @return The number of cards
     */
    public int getNumberOfCards() {
        return mObjectCards.size();
    }

    /** Get how many *usable* cards the player is holding at the moment
     *
     * @return The number of cards
     */
    public int getNumberOfUsableCards() {
        return mUsableObjectCards.size();
    }

    /** Remove an object card
     *
     * @param objectCard The object card to remove
     */
    public void removeObjectCard(ObjectCard objectCard) {
        mObjectCards.remove(objectCard);
        mUsableObjectCards.remove(objectCard);
    }

    /** Get the names of usable cards 
     *
     * @return The names array
     */
    public String[] getNamesOfUsableCards() {
        return cardsToString(mUsableObjectCards);
    }

    /** Get names of all cards
     *
     * @return The names array
     */
    public String[] getNamesOfCards() {
        return cardsToString(mObjectCards);
    }

    /** Remove an object card
     *
     * @param index Index of the card
     * @return The removed object card
     */
    public ObjectCard removeObjectCard(int index) {
        ObjectCard c = mObjectCards.remove(index);
        mUsableObjectCards.remove(c);
        return c;
    }

    /** Helper of getNamesOf*Cards()
     *
     * @param cards Cards array
     * @return Array of names
     */
    private String[] cardsToString(List<ObjectCard> cards) {
        String[] cardNames = new String[cards.size()];

        for(int i = 0; i<cards.size(); i++) {
            ObjectCard card = cards.get(i);
            cardNames[i] = card.getName();
        }

        return cardNames;
    }

    /** True if the player has adrenaline set
     *
     * @return A boolean value
     */
    public boolean getAdrenaline() {
        if(role instanceof Human)
            return ((Human)role).hasUsedAdrenaline();

        return false;
    }
}
