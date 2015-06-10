package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.SpotlightCardState;

/**
 * Spotlight Object Card: after choosing a position, tell everyone who is there and in the surrounding sectors
 * @author Michele
 * @since 2 Jun 2015
 */
public class SpotlightCard extends ObjectCard {

	public SpotlightCard(GameState state, String name) {
        super(state, ObjectCardBuilder.SPOTLIGHT_CARD, name);
        // TODO Auto-generated constructor stub
    }

	/** 
	 * Move to SpotlightCardState
	 */
    @Override
	public PlayerState doAction() {
        mGamePlayer.setStateBeforeSpotlightCard( mGamePlayer.getCurrentState() );
        
        return new SpotlightCardState( mGameState );
	}
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.card.object.ObjectCard#isUsable()
     */
    @Override
    public boolean isUsable() {
        return true;
    }

}
