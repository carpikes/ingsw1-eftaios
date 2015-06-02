package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.config.Config;

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

	public boolean hasEaten() {
		return hasEaten;
	}

	public void setHasEaten(boolean hasEaten) {
		this.hasEaten = hasEaten;
	}

}
