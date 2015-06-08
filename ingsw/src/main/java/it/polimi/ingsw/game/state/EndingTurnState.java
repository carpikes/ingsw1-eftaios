/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.network.GameViewOpcode;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class EndingTurnState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(EndingTurnState.class.getName());

    public EndingTurnState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        mGameState.sendPacketToCurrentPlayer( GameOpcode.CMD_SC_END_OF_TURN );
        buildAndSendAvailableCommands();
    }

    @Override
    protected void buildAndSendAvailableCommands() {
        ArrayList<GameViewCommand> availableCommands = new ArrayList<>();
        availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_ENDTURN));

        addObjectCardIfPossible(availableCommands);
        sendAvailableCommands(availableCommands);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
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


    @Override
    public boolean stillInGame() {
        return true;
    }
}
