/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class EndingTurnState extends PlayerState {

    public EndingTurnState(GameState state, GamePlayer player) {
        super(state, player);
        mGameState.sendPacketToCurrentPlayer( GameCommand.CMD_SC_END_OF_TURN );
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        NetworkPacket packet = mGameState.getPacketFromQueue();
        
        PlayerState nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameCommand.CMD_CS_NOT_MY_TURN ) {
                nextState = new NotMyTurnState(mGameState, mGamePlayer);
            } else if( packet.getOpcode() == GameCommand.CMD_CS_USE_OBJ_CARD ) {
                mGameState.startUsingObjectCard( (ObjectCard)packet.getArgs()[0] );
            } else {
                throw new IllegalStateOperationException("You can only use an object card or end here. Discarding packet.");
            }
        }
        
        return nextState;
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}

    
}
