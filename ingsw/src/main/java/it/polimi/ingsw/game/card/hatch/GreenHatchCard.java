package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.GameState.LastThings;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.WinnerState;

/**
 * Green Hatch Card. It lets the player win.
 * @author Michele
 * @since 2 Jun 2015
 */
public class GreenHatchCard extends HatchCard {

    public GreenHatchCard(GameState state) {
        super(state);
    }

    /**
     * Set next state to WinnerState
     */
    @Override
    public PlayerState getNextState() {
        mGameState.setLastThingDid(LastThings.HUMAN_USED_HATCH);
        return new WinnerState(mGameState, mGamePlayer.getId());
    }

}
