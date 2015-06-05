/**
 * 
 */
package it.polimi.ingsw.game.state;

import java.util.ArrayList;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.network.GameViewOpcode;
import it.polimi.ingsw.game.player.GamePlayer;

import java.awt.Point;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class NoiseInAnySectorState extends PlayerState {

    public NoiseInAnySectorState(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void buildAndSendAvailableCommands() {
        ArrayList<GameViewCommand> availableCommands = new ArrayList<>();
        availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_ENABLEMAPVIEW));

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
            if( packet.getOpcode() == GameOpcode.CMD_CS_CHOSEN_MAP_POSITION  && mGameState.getMap().isWithinBounds((Point)packet.getArgs()[0])) {
            	mGameState.broadcastPacket( new GameCommand(GameOpcode.INFO_NOISE, packet.getArgs()[0] ) );
                nextState = mGameState.getObjectCard( );
            } else {
                throw new IllegalStateOperationException("You can only choose a position here. Discarding packet.");
            }
        }
        
        return nextState;
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}
}
