package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class EndingTurnState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(EndingTurnState.class.getName());

    public EndingTurnState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        buildAndSendAvailableCommands();
    }

    /** Build and send available commands
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    @Override
    protected void buildAndSendAvailableCommands() {
        ArrayList<ViewCommand> availableCommands = new ArrayList<>();
        availableCommands.add(new ViewCommand(ViewOpcode.CMD_ENDTURN));

        addObjectCardIfPossible(availableCommands);
        sendAvailableCommands(availableCommands);
    }

    /** Update the game
     * @see it.polimi.ingsw.game.state.State#update()
     * @return New player state
     */
    @Override
    public PlayerState update() {
        GameCommand packet = mGameState.getPacketFromQueue();

        PlayerState nextState = this;
        if( packet != null ) {
            if( packet.getOpcode() == GameOpcode.CMD_CS_END_TURN )
                nextState = new NotMyTurnState(mGameState);
            else if( packet.getOpcode() == GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD  && mGamePlayer.getNumberOfCards() > 0) 
                nextState = useObjectCard(this, packet);
            else
                throw new IllegalStateOperationException("You can only use an object card or end here. Discarding packet.");
        }

        return nextState;
    }


    /** Is the player still in game?
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     * @return True if the player is still in game
     */
    @Override
    public boolean stillInGame() {
        return true;
    }
}
