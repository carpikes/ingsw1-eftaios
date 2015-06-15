package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/** Adrenaline Object card: the player can move up to 2 sectors during this turn 
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class AdrenalineCard extends ObjectCard {

    /** Constructor
     *
     * @param state Game State
     * @param name Name
     */
    public AdrenalineCard(GameState state, String name) {
        super(state, ObjectCardBuilder.ADRENALINE_CARD, name);
    }

    /** 
     * Set adrenaline to true to the set player
     */
    @Override
    public PlayerState doAction() {
        mGamePlayer.setAdrenaline(true);

        return mGamePlayer.getCurrentState();
    }

    /** Is Usable?
     * @see it.polimi.ingsw.game.card.object.ObjectCard#isUsable()
     * @return True if is usable
     */
    @Override
    public boolean isUsable() {
        return true;
    }
}
