package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

public class DefenseCard extends ObjectCard {

	@Override
	public PlayerState doAction(GameState gameState) {
		// TODO Auto-generated method stub
		return gameState.getCurrentPlayer().getCurrentState();
	}

}
