package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.PlayerState;


/** Abstract class for a generic Object Card. 
 * Always drawn by a human after using some sorts of Dangerous Cards.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 23 May 2015
 */
public abstract class ObjectCard {

    /** Game State */
    protected GameState mGameState;

    /** Game Player */
    protected GamePlayer mGamePlayer;

    /** Name */
    protected final String mName;

    /** Id */
    protected final int mId;

    /** Constructor
     *
     * @param state Game State
     * @param name Name
     */
    protected ObjectCard(GameState state, int id, String name) {
        mGameState = state;
        mGamePlayer = state.getCurrentPlayer();
        mName = name;
        mId = id;
    } 

    /** Resolve the effect of the card.
     *
     * @return Next state for the invoker
     */
    public abstract PlayerState doAction();

    /** Get the card name
     *
     * @return The card name
     */
    public String getName() {
        return mName;
    }

    /** Get the card id
     *
     * @return The card id
     */
    public int getId() {
        return mId;
    }

    /** Is the card usable?
     *
     * @return True if the card is usable
     */
    public abstract boolean isUsable();
}
