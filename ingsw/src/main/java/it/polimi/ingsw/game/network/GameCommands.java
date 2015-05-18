package it.polimi.ingsw.game.network;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 17, 2015
 */

public class GameCommands {
    private GameCommands() { }
    
    public static final int CMD_INVALID             = 0;
    public static final int CMD_PING                = 1;
    public static final int CMD_BYE                 = 2;
    public static final int CMD_SC_STAT             = 3;
    public static final int CMD_SC_TIME             = 4;
    public static final int CMD_CS_USERNAME         = 5;
    public static final int CMD_SC_USEROK           = 6;
    public static final int CMD_SC_USERFAIL         = 7;
    public static final int CMD_SC_RUN              = 8;
    public static final int CMD_SC_WIN              = 9;
}
