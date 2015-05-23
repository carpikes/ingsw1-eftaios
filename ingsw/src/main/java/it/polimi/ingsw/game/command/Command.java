/**
 * 
 */
package it.polimi.ingsw.game.command;

import it.polimi.ingsw.game.GameState;


/**
 * @author Michele
 * @since  May 21, 2015
 */

public abstract class Command {
    public abstract boolean isValid( GameState gameState );
    public abstract void execute( GameState gameState );
    
    protected abstract boolean checkCurrentPlayerState(GameState gameState);
}
