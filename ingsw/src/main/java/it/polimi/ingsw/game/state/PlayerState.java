/**
 * 
 */
package it.polimi.ingsw.game.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameViewOpcode;
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
        if(pkt == null || pkt.getArgs() == null || pkt.getArgs().length == 0 || !(pkt.getArgs()[0] instanceof Integer))
            throw new IllegalStateOperationException("Which object card?");
        
        nextState = mGameState.startUsingObjectCard( (Integer)pkt.getArgs()[0] );
        if(nextState.equals(curState))
            buildAndSendAvailableCommands();
        return nextState;
    }
    
    protected void addObjectCardIfPossible(ArrayList<GameViewCommand> availableCommands) {
        if(mGamePlayer.isObjectCardUsed() || mGamePlayer.getNumberOfCards() > 0 || mGamePlayer.isAlien())
            return;
        
        ArrayList<ObjectCard> cards = mGamePlayer.getObjectCards();
        String[] cardNames = new String[cards.size()];
        
        for(int i = 0; i<cards.size(); i++) {
            ObjectCard card = cards.get(i);
            cardNames[i] = card.getName();
        }
            
        availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_CHOOSEOBJECTCARD, (Serializable[]) cardNames));
    }
}
