/**
 * 
 */
package it.polimi.ingsw.game.state;

import java.util.ArrayList;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 25 May 2015
 */
public abstract class PlayerState {
    protected final GameState mGameState;
    protected final GamePlayer mGamePlayer;
    
    protected PlayerState(GameState state, GamePlayer player) {
        mGameState = state;
        mGamePlayer = player;
    } 
    
    protected void sendAvailableCommands(ArrayList<GameViewCommand> availableCommands) {
        mGameState.sendPacketToCurrentPlayer(new GameCommand(GameOpcode.CMD_SC_AVAILABLE_COMMANDS, availableCommands));
    }
    
    public abstract PlayerState update();

	public abstract boolean stillInGame();
	protected abstract void buildAndSendAvailableCommands();
	

    /**
     * @param pkt
     * @return
     */
    protected PlayerState useObjectCard(PlayerState curState, GameCommand pkt) {
        PlayerState nextState;
        nextState = mGameState.startUsingObjectCard( (ObjectCard)pkt.getArgs()[0] );
        if(nextState.equals(curState))
            buildAndSendAvailableCommands();
        return nextState;
    }
}
