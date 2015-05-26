/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;

/**
 * @author Michele
 * @since 25 May 2015
 */
public abstract class PlayerState {
    protected final GameState gameState;
    
    public PlayerState(GameState state) {
        gameState = state;
    } 
    
    public abstract PlayerState update();
}
