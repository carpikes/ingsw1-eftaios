package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

/** 
 * Attack Object Card: kill all players in the same sector (same as attack() for an alien)
 * @author Michele
 * @since 2 Jun 2015
 */
public class AttackCard extends ObjectCard {

	public AttackCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

	/** 
	 * Invokes attack() on the player's current position
	 */
    @Override
	public PlayerState doAction() {
		mGameState.attack( mGamePlayer.getCurrentPosition() );
		
		return mGamePlayer.getCurrentState();
	}

}
