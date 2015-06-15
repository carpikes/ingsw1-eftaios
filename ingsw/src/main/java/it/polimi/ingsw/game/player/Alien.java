package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.config.Config;

/** Alien role. 
 * Can move up to 2 sectors per time (3 if it has eaten).
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 3 Jun 2015
 */
public class Alien implements Role {
    /** Has the alien eaten yet? */
    private boolean hasEaten;

    /** Alien constructor */
    public Alien( ) {
        super();
        this.hasEaten = false;
    }

    /** Get number of max moves
     * 
     * @see it.polimi.ingsw.game.player.Role#getMaxMoves()
     */
    @Override
    public int getMaxMoves() {
        if( hasEaten ) 
            return Config.MAX_ALIEN_FULL_MOVES;
        else
            return Config.MAX_ALIEN_MOVES;
    }

    /** Check if the alien is full and can move up to 3 sectors per time
     *
     * @return If it has eaten or not
     */
    public boolean hasEaten() {
        return hasEaten;
    }

    /** Set alien to full or not
     *
     * @param hasEaten
     */
    public void setHasEaten(boolean hasEaten) {
        this.hasEaten = hasEaten;
    }

}
