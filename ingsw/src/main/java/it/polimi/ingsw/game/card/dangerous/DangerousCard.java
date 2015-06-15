package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

/** Abstract class representing a card drawn in a dangerous sector.
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 23 May 2015
 */
public abstract class DangerousCard {
    /** Game state */
    protected GameState mGameState;

    /** Game player */
    protected GamePlayer mGamePlayer;

    /** Constructor for dangerous card class.
     *
     * @param state Current GameState 
     * @param player Player who used the card
     */
    protected DangerousCard(GameState state) {
        mGameState = state;
        mGamePlayer = state.getCurrentPlayer();
    } 

    /** Get the effect of the dangerous card.
     *
     * @return Next state for the player
     */
    public abstract PlayerState doAction();
}
