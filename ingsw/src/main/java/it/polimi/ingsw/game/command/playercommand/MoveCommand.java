/**
 * 
 */
package it.polimi.ingsw.game.command.playercommand;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.command.Command;
import it.polimi.ingsw.game.player.GamePlayer;

import java.awt.Point;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class MoveCommand implements Command {

    Point destination;
    
    public MoveCommand( Point dest ) {
        destination = dest;
    }
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValidInCurrentContext(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
        GameMap map = gameState.getMap();
        GamePlayer player = gameState.getCurrentPlayer();
        
        int distance = map.distance( player.getCurrentPosition(), destination );
        return( map.isWithinBounds(destination) && player.isValidDistance(distance) && map.getSectorAt(destination).isValid() );
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.playercommand.PlayerCommand#execute(it.polimi.ingsw.game.GamePlayer)
     */
    @Override
    public void execute(GameState gameState) {
        if( isValid( gameState ) )
            gameState.getCurrentPlayer().setCurrentPosition( destination );
    }

}
