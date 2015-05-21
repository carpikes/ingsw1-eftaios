/**
 * 
 */
package it.polimi.ingsw.game.command.sectorcommand;

import it.polimi.ingsw.game.command.Command;
import it.polimi.ingsw.game.sector.Sector;

/**
 * @author Michele
 * @since 21 May 2015
 */
public interface SectorCommand extends Command {
       public void execute( Sector sector );
}
