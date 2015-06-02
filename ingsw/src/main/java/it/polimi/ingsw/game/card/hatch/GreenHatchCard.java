package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.WinnerState;

/**
 * Green Hatch Card. It lets the player win.
 * @author Michele
 * @since 2 Jun 2015
 */
public class GreenHatchCard extends HatchCard {

	public GreenHatchCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

	/**
	 * Set next state to WinnerState
	 */
    @Override
	public PlayerState getNextState() {
	    return new WinnerState(mGameState, mGamePlayer);
	}

}
