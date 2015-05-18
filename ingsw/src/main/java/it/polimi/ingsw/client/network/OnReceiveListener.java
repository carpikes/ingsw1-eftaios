package it.polimi.ingsw.client.network;

import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

public interface OnReceiveListener {
    public void onReceive(NetworkPacket obj);
    public void onDisconnect();
}
