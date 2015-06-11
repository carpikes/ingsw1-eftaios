package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/** 
 * Attack Object Card: kill all players in the same sector (same as attack() for an alien)
 * @author Michele
 * @since 2 Jun 2015
 */
public class AttackCard extends ObjectCard {

    public AttackCard(GameState state, String name) {
        super(state, ObjectCardBuilder.ATTACK_CARD, name);
    }

    /** 
     * Invokes attack() on the player's current position
     */
    @Override
    public PlayerState doAction() {
        mGameState.attack( mGamePlayer.getCurrentPosition() );

        return mGamePlayer.getCurrentState();
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.card.object.ObjectCard#isUsable()
     */
    @Override
    public boolean isUsable() {
        return true;
    }

}
