package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Teleport Object Card: force player to move to starting position
 * @author Michele
 * @since 2 Jun 2015
 */
public class TeleportCard extends ObjectCard {

    public TeleportCard(GameState state, String name) {
        super(state, ObjectCardBuilder.TELEPORT_CARD, name);
    }

    /**
     * Move player to starting point
     */
    @Override
    public PlayerState doAction() {        
        mGameState.rawMoveTo( mGamePlayer, mGameState.getMap().getStartingPoint(true) );
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
