package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/** Teleport Object Card: force player to move to starting position
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class TeleportCard extends ObjectCard {

    /** Constructor
     *
     * @param state Game State
     * @param name Name
     */
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
