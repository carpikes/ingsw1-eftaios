package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TestGameMap {

    /* Test all possible errors in loading files */
    
    /* Missing file */
    @Test(expected=IOException.class)
    public void testCreateFromNonExistentFile() throws IOException {
        GameMap.createFromMapFile( new File("testmaps/allyourbasearebelongtous.txt") );
    }
    
    /* Character not valid */
    @Test(expected=NumberFormatException.class)
    public void testCreateFromFileCharacterNotValid() throws IOException {
        GameMap.createFromMapFile( new File("testmaps/invalid_character.txt") );
    }
    
    /* Too many characters */
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testCreateFromFileTooManyLines() throws IOException {
        GameMap.createFromMapFile( new File("testmaps/too_many_lines.txt") );
    }
    
    /* Characters missing */
    @Test(expected=SectorException.class)
    public void testCreateFromFileMissingSectors() throws IOException {
        GameMap.createFromMapFile( new File("testmaps/missing_sectors.txt") );
    }
    
    @Test
    public void TestGetSectorAt() throws IOException {
        GameMap g = GameMap.createFromMapFile( new File("maps/fermi.txt") );
        
        assertTrue("First cell in 'Fermi' map is 0.", g.getSectorAt(0, 0).getId() == 0 );
    }
}
