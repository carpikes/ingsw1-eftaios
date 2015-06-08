package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Silence dangerous card. Just say "SILENCE" and go on to {@link EndingTurnState}.
 * @author Michele
 * @since 2 Jun 2015
 */
public class SilenceCard extends DangerousCard {

	public SilenceCard(GameState state) {
        super(state);
        // TODO Auto-generated constructor stub
    }

	/** 
	 * Send info to current player about the card and then move to Ending Turn State
	 */
    @Override
	public PlayerState doAction() {
	    mGameState.sendPacketToCurrentPlayer( new GameCommand(GameOpcode.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardBuilder.SILENCE) );
        mGameState.broadcastPacket( GameOpcode.INFO_SILENCE );
        
        return new EndingTurnState(mGameState);
	}

}
