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
    
    // Client configuration here
    public static final long CLIENT_TCP_PING_TIME           = 5000;
    
    // Game configuration here
    public static final int GAME_MIN_PLAYERS                = 2;
    public static final int GAME_MAX_PLAYERS                = 8;
    public static final int GAME_WAITING_SECS               = 30;
}
