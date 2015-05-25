/**
 * 
 */
package it.polimi.ingsw.game.command;

import it.polimi.ingsw.game.GameState;


/**
 * @author Michele
 * @since  May 21, 2015
 */

public interface Command {
    void execute( GameState gameState );
}
