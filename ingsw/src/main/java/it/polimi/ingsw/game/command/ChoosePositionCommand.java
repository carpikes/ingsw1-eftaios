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
public class ChoosePositionCommand implements Command {

    private Point position;
    
    public ChoosePositionCommand(Point destination) {
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
        
        /*return (
                playerState == PlayerState.START_TURN
                ||
                ( playerState == PlayerState.OBJECT_CARD_DRAWN && player.isHuman() ) // for the teleport card (FIXME: maybe a separate state would be better)
                );*/
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#execute(it.polimi.ingsw.game.GameState)
     */
    @Override
    public void execute(GameState gameState) {
        gameState.getCurrentPlayer().setCurrentPosition(position);        
    }

}
