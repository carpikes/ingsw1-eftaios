package it.polimi.ingsw.game.card;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

import java.awt.Point;

public class TeleportCard extends ObjectCard {

	@Override
	public PlayerState doAction(GameState gameState) {
		GamePlayer player = gameState.getCurrentPlayer();
		
		// TODO set correct sector
        Point humanSector = null;
        
        gameState.moveTo( humanSector, null );
        return player.getCurrentState();
	}

}
