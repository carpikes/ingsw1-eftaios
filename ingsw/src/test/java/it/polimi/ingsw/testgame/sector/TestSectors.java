package it.polimi.ingsw.testgame.sector;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.sector.Sectors;

import org.junit.Test;

public class TestSectors {

    @Test(expected=SectorException.class)
    public void testGetSectorFor() {
        Sectors.getSectorFor(-1);
    }

}
