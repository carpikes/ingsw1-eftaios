package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Not my turn State
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class NotMyTurnState extends PlayerState {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(NotMyTurnState.class.getName());

    /** Constructor
     *
     * @param state Game State
     */
    public NotMyTurnState(GameState state) {
        super(state);

        LOG.log(Level.FINE, "Constructor");
    }

    /** Update the game
     * @see it.polimi.ingsw.game.state.PlayerState#update()
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
        return true;
    }

    /** Build and send available commands
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    @Override
    protected void buildAndSendAvailableCommands() {
        /** no commands */
    }

}
