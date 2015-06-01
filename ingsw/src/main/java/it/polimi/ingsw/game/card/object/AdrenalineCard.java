package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.Human;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.game.state.PlayerState;

public class AdrenalineCard extends ObjectCard {

	@Override
	public PlayerState doAction(GameState gameState) {
		GamePlayer player = gameState.getCurrentPlayer();
		
		if( player.getCurrentState() instanceof MovingState ) {
            player.setAdrenaline(true);
        } else {
            gameState.sendPacketToCurrentPlayer( GameCommand.CMD_SC_ADRENALINE_WRONG_STATE ); 
        }
		
		return player.getCurrentState();
	}

}
