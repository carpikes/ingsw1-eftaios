package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.GameState.LastThings;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.InfoOpcode;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.WinnerState;

/** Green Hatch Card. 
 * It lets the player win.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class GreenHatchCard extends HatchCard {

    /** Constructor
     *
     * @param state Game State
     */
    public GreenHatchCard(GameState state) {
        super(state);
    }

    /** Set next state to WinnerState
     *
     * @return Next player state
     */
    @Override
    public PlayerState doAction() {
        mGameState.setLastThingDid(LastThings.HUMAN_USED_HATCH);
        
        mGameState.broadcastPacket( new GameCommand( InfoOpcode.INFO_GREEN_HATCH ) );
        return new WinnerState(mGameState, mGamePlayer.getId());
    }

}
