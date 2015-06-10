package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Sedatives Object Card: force player NOT to draw a dangerous card during this turn
 * @author Michele
 * @since 2 Jun 2015
 */
public class SedativesCard extends ObjectCard {
    
	public SedativesCard(GameState state, String name) {
        super(state, ObjectCardBuilder.SEDATIVES_CARD, name);
        // TODO Auto-generated constructor stub
    }

	/**
	 * Force player NOT to draw a dangerous card during this turn
	 */
    @Override
	public PlayerState doAction() {
		mGamePlayer.setShouldDrawDangerousCard(false);
		return mGamePlayer.getCurrentState();
	}
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.card.object.ObjectCard#isUsable()
     */
    @Override
    public boolean isUsable() {
        return true;
    }

}
