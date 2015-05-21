/**
 * 
 */
package it.polimi.ingsw.game.player.playerstate;

import it.polimi.ingsw.game.GameState;

/**
 * @author Michele
 * @since 21 May 2015
 */

public interface PlayerState {
    public void attack(GameState gameState);
    public void move(GameState gameState);
    public void drawCard(GameState gameState);
    public void useObjectCard(GameState gameState);
}
