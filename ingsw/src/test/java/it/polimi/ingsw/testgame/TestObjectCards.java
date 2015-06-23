package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertEquals;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.CardBuilder;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;
import it.polimi.ingsw.game.state.AwayState;

import org.junit.Test;

/** Test if object cards are valid
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class TestObjectCards {

    /** For each possible object card, check that all things (name, id, ...) are working */
    @Test
    public void testCardNames() {
        GameState game = new GameState("YES", 1, 4, 0, true);
        CardBuilder objCardBuilder = new ObjectCardBuilder();
        game.getCurrentPlayer().setCurrentState(new AwayState(game));
        
        for(int i = 0; i < ObjectCardBuilder.OBJECT_CARD_TYPES; i++) {
            ObjectCard c = (ObjectCard)objCardBuilder.getCard(game, i);
            assertEquals(c.getName(),ObjectCardBuilder.idToString(i));
            boolean b1 = c.doAction() != null;
            boolean b2 = c.isUsable();
            assertEquals(b1, b2);
        }
        
    }
}
