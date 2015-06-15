package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;
import it.polimi.ingsw.game.player.GamePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Player state abstract class
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public abstract class PlayerState {
    /** GameState */
    protected final GameState mGameState;

    /** GamePlayer */
    protected final GamePlayer mGamePlayer;

    /** Constructor
     *
     * @param state GameState
     */
    protected PlayerState(GameState state) {
        mGameState = state;
        mGamePlayer = state.getCurrentPlayer();
    } 

    /** Send available commands to the player
     *
     * @param availableCommands Commands to send
     */
    protected void sendAvailableCommands(ArrayList<ViewCommand> availableCommands) {
        mGameState.sendPacketToCurrentPlayer(new GameCommand(GameOpcode.CMD_SC_AVAILABLE_COMMANDS, availableCommands));
    }

    /** Update the game */
    public abstract PlayerState update();

    /** Is the player still in game? */
    public abstract boolean stillInGame();

    /** Build and send available commands */
    protected abstract void buildAndSendAvailableCommands();

    /** Use an object card
     * @param curState current state
     * @param cmd Command
     * @return New player state
     */
    protected PlayerState useObjectCard(PlayerState curState, GameCommand cmd) {
        PlayerState nextState;
        if(cmd == null || cmd.getArgs() == null || cmd.getArgs().length == 0 || !(cmd.getArgs()[0] instanceof Integer))
            throw new IllegalStateOperationException("Which object card?");

        nextState = mGameState.startUsingObjectCard( (Integer)cmd.getArgs()[0] );

        if(nextState.equals(curState))
            buildAndSendAvailableCommands();
        return nextState;
    }

    /** Add an object card to the availableCommands
     *
     * @param availableCommands Input array
     */
    protected void addObjectCardIfPossible(List<ViewCommand> availableCommands) {
        if(mGamePlayer.isObjectCardUsed() || mGamePlayer.getNumberOfUsableCards() == 0 || !mGamePlayer.isHuman())
            return;

        availableCommands.add(new ViewCommand(ViewOpcode.CMD_CHOOSEOBJECTCARD, (Serializable[]) mGamePlayer.getNamesOfUsableCards()));
    }
}
