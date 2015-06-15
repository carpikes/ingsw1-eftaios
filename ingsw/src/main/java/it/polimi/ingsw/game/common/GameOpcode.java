package it.polimi.ingsw.game.common;

/** Game opcodes
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 17, 2015
 */
public enum GameOpcode implements Opcode{

    /** GAME COMMANDS FROM CLIENT TO SERVER */

    /** Attack */
    CMD_CS_ATTACK,
    /** Discard object */
    CMD_CS_DISCARD_OBJECT_CARD,
    /** Chosen object card */
    CMD_CS_CHOSEN_OBJECT_CARD,
    /** Change the position */
    CMD_CS_SET_POSITION,
    /** I've chosen a position on the map */
    CMD_CS_CHOSEN_MAP_POSITION,
    /** Draw a dangerous card */
    CMD_CS_DRAW_DANGEROUS_CARD,
    /** Chosen a position for noise in any sector card */
    CMD_CS_NOISE_IN_ANY_SECTOR_POSITION,
    /** End turn */
    CMD_CS_END_TURN,

    /** GAME COMMANDS TO *A SINGLE* CLIENT */

    /** Those commands are available in this state */
    CMD_SC_AVAILABLE_COMMANDS,
    /** You got an object card */
    CMD_SC_OBJECT_CARD_OBTAINED,
    /** Invalid move */
    CMD_SC_MOVE_INVALID,
    /** Drop a card */
    CMD_SC_DROP_CARD,
    /** Lose */
    CMD_SC_LOSE,
    /** Win */
    CMD_SC_WIN,
}
