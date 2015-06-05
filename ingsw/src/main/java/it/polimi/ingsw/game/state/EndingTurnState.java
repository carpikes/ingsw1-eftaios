/**
 * 
 */
package it.polimi.ingsw.game.state;

import java.util.ArrayList;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.network.GameViewOpcode;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class EndingTurnState extends PlayerState {

    public EndingTurnState(GameState state, GamePlayer player) {
        super(state, player);
        mGameState.sendPacketToCurrentPlayer( GameOpcode.CMD_SC_END_OF_TURN );
    }
    
    @Override
    protected void buildAndSendAvailableCommands() {
        ArrayList<GameViewCommand> availableCommands = new ArrayList<>();
        availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_ENDTURN));
        
        if(!mGamePlayer.isObjectCardUsed())
            availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_CHOOSEOBJECTCARD));
        
        sendAvailableCommands(availableCommands);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        GameCommand packet = mGameState.getPacketFromQueue();
        
        PlayerState nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameOpcode.CMD_CS_END_TURN )
                nextState = new NotMyTurnState(mGameState, mGamePlayer);
            else if( packet.getOpcode() == GameOpcode.CMD_CS_USE_OBJ_CARD ) 
                nextState = useObjectCard(this, packet);
            else
                throw new IllegalStateOperationException("You can only use an object card or end here. Discarding packet.");
        }
        
        return nextState;
    }

    
    @Override
	public boolean stillInGame() {
		return true;
	}
}
