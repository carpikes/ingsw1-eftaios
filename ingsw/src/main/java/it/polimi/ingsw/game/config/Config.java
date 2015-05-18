package it.polimi.ingsw.game.config;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 17, 2015
 */

public class Config {
    
    private Config() {}
    
    // Server configuration here
    public static final long SERVER_CONNECTION_TIMEOUT      = 10000;
    public static final int SERVER_MAX_CLIENTS              = 1000;
    /* Why port 3834? 3834 = 0xEFA -> Escape From Aliens
     * Note: ports < 1024 are privileges 
     */
    public static final int SERVER_TCP_LISTEN_PORT          = 3834;
    
    // Client configuration here
    public static final long CLIENT_TCP_PING_TIME           = 5000;
    
    // Game configuration here
    public static final int GAME_MIN_PLAYERS                = 2;
    public static final int GAME_MAX_PLAYERS                = 8;
    public static final int GAME_WAITING_SECS               = 30;
}
