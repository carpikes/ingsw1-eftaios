package it.polimi.ingsw.testserver;

import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.ClientConn;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */

public class ClientConnMock extends ClientConn {

    @Override
    public void run() {
        mIsConnected = true;
        return;
    }

    @Override
    public void sendPacket(GameCommand pkt) {
        return;
    }

    @Override
    public void disconnect() {
        mIsConnected = false;
        return;
    }
    
    public void emulateDisconnect() {
        if(mClient != null)
            mClient.handleDisconnect();
    }

    public void emulateReadPacket(GameCommand pkt) {
        mClient.handlePacket(pkt);
    }
    
    public Client exposeClient() {
        return mClient;
    }
}
