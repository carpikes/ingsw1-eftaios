/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.player.GamePlayer;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class StartTurnState extends PlayerState {

    public StartTurnState(GameState state, GamePlayer player) {
        super(state, player);
      
        player.resetValues();
        player.incrementMoveCounter();
        
        if( player.getMoveCounter() >= Config.MAX_NUMBER_OF_TURNS ) {
        	state.endGame();
        } else {
        	// tell everybody I'm starting playing!
        	state.broadcastPacket( GameOpcode.INFO_START_TURN );
        }
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {       
        return new MovingState(mGameState, mGamePlayer);
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}

    @Override
    protected void buildAndSendAvailableCommands() {
        // TODO Auto-generated method stub
        
    }
}