package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.NoiseInAnySectorState;
import it.polimi.ingsw.game.state.PlayerState;

/** Noise in any sector Dangerous Card. 
 * Tell everyone a position (noise in any sector)  
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class NoiseInAnySectorCard extends DangerousCard {

    /** Constructor
     *
     * @param state GameState
     */
    public NoiseInAnySectorCard(GameState state) {
        super(state);
    }

    /** Send info to current player about the card and then move to NoiseInAnySectorState for next interactions with the user 
     *
     * @return Next state
     */
    @Override
    public PlayerState doAction() {
        return new NoiseInAnySectorState(mGameState);
    }

}
