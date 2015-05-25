/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class UsingDangerousCardState implements State {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public State update( GameState gameState ) {
        NetworkPacket packet = gameState.getPacketFromQueue();

        State nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameCommand.CMD_CS_NOISE_IN_ANY_SECTOR_POSITION ) {
                gameState.getGameManager().broadcastPacket( new NetworkPacket(GameCommand.CMD_SC_NOISE, packet.getArgs() ) );
                nextState = gameState.getObjectCard( );
            } else {
                throw new IllegalStateOperationException("You can only choose a position here. Discarding packet.");
            }
        }
        
        return nextState;
    }
}
