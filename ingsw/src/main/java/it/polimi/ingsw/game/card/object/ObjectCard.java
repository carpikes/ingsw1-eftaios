/**
 * 
 */
package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

import java.io.Serializable;


/**
 * @author Michele
 * @since 23 May 2015
 */
public abstract class ObjectCard implements Serializable {

    protected GameState mGameState;
    protected GamePlayer mGamePlayer;
    
    protected ObjectCard(GameState state, GamePlayer player) {
        mGameState = state;
        mGamePlayer = player;
    } 
	
	public abstract PlayerState doAction();
}
