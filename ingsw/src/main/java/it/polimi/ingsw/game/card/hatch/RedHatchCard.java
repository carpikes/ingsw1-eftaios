package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.NotMyTurnState;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Red Hatch Card. It forces the player to move to EndingTurnState (without winning).
 * @author Michele
 * @since 2 Jun 2015
 */
public class RedHatchCard extends HatchCard {

    public RedHatchCard(GameState state) {
        super(state);
    }

    /** 
     * Return a new EndingTurnState
     */
    @Override
    public PlayerState getNextState() {
        return new NotMyTurnState(mGameState);
    }

}
