/**
 * 
 */
package it.polimi.ingsw.game.player.playerstate;

/**
 * @author Michele
 * @since 21 May 2015
 */

public interface PlayerState {
    public void attack();
    public void move();
    public void drawCard();
    public void useObjectCard();
}
