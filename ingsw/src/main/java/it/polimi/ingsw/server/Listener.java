package it.polimi.ingsw.server;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 18, 2015
 */

public interface Listener extends Runnable{
    public void tearDown();
    public boolean isDown();
    public boolean isUp();
}
