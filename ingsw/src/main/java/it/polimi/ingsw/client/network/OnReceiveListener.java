package it.polimi.ingsw.client.network;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

public interface OnReceiveListener {
    public void onReceive(String msg);
    public void onDisconnect();
}
