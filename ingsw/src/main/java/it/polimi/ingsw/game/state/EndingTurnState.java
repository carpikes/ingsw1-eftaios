/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class EndingTurnState implements PlayerState {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update( GameState gameState ) {
        NetworkPacket packet = gameState.getPacketFromQueue();
        
        PlayerState nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameCommand.CMD_CS_NOT_MY_TURN ) {
                nextState = new NotMyTurnState();
            } else if( packet.getOpcode() == GameCommand.CMD_CS_USE_OBJ_CARD ) {
                // TODO where should I put this?
                gameState.startUsingObjectCard( (ObjectCard)packet.getArgs()[0] );
            } else {
                throw new IllegalStateOperationException("You can only use an object card or end here. Discarding packet.");
            }
        }
        
        return nextState;
    }

    
}
