package it.polimi.ingsw.game.network;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 17, 2015
 */

public class GameCommands {
    private GameCommands() { }
    
    /** LOGIN COMMANDS */
    public static final int CMD_INVALID             = 0;
    public static final int CMD_PING                = 1;
    public static final int CMD_BYE                 = 2;
    public static final int CMD_SC_FULL             = 3;
    public static final int CMD_SC_STAT             = 4;
    public static final int CMD_SC_TIME             = 5;
    public static final int CMD_CS_USERNAME         = 6;
    public static final int CMD_SC_USEROK           = 7;
    public static final int CMD_SC_USERFAIL         = 8;
    public static final int CMD_SC_RUN              = 9;
    public static final int CMD_SC_WIN              = 10;
    
    /** GAME COMMANDS */
    public static final int CMD_CS_ATTACK           = 11;
    public static final int CMD_CS_DISCARD_OBJ_CARD = 12;
    public static final int CMD_CS_USE_OBJ_CARD     = 13;
    public static final int CMD_CS_SET_POSITION     = 14;
}
