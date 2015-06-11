package it.polimi.ingsw.game.common;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 *
 */
public enum CoreOpcode implements Opcode {
    CMD_INVALID,
    CMD_PING,
    CMD_BYE,
    CMD_SC_FULL,
    CMD_SC_STAT,
    CMD_SC_TIME,
    CMD_SC_CHOOSEUSER,
    CMD_CS_USERNAME,
    CMD_SC_USERNAMEOK,
    CMD_SC_USERNAMEFAIL,
    CMD_SC_RUN,
    CMD_SC_CHOOSEMAP,
    CMD_CS_LOADMAP,
    CMD_SC_MAPOK,
    CMD_SC_MAPFAIL,
}
