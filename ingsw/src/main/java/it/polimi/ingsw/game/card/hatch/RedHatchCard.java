package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.InfoOpcode;
import it.polimi.ingsw.game.state.NotMyTurnState;
import it.polimi.ingsw.game.state.PlayerState;

/** Red Hatch Card. 
 * It forces the player to move to EndingTurnState (without winning).
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class RedHatchCard extends HatchCard {

    /** Constructor
     *
     * @param state Game State
     */
    public RedHatchCard(GameState state) {
        super(state);
    }

    /** Return a new EndingTurnState */
    @Override
    public PlayerState doAction() {
        mGameState.broadcastPacket( new GameCommand( InfoOpcode.INFO_RED_HATCH ) );
        return new NotMyTurnState(mGameState);
    }

}
