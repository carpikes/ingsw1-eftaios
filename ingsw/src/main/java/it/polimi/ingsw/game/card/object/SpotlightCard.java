package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.SpotlightCardState;

public class SpotlightCard extends ObjectCard {

	public SpotlightCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

    @Override
	public PlayerState doAction() {
		// another possibility here: add a second argument with the desired position, in order to make it all in one state
        mGamePlayer.setStateBeforeSpotlightCard( mGamePlayer.getCurrentState() );
        
        return new SpotlightCardState( mGameState, mGamePlayer );
	}

}
