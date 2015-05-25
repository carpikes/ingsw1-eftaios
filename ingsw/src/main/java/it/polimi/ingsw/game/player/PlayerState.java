/**
 * 
 */
package it.polimi.ingsw.game.player;

/** Enumeration representing the current state of a player in a game.
 * Note that some states are part of a bigger state.
 * Changes of states are determined only by the GameState class. 
 * @author Michele
 * @since 23 May 2015
 */
public enum PlayerState {
    START_TURN(true), // before moving
    MOVING(false), // after moving
    MOVE_DONE(true),
    DANGEROUS_CARD_DRAWN(true),
    USING_DANGEROUS_CARD(false), // invoked when choosing a state after drawing a Noise in any sector card
    DISCARDING_OBJECT_CARD(false),
    USING_OBJECT_CARD(true), // invoked when choosing a state after drawing a Spotlight card
    ENDING_TURN(false),
    NOT_MY_TURN(false),
    WINNER(false),
    LOSER(false),
    AWAY(false);
    
    private boolean canUseObjectCard;
    
    private PlayerState( boolean canUseObjectCard ) {
        this.canUseObjectCard = canUseObjectCard;
    }

    public boolean isObjectCardUsable() {
        return canUseObjectCard;
    }
}
