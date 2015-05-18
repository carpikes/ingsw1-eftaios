package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 15, 2015
 */

public class ClientStateConnecting extends ClientState{

    public ClientStateConnecting(Client c, Game g) {
        super(c,g);
    }

    /**
     * Handle an incoming network message 
     * @param pkt In this state, the only allowed msg is the player username.
     */
    @Override
    public void handlePacket(NetworkPacket pkt) {
        synchronized(mGame) {
            synchronized(mClient) {
                if(pkt.getOpcode() == GameCommands.CMD_CS_USERNAME) {
                    String[] args = pkt.getArgs();
                    if(args.length == 0)
                        return;
                    
                    String name = args[0];
                    if(mGame.canSetName(name)) {
                        mClient.setUsername(name);
                        mClient.sendPacket(new NetworkPacket(GameCommands.CMD_SC_USEROK, String.valueOf(mGame.getNumberOfPlayers()), String.valueOf(mGame.getRemainingTime())));
                        mClient.setGameReady();
                    } else {
                        mClient.sendPacket(GameCommands.CMD_SC_USERFAIL);
                    }
                }
            }
        }
    }

}
