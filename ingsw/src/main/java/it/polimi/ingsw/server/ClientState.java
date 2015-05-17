package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 15, 2015
 */

public abstract class ClientState {
    protected Client mClient;
    protected Game mGame;
    public ClientState(Client c, Game g) {
        mClient = c;
        mGame = g;
    }
    
    public abstract void handlePacket(NetworkPacket pkt);
}
