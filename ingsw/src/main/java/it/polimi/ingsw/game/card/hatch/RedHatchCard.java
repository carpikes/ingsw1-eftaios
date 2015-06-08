package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.WinnerState;

/**
 * Red Hatch Card. It forces the player to move to EndingTurnState (without winning).
 * @author Michele
 * @since 2 Jun 2015
 */
public class RedHatchCard extends HatchCard {

	public RedHatchCard(GameState state) {
        super(state);
        // TODO Auto-generated constructor stub
    }

	/** 
	 * Return a new EndingTurnState
	 */
    @Override
	public PlayerState getNextState() {
	    return new EndingTurnState(mGameState);
	}

}
