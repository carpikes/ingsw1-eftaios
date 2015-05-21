/**
 * 
 */
package it.polimi.ingsw.game.command.playercommand;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.command.Command;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class SetCurrentPlayerCommand implements Command {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValidInCurrentContext(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.playercommand.PlayerCommand#execute(it.polimi.ingsw.game.GamePlayer)
     */
    @Override
    public void execute(GameState gameState) {
        // TODO Auto-generated method stub

    }

}
