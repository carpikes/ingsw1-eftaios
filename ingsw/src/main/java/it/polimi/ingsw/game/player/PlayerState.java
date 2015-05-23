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
    START_OF_TURN,
    MOVE_DONE,
    HATCH_CARD_DRAWN,
    ATTACK,
    DANGEROUS_CARD_DRAWN,
    OBJECT_CARD_DRAWN,
    END_OF_TURN,
    WINNER,
    LOSER,
    AWAY
}
