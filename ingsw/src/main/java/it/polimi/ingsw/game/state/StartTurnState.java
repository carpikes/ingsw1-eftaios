/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class StartTurnState implements State {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public State update( GameState gameState ) {
        GamePlayer player = gameState.getCurrentPlayer();
        
        ArrayList< Point > availableSectors = gameState.getMap().getCellsWithMaxDistance( player.getCurrentPosition(), player.getMaxMoves() );
        player.sendPacket( new NetworkPacket(GameCommand.CMD_SC_START_TURN, availableSectors) );
        
        return new MovingState();
    }
}