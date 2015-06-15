package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.InfoOpcode;
import it.polimi.ingsw.game.state.PlayerState;

/** Noise in your sector Dangerous Card. 
 * Tell everyone your position
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class NoiseInYourSectorCard extends DangerousCard {

    /** Constructor
     *
     * @param state GameState
     */
    public NoiseInYourSectorCard(GameState state) {
        super(state);
    }

    /** Send info to current player about the card and then get a new object card
     *
     * @return Next player state
     */
    @Override
    public PlayerState doAction() { 
        mGameState.broadcastPacket( new GameCommand(InfoOpcode.INFO_NOISE, mGamePlayer.getCurrentPosition()) );

        return mGameState.getObjectCard( );
    }

}
