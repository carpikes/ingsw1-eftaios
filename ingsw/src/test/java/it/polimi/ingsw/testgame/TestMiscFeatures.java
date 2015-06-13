package it.polimi.ingsw.testgame;

import static org.junit.Assert.*;

import java.io.IOException;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.PlayerInfo;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;

import org.junit.Test;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 *
 */
public class TestMiscFeatures {

    @Test
    public void testPlayerInfo() {
        PlayerInfo p = new PlayerInfo("test");
        assertEquals(p.getUsername(),"test");
        p.setNumberOfCards(5);
        assertEquals(p.getNumberOfCards(), 5);
    }
    
    @Test
    public void testViewOpcode() {
        ViewOpcode v = ViewOpcode.CMD_ATTACK;
        ViewCommand c = new ViewCommand(v);
        assertEquals(c.getOpcode(),v);
        assertEquals(c.getArgs().length, 0);
    }
    
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
}
