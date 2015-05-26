package it.polimi.ingsw.testgame.sector;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.sector.SectorBuilder;

import org.junit.Test;

/** Test sectors
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class TestSectors {

    @Test(expected=SectorException.class)
    public void testGetSectorFor() {
        SectorBuilder.getSectorFor(-1);
    }

}
