package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/** Sedatives Object Card: force player NOT to draw a dangerous card during this turn
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class SedativesCard extends ObjectCard {

    /** Constructor
     *
     * @param state Game State
     * @param name Name
     */
    public SedativesCard(GameState state, String name) {
        super(state, ObjectCardBuilder.SEDATIVES_CARD, name);
    }

    /**
     * Force player NOT to draw a dangerous card during this turn
     */
    @Override
    public PlayerState doAction() {
        mGamePlayer.setShouldDrawDangerousCard(false);
        return mGamePlayer.getCurrentState();
    }

    /** Is this card usable?
     * @see it.polimi.ingsw.game.card.object.ObjectCard#isUsable()
     *
     * @return True if is usable
     */
    @Override
    public boolean isUsable() {
        return true;
    }

}
