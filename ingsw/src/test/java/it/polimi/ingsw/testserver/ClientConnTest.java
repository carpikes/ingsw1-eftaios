package it.polimi.ingsw.testserver;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.ClientConn;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 18, 2015
 */

public class ClientConnTest extends ClientConn{

    @Override
    public void run() {
        mIsConnected = true;
        return;
    }

    @Override
    public void sendPacket(NetworkPacket pkt) {
        return;
    }

    @Override
    public void disconnect() {
        mIsConnected = false;
        return;
    }
    
    public void emulateDisconnect() {
        mClient.handleDisconnect();
    }

    public void emulateReadPacket(NetworkPacket pkt) {
        mClient.handlePacket(pkt);
    }
    
    public Client exposeClient() {
        return mClient;
    }
}
