package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.common.InfoOpcode;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.PlayerState;

/** Silence dangerous card. 
 * Just say "SILENCE" and go on to {@link EndingTurnState}.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class SilenceCard extends DangerousCard {

    /** Constructor
     *
     * @param state GameState
     */
    public SilenceCard(GameState state) {
        super(state);
    }

    /** Send info to current player about the card and then move to Ending Turn State
     *
     * @return Next player state
     */
    @Override
    public PlayerState doAction() {
        mGameState.broadcastPacket( InfoOpcode.INFO_SILENCE );

        return new EndingTurnState(mGameState);
    }

}
