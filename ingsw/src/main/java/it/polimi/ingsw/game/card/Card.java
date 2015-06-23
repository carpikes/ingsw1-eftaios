/**
 * 
 */
package it.polimi.ingsw.game.card;

import it.polimi.ingsw.game.state.PlayerState;

/** Card tagging interface
 * @author Michele Albanese <michele.albanese93@gmail.com>
 * @since 23 Jun 2015
 */
public interface Card {
    public PlayerState doAction();
}
