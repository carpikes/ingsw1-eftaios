package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Defense Object Card: Set defense of the player to true when constructed. It is never used directly via doAction() method.
 * @author Michele
 * @since 2 Jun 2015
 */
public class DefenseCard extends ObjectCard {

    public DefenseCard(GameState state, String name) {
        super(state, ObjectCardBuilder.DEFENSE_CARD, name);

        mGamePlayer.setDefense(true);
    }

    /** 
     * Do nothing.
     */
    @Override
    public PlayerState doAction() {
        return null;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.card.object.ObjectCard#isUsable()
     */
    @Override
    public boolean isUsable() {
        return false;
    }
}
