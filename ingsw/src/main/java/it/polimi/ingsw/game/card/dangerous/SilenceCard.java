package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.PlayerState;

public class SilenceCard extends DangerousCard {

	public SilenceCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

    @Override
	public PlayerState doAction() {
	    mGameState.sendPacketToCurrentPlayer( new NetworkPacket(GameCommand.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardBuilder.SILENCE) );
        mGameState.broadcastPacket( GameCommand.INFO_SILENCE );
        
        return new EndingTurnState(mGameState, mGamePlayer);
	}

}
