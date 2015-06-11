package it.polimi.ingsw.client.gui;

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
    private HexagonFactory() { }

    /**
     * Creates a new Hexagon object.
     *
     * @param center the center
     * @param size radius of hex
     * @return the hexagon
     */
    public static Hexagon createHexagon( Point center, int size, int type) {
        Polygon hexagonPath = new Polygon();

        // get vertices by simply rotating by PI/6 every time
        for( int i = 0; i < NUMBER_OF_VERTICES; ++i )
            hexagonPath.addPoint((int)(center.getX() + size * Math.cos(i*Math.PI/3)),
                    (int)(center.getY() + size * Math.sin(i*Math.PI/3)));

        return new Hexagon( center, size, hexagonPath, type);
    }
}
