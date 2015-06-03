package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.exception.SectorException;

/**
 * Builder for sectors. Create a new sector based on ID.
 * @author Michele
 * @since 3 Jun 2015
 */
public class SectorBuilder {
    
    public static final int NOT_VALID = 0;
    public static final int HATCH = 2;
    public static final int USED_HATCH = 4;
    public static final int NOT_DANGEROUS = 1;
    public static final int DANGEROUS = 3;
    public static final int ALIEN = 8;
    public static final int HUMAN = 9;
    
    private SectorBuilder() {}
    
    /**
     * Create a new sector according to the ID given.
     * @param id The type of sector
     * @return A new sector
     * @throws SectorException Thrown when sector ID not valid
     */
    public static Sector getSectorFor(int id) throws SectorException  {
        switch(id) {
            case ALIEN:              return new Sector(ALIEN, false);
            case DANGEROUS:          return new Sector(DANGEROUS, true);
            case NOT_DANGEROUS:      return new Sector(NOT_DANGEROUS, true);
            case HATCH:              return new Sector(HATCH, true);
            case USED_HATCH:         return new Sector(USED_HATCH, false);
            case HUMAN:              return new Sector(HUMAN, false);
            case NOT_VALID:          return new Sector(NOT_VALID, false);
            default:                 throw new SectorException("Illegal sector code");
        }
    }
}
