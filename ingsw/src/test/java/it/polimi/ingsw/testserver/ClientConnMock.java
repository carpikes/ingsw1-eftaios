package it.polimi.ingsw.testserver;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.ClientConn;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */

public class ClientConnMock extends ClientConn {

    Queue<NetworkPacket> fakeIncomingPackets;
    
    public ClientConnMock() {
        fakeIncomingPackets = new LinkedList<>();
    }
    
    @Override
    public void run() {
        mIsConnected = true;
        return;
    }

    @Override
    public void sendPacket(NetworkPacket pkt) {
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

    public void emulateReadPacket(NetworkPacket pkt) {
        mClient.handlePacket(pkt);
    }
    
    public Client exposeClient() {
        return mClient;
    }
    
    public NetworkPacket getPacketFromList() {
        if( !fakeIncomingPackets.isEmpty() )
            return fakeIncomingPackets.poll();
        else
            return null;
    }
    
    public void clearIncomingPacketQueue() {
        fakeIncomingPackets.clear();
    }
}
