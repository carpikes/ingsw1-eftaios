package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.InfoOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Away State
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class AwayState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(AwayState.class.getName());
    public AwayState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        state.broadcastPacket( InfoOpcode.INFO_AWAY );
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
