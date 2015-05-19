package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.game.GameLogic;
import it.polimi.ingsw.game.GamePlayer;


public abstract class Sector {
	
    @Override
	public String toString() {
	    return getClass().getName();
	}
	
	public abstract int getId();
	public abstract boolean doSectorAction(GameLogic logic, GamePlayer player);
}
