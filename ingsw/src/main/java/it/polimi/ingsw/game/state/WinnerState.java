/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.InfoOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class WinnerState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(WinnerState.class.getName());
    public WinnerState(GameState state, Integer winnerPlayer) {
        super(state);
        LOG.log(Level.FINE, "Constructor");
        
        state.broadcastPacket( new GameCommand(InfoOpcode.INFO_WINNER, winnerPlayer));
        state.sendPacketToPlayer(winnerPlayer, new GameCommand(GameOpcode.CMD_SC_WIN));
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        return this;
    }
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     */
    @Override
    public boolean stillInGame() {
        return false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    @Override
    protected void buildAndSendAvailableCommands() {

    }

}
