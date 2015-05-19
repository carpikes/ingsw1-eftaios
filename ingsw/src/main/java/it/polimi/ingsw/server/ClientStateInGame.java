package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 15, 2015
 */

public class ClientStateInGame extends ClientState{

    public ClientStateInGame(Client c, GameManager g) {
        super(c,g);
    }

    @Override
    public void handlePacket(NetworkPacket pkt) {
        // TODO Auto-generated method stub
        
    }

}
