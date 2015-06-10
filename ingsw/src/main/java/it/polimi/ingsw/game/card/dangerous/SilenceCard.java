package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.InfoOpcode;
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
    }

	/** 
	 * Send info to current player about the card and then move to Ending Turn State
	 */
    @Override
	public PlayerState doAction() {
        mGameState.broadcastPacket( InfoOpcode.INFO_SILENCE );
        
        return new EndingTurnState(mGameState);
	}

}
