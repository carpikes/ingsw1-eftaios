package it.polimi.ingsw.client.gui;
import java.awt.Point;
import java.awt.Polygon;

public class HexagonFactory  {
	public static final int numberOfVertices = 6;
	
	private HexagonFactory() {}
	
	public static Polygon createHexagon( Point center, int size ) {
    	int xPts[] = new int[numberOfVertices];
    	int yPts[] = new int[numberOfVertices];
    	
    	// get vertices by simply rotating by PI/6 for 6 times
    	for( int i = 0; i < numberOfVertices; ++i ) {
    		xPts[i] = (int)(center.getX() + size * Math.cos(i*Math.PI/3));
    		yPts[i] = (int)(center.getY() + size * Math.sin(i*Math.PI/3));
    	}
    	
    	return new Polygon( xPts, yPts, numberOfVertices );
    }
}