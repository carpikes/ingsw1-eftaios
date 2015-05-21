/**
 * 
 */
package it.polimi.ingsw.game.player.playerstate;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.command.Command;
import it.polimi.ingsw.game.command.sectorcommand.AttackCommand;

/**
 * @author Michele
 * @since 21 May 2015
 */
public class AttackingState implements PlayerState {
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#attack()
     */
    @Override
    public void attack(GameState gameState) {
        Command attack = new AttackCommand();
        attack.execute(gameState);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#move()
     */
    @Override
    public void move(GameState gameState) {
        throw new IllegalStateOperationException("Cannot move while attacking!");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#drawCard()
     */
    @Override
    public void drawCard(GameState gameState) {
        throw new IllegalStateOperationException("Cannot draw a card while attacking!");
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.playerstate.PlayerState#useObjectCard()
     */
    @Override
    public void useObjectCard(GameState gameState) {
        throw new IllegalStateOperationException("Cannot use a card while attacking!");
    }

}
