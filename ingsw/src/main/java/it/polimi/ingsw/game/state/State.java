/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;

/**
 * @author Michele
 * @since 25 May 2015
 */
public interface State {
    public State update( GameState gameState );
}
