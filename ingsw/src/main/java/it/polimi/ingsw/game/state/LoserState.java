/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class LoserState extends PlayerState {

    public LoserState(GameState state) {
        super(state);

        state.addToOutputQueue( GameCommand.INFO_LOSER );
        state.getCurrentPlayer().sendPacket( GameCommand.CMD_SC_LOSE );
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
