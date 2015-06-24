package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.Card;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

/** Abstract class for a generic Hatch card.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 23 May 2015
 */
public abstract class HatchCard implements Card {

    /** Game state */
    protected GameState mGameState;

    /** Game player */
    protected GamePlayer mGamePlayer;

    /** Constructor
     *
     * @param state Game State
     */
    protected HatchCard(GameState state) {
        mGameState = state;
        mGamePlayer = state.getCurrentPlayer();
    } 

    /** Get next state after using this card
     *
     * @return Next state
     */
    @Override
    public abstract PlayerState doAction();
}
