package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.config.Config;

/** Human role. 
 * Can move up to 2 move when adrenaline is active (1 otherwise)
 *
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 3 Jun 2015
 */
public class Human implements Role {

    /** Is the adrenaline used? */
    private boolean adrenalineUsed;

    /** Human constructor */
    public Human( ) {
        super();
        this.adrenalineUsed = false;
    }

    /** Get number of max moves
     *
     * @see it.polimi.ingsw.game.player.Role#getMaxMoves()
     * @return Number of max moves
     */
    @Override
    public int getMaxMoves() {
        if( adrenalineUsed ) 
            return Config.MAX_HUMAN_ADRENALINE_MOVES;
        else
            return Config.MAX_HUMAN_MOVES;
    }

    /** Renaline card during this turn.
     *
     * @return True if adrenaline is active
     */
    public boolean hasUsedAdrenaline() {
        return adrenalineUsed;
    }

    /** Set adrenaline to used / not used.
     *
     * @param adrenaline The value to use
     */
    public void setAdrenaline(boolean adrenaline) {
        adrenalineUsed = adrenaline;
    }

}
