package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.SpotlightCardState;

/** Spotlight Object Card: after choosing a position, tell everyone who is there and in the surrounding sectors
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class SpotlightCard extends ObjectCard {

    /** Constructor
     *
     * @param state Game State
     * @param name Name
     */
    public SpotlightCard(GameState state, String name) {
        super(state, ObjectCardBuilder.SPOTLIGHT_CARD, name);
    }

    /** 
     * Move to SpotlightCardState
     */
    @Override
    public PlayerState doAction() {
        mGamePlayer.setStateBeforeSpotlightCard( mGamePlayer.getCurrentState() );

        return new SpotlightCardState( mGameState );
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
