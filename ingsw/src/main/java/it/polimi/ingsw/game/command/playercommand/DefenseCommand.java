/**
 * 
 */
package it.polimi.ingsw.game.command.playercommand;

import it.polimi.ingsw.game.GamePlayer;
import it.polimi.ingsw.game.GameState;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class DefenseCommand implements PlayerCommand {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValidInCurrentContext(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValidInCurrentContext(GameState game) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.playercommand.PlayerCommand#execute(it.polimi.ingsw.game.GamePlayer)
     */
    @Override
    public void execute(GamePlayer player) {
        // TODO Auto-generated method stub

    }

}
