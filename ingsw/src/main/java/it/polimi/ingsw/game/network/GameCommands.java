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
    public static final int CMD_SC_CHOOSEUSER       = 6;
    public static final int CMD_CS_USERNAME         = 7;
    public static final int CMD_SC_USEROK           = 8;
    public static final int CMD_SC_USERFAIL         = 9;
    public static final int CMD_SC_RUN              = 10;
    public static final int CMD_SC_WIN              = 11;
    public static final int CMD_SC_CHOOSEMAP        = 12;
    public static final int CMD_CS_LOADMAP          = 13;
    public static final int CMD_SC_MAPOK            = 14;
    public static final int CMD_SC_MAPFAIL          = 15;
    
    /** GAME COMMANDS */
    public static final int CMD_CS_ATTACK           = 16;
    public static final int CMD_CS_DISCARD_OBJ_CARD = 17;
    public static final int CMD_CS_USE_OBJ_CARD     = 18;
    public static final int CMD_CS_SET_POSITION     = 19;
}
