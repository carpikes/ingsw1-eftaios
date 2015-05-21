/**
 * 
 */
package it.polimi.ingsw.game.command.sectorcommand;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.sector.Sector;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class SetInaccessibleSectorCommand implements SectorCommand {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValidInCurrentContext(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValidInCurrentContext(GameState game) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.sectorcommand.SectorCommand#execute(it.polimi.ingsw.game.sector.Sector)
     */
    @Override
    public void execute(Sector sector) {
        // TODO Auto-generated method stub

    }

}
