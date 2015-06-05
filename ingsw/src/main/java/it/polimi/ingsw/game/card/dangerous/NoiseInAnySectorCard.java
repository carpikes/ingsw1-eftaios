package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.NoiseInAnySectorState;
import it.polimi.ingsw.game.state.PlayerState;

public class NoiseInAnySectorCard extends DangerousCard {

	public NoiseInAnySectorCard(GameState state, GamePlayer player) {
        super(state, player);
    }

    @Override
	public PlayerState doAction() {
	    mGameState.sendPacketToCurrentPlayer(new GameCommand(GameOpcode.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardBuilder.NOISE_IN_ANY_SECTOR) );
        return new NoiseInAnySectorState(mGameState, mGamePlayer);
	}

}
