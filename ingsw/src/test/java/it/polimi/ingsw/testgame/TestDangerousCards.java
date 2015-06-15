package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertFalse;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.dangerous.DangerousCard;
import it.polimi.ingsw.game.card.dangerous.DangerousCardBuilder;

import org.junit.Test;

/** Test Dangerous Cards
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since Jun 13, 2015
 */
public class TestDangerousCards {

    /** Check that there are no errors while generating cards */
    @Test
    public void TestCards() {
        GameState game = new GameState("YES", 0,2,0, true);
        for(int i = 0; i < 50; i ++) {
            DangerousCard c = DangerousCardBuilder.getRandomCard(game);
            assertFalse(c == null);
        }
    }
}
