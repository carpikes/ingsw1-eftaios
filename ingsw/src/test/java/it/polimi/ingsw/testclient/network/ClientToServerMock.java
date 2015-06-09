/**
 * 
 */
package it.polimi.ingsw.testclient.network;

import java.io.IOException;

import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.game.network.GameCommand;

/**
 * @author Michele
 * @since 8 Jun 2015
 */
public class ClientToServerMock extends Connection {

    private boolean connected = false;
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.network.Connection#setHost(java.lang.String)
     */
    @Override
    public void setHost(String host) {
        return;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.network.Connection#connect()
     */
    @Override
    public void connect() throws IOException {
        connected = true;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.network.Connection#disconnect()
     */
    @Override
    public void disconnect() {
        connected = false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.network.Connection#sendPacket(it.polimi.ingsw.game.network.GameCommand)
     */
    @Override
    public void sendPacket(GameCommand pkt) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.network.Connection#isOnline()
     */
    @Override
    public boolean isOnline() {
        // TODO Auto-generated method stub
        return false;
    }

}
