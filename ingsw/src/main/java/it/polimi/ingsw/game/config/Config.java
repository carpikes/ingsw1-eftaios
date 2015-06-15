package it.polimi.ingsw.game.config;

/** Configuration class: a list of constants for game and connections
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 17, 2015
 */
public class Config {

    /** Game configuration here */

    /** Minimum players in a game */
    public static final int GAME_MIN_PLAYERS                = 2;

    /** Maximum players in a game */
    public static final int GAME_MAX_PLAYERS                = 8;

    /** Players waiting secs (default: 30) */
    public static final int GAME_WAITING_SECS               = 5;

    /** Max seconds per turn */
    public static final int GAME_MAX_SECONDS_PER_TURN       = 120;

    /** Map width */
    public static final int GAME_MAP_WIDTH                  = 23;

    /** Map height */
    public static final int GAME_MAP_HEIGHT                 = 14;

    /** Max number of turns */
    public static final int MAX_NUMBER_OF_TURNS             = 39;

    /** Max number of object card per player */
    public static final int MAX_NUMBER_OF_OBJ_CARDS         = 3;

    /** Max sectors that a human can cross in a move */
    public static final int MAX_HUMAN_MOVES                 = 1;

    /** Max sectors that a human can cross after using adrenaline */
    public static final int MAX_HUMAN_ADRENALINE_MOVES      = 2;

    /** Max sectors that an alien can cross */
    public static final int MAX_ALIEN_MOVES                 = 2;

    /** Max sectors that an alien can cross after eating a human */
    public static final int MAX_ALIEN_FULL_MOVES            = 3;


    /** Server configuration here */

    /** Server connection timeout in milliseconds */
    public static final long SERVER_CONNECTION_TIMEOUT      = 10000;

    /** Max clients that a server can handle */
    public static final int SERVER_MAX_CLIENTS              = 5 * GAME_MAX_PLAYERS;

    /** TCP listening port
     * Why port 3834? 3834 = 0xEFA -> Escape From Aliens
     * Note: ports < 1024 are privileges 
     */
    public static final int SERVER_TCP_LISTEN_PORT          = 3834;


    /** Client configuration here */

    /** Client ping timeout (TCP) */
    public static final long CLIENT_TCP_PING_TIME           = 5000;

    /** GUI settings */

    /** Window width */
    public static final int WIDTH = 1024;

    /** Left view width */
    public static final int WIDTH_LEFT  = 745;

    /** Right view width */
    public static final int WIDTH_RIGHT  = WIDTH - WIDTH_LEFT;

    /** Window height */
    public static final int HEIGHT = 768;

    /** Bottom view height */
    public static final int HEIGHT_BOTTOM = 50;

    /** Top view height */
    public static final int HEIGHT_TOP = HEIGHT - HEIGHT_BOTTOM;

    /** Panel margin */
    public static final int PANEL_MARGIN = 10;

    /** Card horizontal gap */
    public static final int CARD_HGAP = 5;

    /** Card vertical gap */
    public static final int CARD_VGAP = 5;

    /** Users horizontal gap */
    public static final int USERS_HGAP = 10;    

    /** Card width (based on image size) */
    public static final int CARD_WIDTH = WIDTH_RIGHT - 2 * PANEL_MARGIN - 2 * CARD_HGAP;

    /** Card height (based on image size) */
    public static final int CARD_HEIGHT = CARD_WIDTH * 745 / 490;

    /** Private constructor */
    private Config() {}
}
