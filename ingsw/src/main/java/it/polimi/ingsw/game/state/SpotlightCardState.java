/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.awt.Point;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class SpotlightCardState extends PlayerState {

    public SpotlightCardState(GameState state) {
        super(state);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        NetworkPacket packet = gameState.getPacketFromQueue();
        
        PlayerState nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameCommand.CMD_CS_SET_POSITION ) {
                gameState.light( (Point)packet.getArgs()[0] );
                
                nextState = gameState.getCurrentPlayer().getStateBeforeSpotlightCard();
                gameState.getCurrentPlayer().setStateBeforeSpotlightCard(null);
            } else {
                throw new IllegalStateOperationException("You can only choose a position here. Discarding packet.");
            }
        }
        
        return nextState;
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}

}
