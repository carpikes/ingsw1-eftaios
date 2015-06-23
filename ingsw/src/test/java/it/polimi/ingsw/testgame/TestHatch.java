package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.Card;
import it.polimi.ingsw.game.card.CardBuilder;
import it.polimi.ingsw.game.card.hatch.HatchCardBuilder;
import it.polimi.ingsw.game.state.PlayerState;

import org.junit.Test;

/** Test hatches
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class TestHatch {

    /** Test hatches */
    @Test
    public void testHatch() {
        GameState game = new GameState("YES", 1, 2, 0, true);
        CardBuilder cardBuilder = new HatchCardBuilder();
        
        for(int i = 0; i < 50; i++) {
            Card h = cardBuilder.getRandomCard(game);
            PlayerState p = h.doAction();
            assertTrue(p != null);
            assertEquals(p.update(),p);
        }
    }
}
