package it.polimi.ingsw.testgame.sector;

import static org.junit.Assert.*;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import org.junit.Test;

/** Test sectors
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class TestSectors {

    /** Check the exception when asking for an invalid sector */
    @Test(expected=SectorException.class)
    public void testGetSectorFor() {
        SectorBuilder.getSectorFor(-1);
    }
    
    /** Test sectors */
    @Test
    public void testSectors() {
        for(int i = 0; i < SectorBuilder.MAX_COUNT; i++) {
            try {
                Sector s = SectorBuilder.getSectorFor(i);
                assertEquals(s.getId(),i);
                if(s.isCrossable())
                    assertTrue(s.isValid());
            } catch(SectorException e) {
                assertTrue(e.toString().length() > 0);
            }
        }
    }
}
