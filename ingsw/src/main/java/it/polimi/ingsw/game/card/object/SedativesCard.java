package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Sedatives Object Card: force player NOT to draw a dangerous card during this turn
 * @author Michele
 * @since 2 Jun 2015
 */
public class SedativesCard extends ObjectCard {
    
	public SedativesCard(GameState state, GamePlayer player) {
        super(state, player, ObjectCardBuilder.SEDATIVES_CARD, "Sedatives");
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

}