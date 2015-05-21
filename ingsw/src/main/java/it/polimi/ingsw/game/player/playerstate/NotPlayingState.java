/**
 * 
 */
package it.polimi.ingsw.game.player.playerstate;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class NotPlayingState implements PlayerState {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#attack()
     */
    @Override
    public void attack(GameState gameState) {
        throw new IllegalStateOperationException("Cannot attack while not playing!");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#move()
     */
    @Override
    public void move(GameState gameState) {
        throw new IllegalStateOperationException("Cannot move while not playing!");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#drawCard()
     */
    @Override
    public void drawCard(GameState gameState) {
        throw new IllegalStateOperationException("Cannot draw any cards while not playing!");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#useObjectCard()
     */
    @Override
    public void useObjectCard(GameState gameState) {
        throw new IllegalStateOperationException("Cannot use any cards while not playing!");
    }

}
