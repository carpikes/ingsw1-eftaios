/**
 * 
 */
package it.polimi.ingsw.game.state;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class NotMyTurnState extends PlayerState {
	private static final Logger LOG = Logger.getLogger(NotMyTurnState.class.getName());
    public NotMyTurnState(GameState state) {
        super(state);
        
        LOG.log(Level.FINE, "Constructor");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        return this;
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
