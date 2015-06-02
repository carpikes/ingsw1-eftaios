package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

public class TeleportCard extends ObjectCard {
    
	public TeleportCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

    @Override
	public PlayerState doAction() {        
        mGameState.rawMoveTo( mGameState.getMap().getStartingPoint(true), null );
        return mGamePlayer.getCurrentState();
	}

}
