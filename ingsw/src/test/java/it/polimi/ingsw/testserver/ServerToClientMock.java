package it.polimi.ingsw.testserver;

import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.ClientConn;

import java.util.LinkedList;
import java.util.Queue;

/** ClientConn mock
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public class ServerToClientMock extends ClientConn {
    /** Incoming queue */
    Queue<GameCommand> fakeIncomingPackets;

    /** Constructor */
    public ServerToClientMock() {
        fakeIncomingPackets = new LinkedList<>();
    }

    /** Run this (fake) connection */
    @Override
    public void run() {
        mIsConnected = true;
        return;
    }

    /** Send a command to the (fake) client 
     *
     * @param cmd The command 
     */
    @Override
    public void sendPacket(GameCommand cmd) {
        fakeIncomingPackets.add( cmd );
    }

    /** Disconnect */
    @Override
    public void disconnect() {
        mIsConnected = false;
        return;
    }

    /** Emulate a client disconnect */
    public void emulateDisconnect() {
        if(mClient != null)
            mClient.handleDisconnect();
    }

    /** Emulate a command read 
     *
     * @param cmd The command
     */
    public void emulateReadPacket(GameCommand cmd) {
        mClient.handlePacket(cmd);
    }

    /** Get the client
     *
     * @return The client
     */
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
