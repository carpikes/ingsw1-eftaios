package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.InfoOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Winner state.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class WinnerState extends PlayerState {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(WinnerState.class.getName());

    /** Constructor
     *
     * @param state GameState
     * @param winnerPlayer The winner player id
     */
    public WinnerState(GameState state, Integer winnerPlayer) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        state.broadcastPacket( new GameCommand(InfoOpcode.INFO_WINNER, winnerPlayer));
        state.sendPacketToPlayer(winnerPlayer, new GameCommand(GameOpcode.CMD_SC_WIN));
    }

    /** Update the game
     * @see it.polimi.ingsw.game.state.State#update()
     * @return New player state
     */
    @Override
    public PlayerState update() {
        return this;
    }

    /** Is the player still in game?
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     * @return True if the player is still in game
     */
    @Override
    public boolean stillInGame() {
        return false;
    }

    /** Build and send available commands
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    @Override
    protected void buildAndSendAvailableCommands() {
        /** no commands */
    }

}
