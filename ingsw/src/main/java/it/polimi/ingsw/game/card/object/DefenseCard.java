package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/** Defense Object Card: Set defense of the player to true when constructed. 
 * It is never used directly via doAction() method.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class DefenseCard extends ObjectCard {

    /** Constructor
     *
     * @param state Game State
     * @param name Name
     */
    public DefenseCard(GameState state, String name) {
        super(state, ObjectCardBuilder.DEFENSE_CARD, name);

        mGamePlayer.setDefense(true);
    }

    /** Do nothing. */
    @Override
    public PlayerState doAction() {
        return null;
    }

    /** Is this card usable?
     * 
     * @see it.polimi.ingsw.game.card.object.ObjectCard#isUsable()
     * @return True if is usable
     */
    @Override
    public boolean isUsable() {
        return false;
    }
}
