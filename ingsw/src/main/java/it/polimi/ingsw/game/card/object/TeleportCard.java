package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

import java.awt.Point;

public class TeleportCard extends ObjectCard {
    
	public TeleportCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

    @Override
	public PlayerState doAction() {
		// TODO set correct sector
        Point humanSector = null;
        
        mGameState.rawMoveTo( humanSector, null );
        return mGamePlayer.getCurrentState();
	}

}
