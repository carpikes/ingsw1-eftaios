package it.polimi.ingsw.game.sector;

import java.io.Serializable;


public class Sector implements Serializable{
	
    private int id;
    
    // other properties: maybe we can refactor and create a SectorProperties class
    private boolean crossable;
    //...
    
    public Sector( int id, boolean crossable ) {
        this.id = id;
        this.crossable = crossable;
    }
	
	public int getId() { return id; }
	
    /**
     * @return
     */
    public boolean isValid() {
        return id != SectorBuilder.NOT_VALID;
    }

    /**
     * @return
     */
    
    public boolean isCrossable() {
        return crossable;
    }
}
