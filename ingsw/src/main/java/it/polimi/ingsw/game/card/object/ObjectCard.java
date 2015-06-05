/**
 * 
 */
package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;

import java.io.Serializable;


/**
 * Abstract class for a generic Object Card. Always drawn by a human after using some sorts of Dangerous Cards.
 * @author Michele
 * @since 23 May 2015
 */
public abstract class ObjectCard implements Serializable {

    protected GameState mGameState;
    protected GamePlayer mGamePlayer;
    
    protected ObjectCard(GameState state, GamePlayer player) {
        // FIXME AGGIUNGI CONTROLLO: Player dev'essere umano
        mGameState = state;
        mGamePlayer = player;
    } 
	
    /**
     * Resolve the effect of the card.
     * @return Next state for the invoker
     */
	public abstract PlayerState doAction();
}
