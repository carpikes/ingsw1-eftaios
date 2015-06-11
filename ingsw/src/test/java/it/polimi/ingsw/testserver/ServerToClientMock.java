package it.polimi.ingsw.testserver;

import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.ClientConn;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */

public class ServerToClientMock extends ClientConn {

    Queue<GameCommand> fakeIncomingPackets;

    public ServerToClientMock() {
        fakeIncomingPackets = new LinkedList<>();
    }

    @Override
    public void run() {
        mIsConnected = true;
        return;
    }

    @Override

    public void sendPacket(GameCommand pkt) {
        fakeIncomingPackets.add( pkt );
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

    public GameCommand getPacketFromList() {
        if( !fakeIncomingPackets.isEmpty() )
            return fakeIncomingPackets.poll();
        else
            return null;
    }

    public void clearIncomingPacketQueue() {
        fakeIncomingPackets.clear();
    }
}
