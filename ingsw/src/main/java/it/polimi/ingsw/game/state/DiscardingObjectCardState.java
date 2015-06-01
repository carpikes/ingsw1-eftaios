/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class DiscardingObjectCardState extends PlayerState {

    public DiscardingObjectCardState(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        NetworkPacket packet = mGameState.getPacketFromQueue();

        PlayerState nextState = this;
        if( packet != null ) {
            if ( mGamePlayer.isObjectCardUsed() ) {
                if( packet.getOpcode() == GameCommand.CMD_SC_DISCARD_OBJECT_CARD ) {
                    nextState = discardObjectCard(packet, nextState);
                } else {
                    throw new IllegalStateOperationException("You can only choose what object card to discard here because you already used a card during this turn. Discarding packet.");
                }
            } else {
                if( packet.getOpcode() == GameCommand.CMD_SC_DISCARD_OBJECT_CARD ) {
                    nextState = discardObjectCard(packet, nextState);
                } else if( packet.getOpcode() == GameCommand.CMD_CS_USE_OBJ_CARD ) {
                    nextState = mGameState.startUsingObjectCard( (ObjectCard)packet.getArgs()[0] );
                } else {
                    throw new IllegalStateOperationException("You can only choose a card to use or discard. Discarding packet.");
                }
            }
        }
        
        return nextState;
    }

    private PlayerState discardObjectCard(NetworkPacket packet,
            PlayerState nextState) {
        int index = (int)packet.getArgs()[0];
        if( index > 0 && index <= Config.MAX_NUMBER_OF_OBJ_CARDS ) { // <=, not <, because here we have a card over the limit 
            mGamePlayer.getObjectCards().remove(index);
            nextState = new EndingTurnState(mGameState, mGamePlayer);
        } else {
            throw new IllegalStateOperationException("Wrong index for card. Discarding packet.");
        }
        return nextState;
    }

	@Override
	public boolean stillInGame() {
		return true;
	}
    
    

}
