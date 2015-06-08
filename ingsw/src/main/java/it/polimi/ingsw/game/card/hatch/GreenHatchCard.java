package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.WinnerState;

/**
 * Green Hatch Card. It lets the player win.
 * @author Michele
 * @since 2 Jun 2015
 */
public class GreenHatchCard extends HatchCard {

    private static final long serialVersionUID = 1L;

    public GreenHatchCard(GameState state) {
        super(state);
        // TODO Auto-generated constructor stub
    }

	/**
	 * Set next state to WinnerState
	 */
    @Override
	public PlayerState getNextState() {
	    return new WinnerState(mGameState, mGamePlayer.getId());
	}

}
