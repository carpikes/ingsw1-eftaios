package it.polimi.ingsw.client.network;

import it.polimi.ingsw.game.common.GameCommand;

/** A packet listener
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 10, 2015
 */
public interface OnReceiveListener {

    /** This method is called on a received packet event
     * 
     * @param obj The received packet
     */
    public void onReceive(GameCommand obj);

    /** This method is called when the connection drops */
    public void onDisconnect();
}
