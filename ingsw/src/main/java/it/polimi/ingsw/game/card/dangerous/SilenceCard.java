package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.PlayerState;

public class SilenceCard extends DangerousCard {

	@Override
	public PlayerState doAction(GameState gameState) {
	    GamePlayer player = gameState.getCurrentPlayer();
	    
	    player.sendPacket( new NetworkPacket(GameCommand.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.SILENCE) );
        gameState.addToOutputQueue( GameCommand.INFO_SILENCE );
        
        return new EndingTurnState(gameState);
	}

}
