package it.polimi.ingsw.client;

import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.GameViewCommand;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public abstract class View {
    private final LinkedBlockingQueue<ArrayList<GameViewCommand>> mViewQueue;
    protected boolean mStopEvent = false; 
    protected final GameController mController;
    
    public View(GameController controller) {
        mController = controller;
        mViewQueue = new LinkedBlockingQueue<>();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(mController.isRunning()) {
                        ArrayList<GameViewCommand> cmd = mViewQueue.take();
                        handleCommand(cmd);
                    }
                } catch(InterruptedException e) {
                    mController.stop();
                }
                close();
            }
        }).start();
    }
    
    protected void enqueueCommand(ArrayList<GameViewCommand> arrayList) {
        try {
            mViewQueue.put(arrayList);
        } catch(InterruptedException e) {
            // TODO: log(e)
        }
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
    public abstract void switchToMainScreen(GameStartInfo container);
    
    /** Notify a closed connection */
    public abstract void close();
    
    protected abstract void handleCommand(ArrayList<GameViewCommand> cmd);

	public abstract void showInfo(String user, String message);

	public abstract void showNoiseInSector(String user, Point p);
}