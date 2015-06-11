package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.config.Config;

/**
 * Alien role. Can move up to 2 sectors per time (3 if it has eaten).
 * @author Michele
 * @since 3 Jun 2015
 */
public class Alien implements Role {

    private boolean hasEaten;

    public Alien( ) {
        super();
        this.hasEaten = false;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.Role#getMaxMoves()
     */
    @Override
    public int getMaxMoves() {
        if( hasEaten ) 
            return Config.MAX_ALIEN_FULL_MOVES;
        else
            return Config.MAX_ALIEN_MOVES;
    }

    /**
     * Check if the alien is full and can move up to 3 sectors per time
     * @return If it has eaten or not
     */
    public boolean hasEaten() {
        return hasEaten;
    }

    /**
     * Set alien to full or not
     * @param hasEaten
     */
    public void setHasEaten(boolean hasEaten) {
        this.hasEaten = hasEaten;
    }

}
