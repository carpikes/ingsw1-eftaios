/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class StartTurnState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(StartTurnState.class.getName());
    public StartTurnState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        mGamePlayer.resetValues();
        
        // tell everybody I'm starting playing!
        state.broadcastPacket( new GameCommand(GameOpcode.INFO_START_TURN, state.getTurnId()) );
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {       
        return new MovingState(mGameState);
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
