package it.polimi.ingsw.client.gui;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class HexagonFactory  {
	public static final int numberOfVertices = 6;
	private static Path2D hexagonPath;
	
	private HexagonFactory() {}
	
	public static Shape createHexagon( Point2D.Double center, double size ) {
	    hexagonPath = new Path2D.Double();
	     
	    hexagonPath.moveTo(center.getX()+size, center.getY());

        // get vertices by simply rotating by PI/6 for 6 times
        for( int i = 1; i < numberOfVertices; ++i ) {
            hexagonPath.lineTo(center.getX() + size * Math.cos(i*Math.PI/3), center.getY() + size * Math.sin(i*Math.PI/3));
        }
        
        hexagonPath.lineTo(center.getX()+size, center.getY());
        hexagonPath.closePath();

	    return hexagonPath;
    }
}