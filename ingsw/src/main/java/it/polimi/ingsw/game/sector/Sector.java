package it.polimi.ingsw.game.sector;


public abstract class Sector {
	
    @Override
	public String toString() {
	    return getClass().getName();
	}
	
	public abstract int getId();
}
