package it.polimi.ingsw.game.card;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

public class AttackCard extends ObjectCard {

	@Override
	public PlayerState doAction(GameState gameState) {
		GamePlayer player = gameState.getCurrentPlayer();
		gameState.attack( player.getCurrentPosition() );
		
		return player.getCurrentState();
	}

}
