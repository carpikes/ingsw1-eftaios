package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.hatch.HatchCard;
import it.polimi.ingsw.game.card.hatch.HatchCardBuilder;
import it.polimi.ingsw.game.state.PlayerState;

import org.junit.Test;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 *
 */
public class TestHatch {

    @Test
    public void testHatch() {
        GameState game = new GameState("YES", 1, 2, 0, true);
        for(int i = 0; i < 50; i++) {
            HatchCard h = HatchCardBuilder.getRandomCard(game);
            PlayerState p = h.getNextState();
            assertTrue(p != null);
            assertEquals(p.update(),p);
        }
    }
}
