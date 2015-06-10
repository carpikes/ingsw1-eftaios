package it.polimi.ingsw.game.config;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 17, 2015
 */

/**
 * Configuration class: a list of constants for game and connections
 * @author Michele
 * @since 3 Jun 2015
 */
public class Config {
    
    // Game configuration here
    public static final int GAME_MIN_PLAYERS                = 2;
    public static final int GAME_MAX_PLAYERS                = 8;
    public static final int GAME_WAITING_SECS               = 5;
    
    public static final int GAME_MAP_WIDTH                  = 23;
    public static final int GAME_MAP_HEIGHT                 = 14;
    
    public static final int MAX_NUMBER_OF_TURNS             = 39;
    public static final int MAX_NUMBER_OF_OBJ_CARDS         = 3;
    public static final int MAX_HUMAN_MOVES                 = 1;
    public static final int MAX_HUMAN_ADRENALINE_MOVES      = 2;
    public static final int MAX_ALIEN_MOVES                 = 2;
    public static final int MAX_ALIEN_FULL_MOVES            = 3;
    
   
    
    // Server configuration here
    public static final long SERVER_CONNECTION_TIMEOUT      = 10000;
    public static final int SERVER_MAX_CLIENTS              = 3 * GAME_MAX_PLAYERS;
    
    /* Why port 3834? 3834 = 0xEFA -> Escape From Aliens
     * Note: ports < 1024 are privileges 
     */
    public static final int SERVER_TCP_LISTEN_PORT          = 3834;
    
    // Client configuration here
    public static final long CLIENT_TCP_PING_TIME           = 5000;
    
    // GUI settings
    public static final int GUI_NOISE_ANIMATION_TIME        = 30000;
    
    private Config() {}
}
