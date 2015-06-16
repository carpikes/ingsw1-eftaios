package it.polimi.ingsw.client;

import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.ViewCommand;

import java.awt.Point;
import java.util.List;

/** View interface
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 18, 2015
 */
public abstract class View {

    /** Stop event */
    protected boolean mStopEvent = false; 
    
    /** Controller */
    protected final GameController mController;

    /** Constructor
     *
     * @param controller Game Controller
     */
    public View(GameController controller) {
        mController = controller;
    }

    /** Ask which connection the user want to use
     * 
     * @param params Available connections
     * @return The index of the chosen connection
     */
    public abstract int askConnectionType(String[] params);

    /** Ask the hostname
     * 
     * @return The chosen hostname
     */
    public abstract String askHost();

    /** Ask the username
     * 
     * @param message Prompt message
     * @return The username
     */
    public abstract String askUsername(String message);

    /** Ask something to the user
     *
     * @param viewList Choice list
     * @return The choice
     */
    public abstract Integer askView( String[] viewList );

    /** Ask which map to load
     * 
     * @param mapList List of maps
     * @return Index of the chosen map (-1 = Random)
     */
    public abstract Integer askMap(String[] mapList);

    /** Run the Interface*/
    public abstract void run();

    /** Display an error
     * 
     * @param string Error message
     */
    public abstract void showError(String string);

    /** [Login] Notify a change of remaining time
     * 
     * @param remainingTime Remaining time
     */
    public abstract void updateLoginTime(int remainingTime);

    /** [Login] Notify a change of players connected
     *  
     * @param numOfPlayers Number of connected players in your game
     */
    public abstract void updateLoginStat(int numOfPlayers);

    /** Notify to switch to main screen. The game is started
     * 
     * @param container Some infos about this game
     */
    public abstract void switchToMainScreen(GameInfo container);

    /** Handle a command
     *
     * @param cmd List of commands
     */
    protected abstract void handleCommand(List<ViewCommand> cmd);

    /** Show an info
     *
     * @param user Current player
     * @param message  Message
     */
    public abstract void showInfo(String user, String message);

    /** Show a noise in the specified sector
     *
     * @param user Current player
     * @param p Point
     */
    public abstract void showNoiseInSector(String user, Point p);

    /** Called if is my turn */
    public abstract void onMyTurn();

    /** Called if is turn of anyone
     *
     * @param username Username
     */
    public abstract void onOtherTurn(String username);

    /** Startup */
    public abstract void startup();

    /** Show ending
     * @param winnerList The list of winners' id
     * @param loserList The list of losers's id
     */
    public abstract void showEnding(List<Integer> winnerList, List<Integer> loserList);

    /** Notification on object card change
     * @param listOfCards list of card
     */
    public abstract void notifyObjectCardListChange(List<Integer> listOfCards);

    /** Update player info
     *  @param idPlayer Id
     */
    public abstract void updatePlayerInfoDisplay( int idPlayer );

    /** Handle a spotlight result. 
     * (e.g. n players spotted)
     *
     * @param chosenPoint Player coordinates. null = not spotted
     * @param playersFound How many players are there
     */
    public abstract void handleSpotlightResult(Point chosenPoint, Point[] playersFound);

    /** Handles an attack
     *
     * @param user Username
     * @param p Point
     */
    public abstract void handleAttack(String user, Point p);

}
