/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class StartTurnState extends PlayerState {

    public StartTurnState(GameState state) {
        super(state);
        
        GamePlayer player = gameState.getCurrentPlayer();        
        player.resetValues();
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        GamePlayer player = gameState.getCurrentPlayer();
        
        player.sendPacket( new NetworkPacket(GameCommand.CMD_SC_START_TURN, player.getCurrentPosition(), player.getMaxMoves()) );
        
        return new MovingState(gameState);
    }
}