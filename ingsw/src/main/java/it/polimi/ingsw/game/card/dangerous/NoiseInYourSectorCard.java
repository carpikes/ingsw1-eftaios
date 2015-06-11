package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.InfoOpcode;
import it.polimi.ingsw.game.state.PlayerState;

/**
 *  Noise in your sector Dangerous Card. Tell everyone your position
 * @author Michele
 * @since 2 Jun 2015
 */
public class NoiseInYourSectorCard extends DangerousCard {

	public NoiseInYourSectorCard(GameState state) {
        super(state);
    }

	/**
	 * Send info to current player about the card and then get a new object card
	 */
    @Override
	public PlayerState doAction() { 
        mGameState.broadcastPacket( new GameCommand(InfoOpcode.INFO_NOISE, mGamePlayer.getCurrentPosition()) );
        
        return mGameState.getObjectCard( );
	}

}
