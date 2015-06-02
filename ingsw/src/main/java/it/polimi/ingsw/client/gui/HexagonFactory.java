package it.polimi.ingsw.client.gui;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

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
	public static Hexagon createHexagon( Point2D.Double center, double size) {
	    Path2D.Double hexagonPath = new Path2D.Double();
	     
	    hexagonPath.moveTo(center.getX()+size, center.getY());

        // get vertices by simply rotating by PI/6 every time
        for( int i = 1; i < NUMBER_OF_VERTICES; ++i ) {
            hexagonPath.lineTo(center.getX() + size * Math.cos(i*Math.PI/3), center.getY() + size * Math.sin(i*Math.PI/3));
        }
        hexagonPath.lineTo(center.getX()+size, center.getY());
        
        hexagonPath.closePath();

	    return new Hexagon( center, size, hexagonPath );
    }
}