package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.WinnerState;

public class RedHatchCard extends HatchCard {

	@Override
	public PlayerState getNextState(GameState gameState) {
	    return new EndingTurnState(gameState);
	}

}
