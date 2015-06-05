package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.SpotlightCardState;

/**
 * Spotlight Object Card: after choosing a position, tell everyone who is there and in the surrounding sectors
 * @author Michele
 * @since 2 Jun 2015
 */
public class SpotlightCard extends ObjectCard {

	public SpotlightCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

	/** 
	 * Move to SpotlightCardState
	 */
    @Override
	public PlayerState doAction() {
        mGamePlayer.setStateBeforeSpotlightCard( mGamePlayer.getCurrentState() );
        
        return new SpotlightCardState( mGameState, mGamePlayer );
	}

}
