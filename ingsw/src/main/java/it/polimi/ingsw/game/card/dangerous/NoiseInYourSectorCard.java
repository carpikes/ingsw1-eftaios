package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

public class NoiseInYourSectorCard extends DangerousCard {

	public NoiseInYourSectorCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

    @Override
	public PlayerState doAction() {	    
	    mGameState.sendPacketToCurrentPlayer( new GameCommand(GameOpcode.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardBuilder.NOISE_IN_YOUR_SECTOR) );
        mGameState.broadcastPacket( new GameCommand(GameOpcode.INFO_NOISE, mGamePlayer.getCurrentPosition()) );
        
        return mGameState.getObjectCard( );
	}

}
