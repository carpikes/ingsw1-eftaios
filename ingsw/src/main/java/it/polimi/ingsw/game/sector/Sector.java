package it.polimi.ingsw.game.sector;

import java.io.Serializable;

/** Model for a sector.
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since May, 2015
 */
public class Sector implements Serializable{

    /** Serial version */
    private static final long serialVersionUID = 1L;

    /** The type of sector */
    private int mId;

    /** Players can get on/through this sector or not? */
    private boolean mIsCrossable;

    /** Constructor
     *
     * @param id Sector id
     * @param crossable True if crossable
     */
    public Sector( int id, boolean crossable ) {
        mId = id;
        mIsCrossable = crossable;
    }

    /** Get type of sector
     *
     * @return ID sector
     */
    public int getId() { 
        return mId; 
    }

    /** Return true if this is NOT a NOT_VALID sector (off map)
     *
     * @return True if it is a valid sector
     */
    public boolean isValid() {
        return mId != SectorBuilder.NOT_VALID;
    }

    /** Check if this sector is crossable or not
     *
     * @return True if it is crossable
     */
    public boolean isCrossable() {
        return mIsCrossable;
    }
}
