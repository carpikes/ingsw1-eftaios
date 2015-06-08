package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.NoiseInAnySectorState;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Noise in any sector Dangerous Card. Tell everyone a position (noise in any sector)  
 * @author Michele
 * @since 2 Jun 2015
 */
public class NoiseInAnySectorCard extends DangerousCard {

	public NoiseInAnySectorCard(GameState state) {
        super(state);
    }

	/** 
	 * Send info to current player about the card and then move to NoiseInAnySectorState for next interactions with the user
	 */
    @Override
	public PlayerState doAction() {
	    mGameState.sendPacketToCurrentPlayer(new GameCommand(GameOpcode.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardBuilder.NOISE_IN_ANY_SECTOR) );
        return new NoiseInAnySectorState(mGameState);
	}

}
