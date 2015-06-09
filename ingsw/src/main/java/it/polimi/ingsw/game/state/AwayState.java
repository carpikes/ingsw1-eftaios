/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.InfoOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class AwayState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(AwayState.class.getName());
    public AwayState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");
        
        state.broadcastPacket( InfoOpcode.INFO_AWAY );
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
        return false;
    }

    @Override
    protected void buildAndSendAvailableCommands() {
        // TODO Auto-generated method stub
        
    }

}
