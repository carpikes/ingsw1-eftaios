package it.polimi.ingsw.game.common;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 *
 */
public enum InfoOpcode implements Opcode {

    /** INFO TO *ALL* PLAYERS */
    INFO_HAS_MOVED,
    INFO_USED_HATCH,
    INFO_WINNER,
    INFO_LOSER, 
    INFO_PLAYER_ATTACKED, 
    INFO_START_TURN, 
    INFO_OBJ_CARD_USED,
    INFO_NOISE,
    INFO_SILENCE, 
    INFO_CHANGED_NUMBER_OF_CARDS,
    INFO_DISCARDED_OBJ_CARD,
    INFO_END_GAME,
    INFO_SPOTLIGHT, 
    INFO_AWAY, 
    INFO_ALIEN_FULL, 
}
