/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.InfoOpcode;
import it.polimi.ingsw.game.network.ViewCommand;
import it.polimi.ingsw.game.network.ViewOpcode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class DiscardingObjectCardState extends PlayerState {
	private static final Logger LOG = Logger.getLogger(DiscardingObjectCardState.class.getName());
	
    public DiscardingObjectCardState(GameState state) {
        super(state);

        LOG.log(Level.FINE, "Constructor");
        buildAndSendAvailableCommands();
    }

    @Override
    protected void buildAndSendAvailableCommands() {
        ArrayList<ViewCommand> availableCommands = new ArrayList<>();

        if(mGamePlayer.getNumberOfCards() > Config.MAX_NUMBER_OF_OBJ_CARDS) {
            availableCommands.add(new ViewCommand(ViewOpcode.CMD_DISCARDOBJECTCARD, (Serializable[]) mGamePlayer.getNamesOfCards()));
            addObjectCardIfPossible(availableCommands);
            sendAvailableCommands(availableCommands);
        }
    }
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        GameCommand packet = mGameState.getPacketFromQueue();

        PlayerState nextState = this;
        
        if(mGamePlayer.getNumberOfCards() <= Config.MAX_NUMBER_OF_OBJ_CARDS)
            nextState = new EndingTurnState(mGameState);
        else if( packet != null ) {
            if( packet.getOpcode() == GameOpcode.CMD_CS_DISCARD_OBJECT_CARD )
                nextState = discardObjectCard(packet, nextState);
            else if ( !mGamePlayer.isObjectCardUsed() ) {
                if( packet.getOpcode() == GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD )
                    nextState = useObjectCard(this, packet);
                else
                    throw new IllegalStateOperationException("You can only choose what object card to discard here because you already used a card during this turn. Discarding packet.");
            }
        }
        
        return nextState;
    }

    private PlayerState discardObjectCard(GameCommand packet, PlayerState nextState) {
        int index = (int)packet.getArgs()[0];
        if( index >= 0 && index <= Config.MAX_NUMBER_OF_OBJ_CARDS ) { // <=, not <, because here we have a card over the limit 
            ObjectCard objectCard = mGamePlayer.removeObjectCard(index);
            mGameState.broadcastPacket( new GameCommand(InfoOpcode.INFO_DISCARDED_OBJ_CARD, objectCard.getId(), objectCard.getName()));
            nextState = new EndingTurnState(mGameState);
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
