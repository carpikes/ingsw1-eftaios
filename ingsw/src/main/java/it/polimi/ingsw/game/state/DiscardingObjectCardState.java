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
public class DiscardingObjectCardState extends PlayerState {

    public DiscardingObjectCardState(GameState state) {
        super(state);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        NetworkPacket packet = gameState.getPacketFromQueue();

        PlayerState nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameCommand.CMD_SC_DISCARD_OBJECT_CARD ) {
                // TODO to be implemented!
                
                nextState = new EndingTurnState(gameState);
            } else {
                throw new IllegalStateOperationException("You can only choose what object card to discard here. Discarding packet.");
            }
        }
        
        return nextState;
    }

	@Override
	public boolean stillInGame() {
		return true;
	}
    
    

}
