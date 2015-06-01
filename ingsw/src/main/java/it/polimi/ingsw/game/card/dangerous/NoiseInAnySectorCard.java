package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.NoiseInAnySectorState;
import it.polimi.ingsw.game.state.PlayerState;

public class NoiseInAnySectorCard extends DangerousCard {

	@Override
	public PlayerState doAction(GameState gameState) {
	    gameState.sendPacketToCurrentPlayer(new NetworkPacket(GameCommand.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.NOISE_IN_ANY_SECTOR) );
        return new NoiseInAnySectorState(gameState, gameState.getCurrentPlayer());
	}

}
