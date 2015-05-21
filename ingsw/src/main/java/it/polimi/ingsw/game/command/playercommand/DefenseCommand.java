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
public class DefenseCommand implements Command {
    
    private boolean value;
    
    public DefenseCommand( boolean value ) {
        this.value = value;
    }
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValidInCurrentContext(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
        return gameState.getCurrentPlayer().isDefenseEnable() ^ value; // only one must be true
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.playercommand.PlayerCommand#execute(it.polimi.ingsw.game.GamePlayer)
     */
    @Override
    public void execute(GameState gameState) {
        gameState.getCurrentPlayer().setDefense(value);
    }

}
