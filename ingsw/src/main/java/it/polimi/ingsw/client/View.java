package it.polimi.ingsw.client;

import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.ViewCommand;

import java.awt.Point;
import java.util.List;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public abstract class View {

    protected boolean mStopEvent = false; 
    protected final GameController mController;

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

    protected abstract void handleCommand(List<ViewCommand> cmd);

    public abstract void showInfo(String user, String message);

    public abstract void showNoiseInSector(String user, Point p);

    /**
     * @param username
     */
    public abstract void onMyTurn();
    public abstract void onOtherTurn(String username);

    /**
     * 
     */
    public abstract void startup();

    /**
     * @param winnerList The list of winners' id
     * @param loserList The list of losers's id
     * @return True when the game has to be stopped
     */
    public abstract void showEnding(List<Integer> winnerList, List<Integer> loserList);

    /**
     * @param listOfCards
     */
    public abstract void notifyObjectCardListChange(List<Integer> listOfCards);

    /**
     * 
     */
    public abstract void updatePlayerInfoDisplay( int idPlayer );

    /** Handle a spotlight result. (e.g. n players spotted)
     * @param coords Player coordinates. null = not spotted
     */
    public abstract void handleSpotlightResult(Point chosenPoint, Point[] playersFound);

}
