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
public class AwayState implements PlayerState {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update( GameState gameState ) {
        NetworkPacket packet = gameState.getPacketFromQueue();

        PlayerState nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameCommand.CMD_CS_AWAKE ) {
                nextState = new NotMyTurnState();
            } else {
                throw new IllegalStateOperationException("You can only awake here. Discarding packet.");
            }
        }
        
        return nextState;
    }

}
