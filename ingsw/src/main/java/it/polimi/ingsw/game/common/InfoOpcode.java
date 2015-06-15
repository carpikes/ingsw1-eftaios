package it.polimi.ingsw.game.common;

/** Info to all players
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public enum InfoOpcode implements Opcode {

    /** This player has just moved */
    INFO_HAS_MOVED,

    /** Used a hatch */
    INFO_USED_HATCH,

    /** Winner */
    INFO_WINNER,

    /** Loser */
    INFO_LOSER, 

    /** Just attacked */
    INFO_PLAYER_ATTACKED, 

    /** Start turn */
    INFO_START_TURN, 

    /** Used a object card */
    INFO_OBJ_CARD_USED,

    /** Noise card */
    INFO_NOISE,

    /** Silence card */
    INFO_SILENCE, 

    /** Changed number of cards */
    INFO_CHANGED_NUMBER_OF_CARDS,

    /** Discarded an object card */
    INFO_DISCARDED_OBJ_CARD,

    /** End game. Goodbye */
    INFO_END_GAME,

    /** Spotlight card */
    INFO_SPOTLIGHT, 

    /** Player went away */
    INFO_AWAY, 

    /** Alien is full */
    INFO_ALIEN_FULL, 
}
