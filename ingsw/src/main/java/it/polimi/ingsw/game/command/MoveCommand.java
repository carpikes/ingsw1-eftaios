/**
 * 
 */
package it.polimi.ingsw.game.command;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.PlayerState;

import java.awt.Point;

/** 
 * @author Michele
 * @since 23 May 2015
 */
public class MoveCommand implements Command {

    private Point position;
    
    public MoveCommand(Point destination) {
        position = destination;
    }
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValid(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
        GamePlayer player = gameState.getCurrentPlayer();
        PlayerState playerState = player.getCurrentState();
        
        return true;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#execute(it.polimi.ingsw.game.GameState)
     */
    @Override
    public void execute(GameState gameState) {
        gameState.getCurrentPlayer().setCurrentPosition(position);        
    }

}
