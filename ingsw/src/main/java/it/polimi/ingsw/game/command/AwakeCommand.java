/**
 * 
 */
package it.polimi.ingsw.game.command;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.Alien;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.PlayerState;
import it.polimi.ingsw.game.player.Role;

import java.awt.Point;

/**
 * @author Michele
 * @since 23 May 2015
 */
public class AwakeCommand implements Command {
    
    private Point position;
    
    public AwakeCommand( Point where ) {
        position = where;
    }
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValid(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
        GamePlayer player = gameState.getCurrentPlayer();
        PlayerState playerState = player.getCurrentState();
        
        return(
                ( playerState == PlayerState.ATTACKED && player.isAlien() )
                ||
                ( playerState == PlayerState.OBJECT_CARD_DRAWN && player.isHuman() )
              );        
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#execute(it.polimi.ingsw.game.GameState)
     */
    @Override
    public void execute(GameState gameState) {
        for( GamePlayer p : gameState.getPlayers() )
            if( p.getCurrentPosition().equals(position) )
                p.setCurrentState(PlayerState.LOSER);
    }

}
