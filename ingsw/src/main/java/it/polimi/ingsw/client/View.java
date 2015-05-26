package it.polimi.ingsw.client;

import it.polimi.ingsw.game.network.GameInfoContainer;

import java.util.Map;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public interface View {
    /** Ask which connection the user want to use
     * 
     * @param params Available connections
     * @return The index of the chosen connection
     */
    public int askConnectionType(String[] params);
    
    /** Ask the hostname
     * 
     * @return The chosen hostname
     */
    public String askHost();
    
    /** Ask the username
     * 
     * @param message Prompt message
     * @return The username
     */
    public String askUsername(String message);
    
    /** Ask which map to load
     * 
     * @param mapList List of maps
     * @return Index of the chosen map (-1 = Random)
     */
    public Integer askMap(String[] mapList);
    
    /** Run the Interface*/
    public void run();

    /** Display an error
     * 
     * @param string Error message
     */
    public void showError(String string);
    
    /** [Login] Notify a change of remaining time
     * 
     * @param remainingTime Remaining time
     */
    public void updateLoginTime(int remainingTime);
    
    /** [Login] Notify a change of players connected
     *  
     * @param numOfPlayers Number of connected players in your game
     */
    public void updateLoginStat(int numOfPlayers);
    
    /** Notify to switch to main screen. The game is started
     * 
     * @param container Some infos about this game
     */
    public void switchToMainScreen(GameInfoContainer container);
    
    /** Notify a closed connection */
    public void close();
}
