package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Point;
import java.awt.Polygon;

/** Class for creating and returning a new hexagon according to values given.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * 
 */
public class HexagonFactory  {

    public static final int NUMBER_OF_VERTICES = 6;

    /**
     * Private constructor. Use createHexagon() instead.
     */
    private HexagonFactory() { 

    }

    /**
     * Creates a new Hexagon object.
     *
     * @param center Center coordinates 
     * @param size Radius of the hex
     * @return The hexagon just created
     */
    public static Hexagon createHexagon( Point center, int size, int type) {
        Polygon hexagonPath = new Polygon();

        // get vertices by simply rotating by PI/6 every time
        for( int i = 0; i < NUMBER_OF_VERTICES; ++i )
            hexagonPath.addPoint((int)(center.getX() + size * Math.cos(i*Math.PI/3)),
                    (int)(center.getY() + size * Math.sin(i*Math.PI/3)));

        return new Hexagon( center, size, hexagonPath, type);
    }

    /**
     * @param type
     * @return
     */
    public static ColorPalette getHexagonColorPalette(int type) {
        switch(type) {
        case SectorBuilder.ALIEN:                            return ColorPalette.ALIEN;           
        case SectorBuilder.DANGEROUS:                        return ColorPalette.DANGEROUS;       
        case SectorBuilder.NOT_DANGEROUS:                    return ColorPalette.NOT_DANGEROUS;   
        case SectorBuilder.HATCH:                            return ColorPalette.HATCH;           
        case SectorBuilder.USED_HATCH:                       return ColorPalette.USED_HATCH;        
        case SectorBuilder.HUMAN:                            return ColorPalette.HUMAN;          
        case SectorBuilder.NOT_VALID:                        return ColorPalette.NOT_VALID;       
        default:                                             return ColorPalette.NOT_VALID;       
        }
    }
}
