package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.SpotlightCardState;

public class SpotlightCard extends ObjectCard {

	@Override
	public PlayerState doAction(GameState gameState) {
		// another possibility here: add a second argument with the desired position, in order to make it all in one state
		GamePlayer player = gameState.getCurrentPlayer();
        player.setStateBeforeSpotlightCard( player.getCurrentState() );
        
        return new SpotlightCardState( gameState, gameState.getCurrentPlayer() );
	}

}
