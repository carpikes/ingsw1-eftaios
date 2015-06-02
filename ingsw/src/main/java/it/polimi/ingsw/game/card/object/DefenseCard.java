package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

/**
 * Defense Object Card: Set defense of the player to true when constructed. It is never used directly via doAction() method.
 * @author Michele
 * @since 2 Jun 2015
 */
public class DefenseCard extends ObjectCard {

	public DefenseCard(GameState state, GamePlayer player) {
        super(state, player);
        
        player.setDefense(true);
    }

	/** 
	 * Do nothing.
	 */
    @Override
	public PlayerState doAction() {
		return null;
	}

}
