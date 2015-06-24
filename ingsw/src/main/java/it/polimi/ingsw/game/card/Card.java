package it.polimi.ingsw.game.card;

import it.polimi.ingsw.game.state.PlayerState;

/** Card tagging interface
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 23 Jun 2015
 */
public interface Card {
    public PlayerState doAction();
}
