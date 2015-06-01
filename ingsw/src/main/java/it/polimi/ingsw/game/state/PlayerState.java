/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 25 May 2015
 */
public abstract class PlayerState {
    protected final GameState mGameState;
    protected final GamePlayer mGamePlayer;
    
    public PlayerState(GameState state, GamePlayer player) {
        mGameState = state;
        mGamePlayer = player;
    } 
    
    public abstract PlayerState update();

	public abstract boolean stillInGame();
}
