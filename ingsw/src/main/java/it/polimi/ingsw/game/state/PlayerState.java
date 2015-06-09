/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.ViewCommand;
import it.polimi.ingsw.game.network.ViewOpcode;
import it.polimi.ingsw.game.player.GamePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michele
 * @since 25 May 2015
 */
public abstract class PlayerState {
    protected final GameState mGameState;
    protected final GamePlayer mGamePlayer;
    
    protected PlayerState(GameState state) {
        mGameState = state;
        mGamePlayer = state.getCurrentPlayer();
    } 
    
    protected void sendAvailableCommands(ArrayList<ViewCommand> availableCommands) {
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
        if(pkt == null || pkt.getArgs() == null || pkt.getArgs().length == 0 || !(pkt.getArgs()[0] instanceof Integer))
            throw new IllegalStateOperationException("Which object card?");
        
        nextState = mGameState.startUsingObjectCard( (Integer)pkt.getArgs()[0] );
        if(nextState.equals(curState))
            buildAndSendAvailableCommands();
        return nextState;
    }
        
    protected void addObjectCardIfPossible(List<ViewCommand> availableCommands) {
        if(mGamePlayer.isObjectCardUsed() || mGamePlayer.getNumberOfUsableCards() == 0 || !mGamePlayer.isHuman())
            return;
        
        availableCommands.add(new ViewCommand(ViewOpcode.CMD_CHOOSEOBJECTCARD, (Serializable[]) mGamePlayer.getNamesOfUsableCards()));
    }
}
