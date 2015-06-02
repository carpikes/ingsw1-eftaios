package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

public class DefenseCard extends ObjectCard {

	public DefenseCard(GameState state, GamePlayer player) {
        super(state, player);
        
        player.setDefense(true);
    }

    @Override
	public PlayerState doAction() {
            
		return null;
	}

}
