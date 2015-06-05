package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.config.Config;

/**
 * Human role. Can move up to 2 move when adrenaline is active (1 otherwise)
 * @author Michele
 * @since 3 Jun 2015
 */
public class Human implements Role {
    
    private boolean adrenalineUsed;
    
    public Human( ) {
		super();
		this.adrenalineUsed = false;
	}

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.Role#getMaxMoves()
     */
    @Override
    public int getMaxMoves() {
        if( adrenalineUsed ) 
        	return Config.MAX_HUMAN_ADRENALINE_MOVES;
        else
        	return Config.MAX_HUMAN_MOVES;
    }

    /** 
     * Check if he has used an adrenaline card during this turn.
     * @return True if adrenaline is active
     */
	public boolean hasUsedAdrenaline() {
		return adrenalineUsed;
	}

	/**
	 * Set adrenaline to used / not used.
	 * @param adrenaline The value to use
	 */
	public void setAdrenaline(boolean adrenaline) {
		adrenalineUsed = adrenaline;
	}

}
