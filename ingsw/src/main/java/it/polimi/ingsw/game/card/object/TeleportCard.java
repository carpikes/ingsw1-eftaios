package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Teleport Object Card: force player to move to starting position
 * @author Michele
 * @since 2 Jun 2015
 */
public class TeleportCard extends ObjectCard {
    
	public TeleportCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

	/**
	 * Move player to starting point
	 */
    @Override
	public PlayerState doAction() {        
        mGameState.rawMoveTo( mGamePlayer, mGameState.getMap().getStartingPoint(true) );
        return mGamePlayer.getCurrentState();
	}

}
