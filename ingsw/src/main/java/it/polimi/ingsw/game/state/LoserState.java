/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class LoserState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(LoserState.class.getName());

    public LoserState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        state.broadcastPacket( GameOpcode.INFO_LOSER );
        mGameState.sendPacketToCurrentPlayer( GameOpcode.CMD_SC_LOSE );
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
    }
}
