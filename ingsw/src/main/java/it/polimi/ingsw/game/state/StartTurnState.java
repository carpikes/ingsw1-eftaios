package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.InfoOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Start Turn State
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class StartTurnState extends PlayerState {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(StartTurnState.class.getName());

    /** Constructor
     *
     * @param state Game State
     */
    public StartTurnState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");
        
        mGamePlayer.resetValues();
        
        /** tell everybody I'm starting playing! */
        state.broadcastPacket( new GameCommand(InfoOpcode.INFO_START_TURN, state.getTurnId()) );
    }

    /** Update
     * @see it.polimi.ingsw.game.state.State#update()
     * 
     * @return New State
     */
    @Override
    public PlayerState update() {       
        return new MovingState(mGameState);
    }

    /** Is the player still in game
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     *
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
