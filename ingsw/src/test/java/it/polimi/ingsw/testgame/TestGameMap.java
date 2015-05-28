package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/** Test all possible errors in loading files 
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class TestGameMap {
    
    /** Missing file
     * 
     * @throws IOException If there are errors
     */
    @Test(expected=IOException.class)
    public void testCreateFromNonExistentFile() throws IOException {
        GameMap.createFromMapFile( new File("testmaps/allyourbasearebelongtous.txt") );
    }
    
    /** Character not valid 
     * 
     * @throws IOException If there are errors
     */
    @Test(expected=NumberFormatException.class)
    public void testCreateFromFileCharacterNotValid() throws IOException {
        GameMap.createFromMapFile( new File("testmaps/invalid_character.txt") );
    }
    
    /** Too many characters
     * 
     * @throws IOException If there are errors
     */
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testCreateFromFileTooManyLines() throws IOException {
        GameMap.createFromMapFile( new File("testmaps/too_many_lines.txt") );
    }
    
    /** Characters missing 
     * 
     * @throws IOException If there are errors
     */
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