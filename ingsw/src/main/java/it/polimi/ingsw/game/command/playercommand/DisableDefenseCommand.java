/**
 * 
 */
package it.polimi.ingsw.game.command.playercommand;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.command.Command;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class DisableDefenseCommand implements Command {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValidInCurrentContext(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
      return false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.playercommand.PlayerCommand#execute(it.polimi.ingsw.game.GamePlayer)
     */
    @Override
    public void execute(GameState gameState) {
        
    }

}
