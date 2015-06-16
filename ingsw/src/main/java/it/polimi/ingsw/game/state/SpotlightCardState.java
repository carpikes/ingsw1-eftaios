package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;

import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Spotlight Card
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class SpotlightCardState extends PlayerState {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(SpotlightCardState.class.getName());

    /** Constructor
     *
     * @param state Game State
     */
    public SpotlightCardState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");
        buildAndSendAvailableCommands();
    }

    /** Build and send available commands
     *
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    @Override
    protected void buildAndSendAvailableCommands() {
        ArrayList<ViewCommand> availableCommands = new ArrayList<>();
        availableCommands.add(new ViewCommand(ViewOpcode.CMD_ENABLEMAPVIEW));

        sendAvailableCommands(availableCommands);
    }

    /** Update the game
     *
     * @see it.polimi.ingsw.game.state.PlayerState#update()
     * @return New player state
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

    /** Is the player still in game?
     *
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     * @return True if the player is still in game
     */
    @Override
    public boolean stillInGame() {
        return true;
    }
}
