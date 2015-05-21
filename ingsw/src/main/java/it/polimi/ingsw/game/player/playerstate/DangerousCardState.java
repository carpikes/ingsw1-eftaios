/**
 * 
 */
package it.polimi.ingsw.game.player.playerstate;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.Card;
import it.polimi.ingsw.game.command.Command;
import it.polimi.ingsw.game.command.playercommand.DrawCardCommand;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class DangerousCardState implements PlayerState {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#attack()
     */
    @Override
    public void attack(GameState gameState) {
        throw new IllegalStateOperationException("Cannot attack while drawing a dangerous card!");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#move()
     */
    @Override
    public void move(GameState gameState) {
        throw new IllegalStateOperationException("Cannot move while drawing a dangerous card!");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#drawCard()
     */
    @Override
    public void drawCard(GameState gameState) {
        // FIXME carte diverse!
        Command command = new DrawCardCommand( new Card() );
        command.execute(gameState);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#useObjectCard()
     */
    @Override
    public void useObjectCard(GameState gameState) {
        throw new IllegalStateOperationException("Cannot use an object card while drawing a dangerous one!");
    }

}
