/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class WinnerState extends PlayerState {

    public WinnerState(GameState state) {
        super(state);

     // rimuovi da lista giocatori
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        return this;
    }

}
