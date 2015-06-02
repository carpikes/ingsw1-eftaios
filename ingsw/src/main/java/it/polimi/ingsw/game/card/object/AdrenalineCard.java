package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.game.state.PlayerState;

public class AdrenalineCard extends ObjectCard {

	public AdrenalineCard(GameState state, GamePlayer player) {
        super(state, player);
        // TODO Auto-generated constructor stub
    }

    @Override
	public PlayerState doAction() {
	
		if( mGamePlayer.getCurrentState() instanceof MovingState ) {
		    mGamePlayer.setAdrenaline(true);
        } else {
            mGameState.sendPacketToCurrentPlayer( GameCommand.CMD_SC_ADRENALINE_WRONG_STATE ); 
        }
		
		return mGamePlayer.getCurrentState();
	}

}
