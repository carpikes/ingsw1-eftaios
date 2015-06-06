package it.polimi.ingsw.game.sector;

import java.io.Serializable;

/*
 * Model for a sector.
 */
public class Sector implements Serializable{

    private static final long serialVersionUID = 1L;

    /** The type of sector */
    private int id;
    
    /** Players can get on/through this sector or not? */
    private boolean crossable;
    //...
    
    public Sector( int id, boolean crossable ) {
        this.id = id;
        this.crossable = crossable;
    }
	
    /**
     * Get type of sector
     * @return ID sector
     */
	public int getId() { return id; }
	
    /**
     * Return true is this is NOT a NOT_VALID sector (off map)
     */
    public boolean isValid() {
        return id != SectorBuilder.NOT_VALID;
    }

    /**
     * Check if this sector is crossable or not
     * @return True if crossable
     */
    public boolean isCrossable() {
        return crossable;
    }
}
