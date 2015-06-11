package it.polimi.ingsw.game.common;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 17, 2015
 */

public enum GameOpcode implements Opcode{

    /** GAME COMMANDS TO SERVER */
    
    CMD_CS_ATTACK,
    CMD_CS_DISCARD_OBJECT_CARD,
    CMD_CS_CHOSEN_OBJECT_CARD,
    CMD_CS_SET_POSITION,
    CMD_CS_CHOSEN_MAP_POSITION,
    CMD_CS_DRAW_DANGEROUS_CARD,
    CMD_CS_NOISE_IN_ANY_SECTOR_POSITION,
    CMD_CS_END_TURN,
    CMD_CS_AWAKE,
    
    /** GAME COMMANDS TO *A SINGLE* CLIENT */
    CMD_SC_AVAILABLE_COMMANDS,
    
    CMD_SC_OBJECT_CARD_OBTAINED,
    CMD_SC_MOVE_INVALID,
    CMD_SC_LOSE,
    CMD_SC_WIN,
}
