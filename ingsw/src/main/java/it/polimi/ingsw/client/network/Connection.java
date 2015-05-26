package it.polimi.ingsw.client.network;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.io.IOException;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public abstract class Connection {
    protected OnReceiveListener mListener = null;
    
    public void setOnReceiveListener(OnReceiveListener listener) {
        mListener = listener;
    }

    public abstract void setHost(String host);
    public abstract void connect() throws IOException;
    public abstract void disconnect();
    public abstract void sendPacket(NetworkPacket pkt);
    public abstract boolean isOnline();
    
    public void sendPacket(GameCommand opcode) {
        sendPacket(new NetworkPacket(opcode));
    }
}
