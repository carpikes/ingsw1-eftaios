package it.polimi.ingsw.testgame;

import static org.junit.Assert.*;
import it.polimi.ingsw.game.common.PlayerInfo;

import org.junit.Test;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 *
 */
public class TestPlayerInfo {

    @Test
    public void testPlayerInfo() {
        PlayerInfo p = new PlayerInfo("test");
        assertEquals(p.getUsername(),"test");
        p.setNumberOfCards(5);
        assertEquals(p.getNumberOfCards(), 5);
    }
}
