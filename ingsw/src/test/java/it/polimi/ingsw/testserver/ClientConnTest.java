package it.polimi.ingsw.testserver;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.server.ClientConn;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 18, 2015
 */

public class ClientConnTest extends ClientConn{

    @Override
    public void run() {
        return;
    }

    @Override
    public void sendPacket(NetworkPacket pkt) {
        return;
    }

    @Override
    public void disconnect() {
        return;
    }
    
    public void doDisconnect() {
        mClient.handleDisconnect();
    }

}
