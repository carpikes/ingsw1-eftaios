/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class WinnerState extends PlayerState {

    public WinnerState(GameState state, GamePlayer player) {
        super(state, player);

        state.broadcastPacket( GameCommand.INFO_WINNER );
        state.sendPacketToCurrentPlayer( GameCommand.CMD_SC_WIN );
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        return this;
    }
    
    @Override
	public boolean stillInGame() {
		return false;
	}

}
