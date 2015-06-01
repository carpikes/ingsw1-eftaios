package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

public class NoiseInYourSectorCard extends DangerousCard {

	@Override
	public PlayerState doAction(GameState gameState) {
	    GamePlayer player = gameState.getCurrentPlayer();
	    
	    player.sendPacket( new NetworkPacket(GameCommand.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.NOISE_IN_YOUR_SECTOR) );
        gameState.addToOutputQueue( new NetworkPacket(GameCommand.INFO_NOISE, player.getCurrentPosition()) );
        
        return gameState.getObjectCard( );
	}

}
