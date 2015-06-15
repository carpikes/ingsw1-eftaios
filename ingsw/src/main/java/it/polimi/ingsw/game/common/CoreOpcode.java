package it.polimi.ingsw.game.common;

/** Core Opcodes
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public enum CoreOpcode implements Opcode {
    /** Invalid opcode */
    CMD_INVALID,
    /** Ping */
    CMD_PING,
    /** Good bye */
    CMD_BYE,
    /** Server is full */
    CMD_SC_FULL,
    /** Players connected */
    CMD_SC_STAT,
    /** Remaining login time */
    CMD_SC_TIME,
    /** Choose username */
    CMD_SC_CHOOSEUSER,
    /** My username is.. */
    CMD_CS_USERNAME,
    /** Username OK */
    CMD_SC_USERNAMEOK,
    /** Username KO */
    CMD_SC_USERNAMEFAIL,
    /** Game is ready */
    CMD_SC_RUN,
    /** Please choose a map */
    CMD_SC_CHOOSEMAP,
    /** This is the map */
    CMD_CS_LOADMAP,
    /** Map ok */
    CMD_SC_MAPOK,
    /** Map ko */
    CMD_SC_MAPFAIL,
}
