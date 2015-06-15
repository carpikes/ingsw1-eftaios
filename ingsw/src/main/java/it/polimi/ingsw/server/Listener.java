package it.polimi.ingsw.server;

/** Common server listeners interface
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public interface Listener extends Runnable{

    /** Shut down the server */
    public void tearDown();

    /** Check if this listener is correctly shutted down
     * 
     * @return True if this listener is down
     */
    public boolean isDown();

    /** Check if this listener is running
     * 
     * @return True if this listener is correctly listening
     */
    public boolean isUp();
}
