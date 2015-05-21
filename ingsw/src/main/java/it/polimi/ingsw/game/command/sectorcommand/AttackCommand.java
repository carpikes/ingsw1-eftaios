/**
 * 
 */
package it.polimi.ingsw.game.command.sectorcommand;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.command.Command;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class AttackCommand implements Command {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValidInCurrentContext(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.sectorcommand.SectorCommand#execute(it.polimi.ingsw.game.sector.Sector)
     */
    @Override
    public void execute(GameState gameState) {
        // TODO Auto-generated method stub

    }

}
