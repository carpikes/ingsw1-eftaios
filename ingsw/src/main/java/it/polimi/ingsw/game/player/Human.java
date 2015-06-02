package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.config.Config;

public class Human implements Role {
    
    private boolean adrenalineUsed;
    
    public Human( ) {
		super();
		this.adrenalineUsed = false;
	}

    @Override
    public int getMaxMoves() {
        if( adrenalineUsed ) 
        	return Config.MAX_HUMAN_ADRENALINE_MOVES;
        else
        	return Config.MAX_HUMAN_MOVES;
    }

	public boolean hasUsedAdrenaline() {
		return adrenalineUsed;
	}

	public void setAdrenaline(boolean adrenaline) {
		adrenalineUsed = adrenaline;
	}

}
