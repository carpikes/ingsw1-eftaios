package it.polimi.ingsw.testgame;

import static org.junit.Assert.*;

import java.io.IOException;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.PlayerInfo;
import it.polimi.ingsw.game.common.Rand;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;

import org.junit.Test;

/** Some misc tests
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since Jun 13, 2015
 */
public class TestMiscFeatures {

    /** Test if PlayerInfo is working as expected */
    @Test
    public void testPlayerInfo() {
        PlayerInfo p = new PlayerInfo("test");
        assertEquals(p.getUsername(),"test");
        p.setNumberOfCards(5);
        assertEquals(p.getNumberOfCards(), 5);
    }
    
    /** Test if ViewOpcode and ViewCommand are working as expected */
    @Test
    public void testViewOpcode() {
        ViewOpcode v = ViewOpcode.CMD_ATTACK;
        ViewCommand c = new ViewCommand(v);
        assertEquals(c.getOpcode(),v);
        assertEquals(c.getArgs().length, 0);
    }
    
    /** Test if GameInfo is working as expected */
    @Test
    public void testGameInfo() throws IOException {
        GameMap map = GameMap.createFromId(1);
        GameInfo g = new GameInfo(new PlayerInfo[0], 0, true, map);
        assertEquals(g.getPlayersList().length, 0);
        assertEquals(g.getMap(), map);
        assertEquals(g.getId(), 0);
        assertEquals(g.isHuman(), true);
        assertFalse(g.getListOfCards() == null);
    }
    
    /** Test the Rand class */
    @Test
    public void testRandomGenerator() {
        for(int i = 0;i<100;i++) {
            assertTrue(Rand.nextInt(10) < 10);
            assertTrue((Rand.nextInt() % 10) < 10);
        }
    }
}
