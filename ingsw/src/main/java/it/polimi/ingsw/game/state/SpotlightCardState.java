/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.network.GameViewOpcode;
import it.polimi.ingsw.game.player.GamePlayer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class SpotlightCardState extends PlayerState {
	private static final Logger LOG = Logger.getLogger(SpotlightCardState.class.getName());
    public SpotlightCardState(GameState state, GamePlayer player) {
        super(state, player);
        LOG.log(Level.FINE, "Constructor");
        buildAndSendAvailableCommands();
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
            if( packet.getOpcode() == GameOpcode.CMD_CS_CHOSEN_MAP_POSITION ) {
                mGameState.spotlightAction( (Point)packet.getArgs()[0] );
                
                nextState = mGamePlayer.getStateBeforeSpotlightCard();
                nextState.buildAndSendAvailableCommands();
                mGamePlayer.setStateBeforeSpotlightCard(null);
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
