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
    START_OF_TURN(true), // before moving
    MOVE_DONE(true), // after moving
    HATCH_CARD_DRAWN(false),
    ATTACK(false),
    DANGEROUS_CARD_DRAWN(true),
    USING_DANGEROUS_CARD(false), // invoked when choosing a state after drawing a Noise in any sector card
    OBJECT_CARD_DRAWN(false), // invoked when drawing a card: can lead to a DiscardObjectCardCommand
    USING_OBJECT_CARD(true), // invoked when choosing a state after drawing a Spotlight card
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
