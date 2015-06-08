/**
 * 
 */
package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;


/**
 * Abstract class for a generic Object Card. Always drawn by a human after using some sorts of Dangerous Cards.
 * @author Michele
 * @since 23 May 2015
 */
public abstract class ObjectCard {

    protected GameState mGameState;
    protected GamePlayer mGamePlayer;
    protected final String mName;
    protected final int mId;
    
    protected ObjectCard(GameState state, int id, String name) {
        // FIXME AGGIUNGI CONTROLLO: Player dev'essere umano
        mGameState = state;
        mGamePlayer = state.getCurrentPlayer();
        mName = name;
        mId = id;
    } 
	
    /**
     * Resolve the effect of the card.
     * @return Next state for the invoker
     */
	public abstract PlayerState doAction();
	
	public String getName() {
	    return mName;
	}
	
	public int getId() {
	    return mId;
	}
	
	public abstract boolean isUsable();
}
