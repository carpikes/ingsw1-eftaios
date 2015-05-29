package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

public class SedativesCard extends ObjectCard {

	@Override
	public PlayerState doAction(GameState gameState) {
		GamePlayer player = gameState.getCurrentPlayer();
		player.setShouldDrawDangerousCard(false);
		return player.getCurrentState();
	}

}
