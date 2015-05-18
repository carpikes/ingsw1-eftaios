package it.polimi.ingsw.testclient.testgui;

import it.polimi.ingsw.client.gui.Hexagon;
import it.polimi.ingsw.client.gui.HexagonFactory;

import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import org.junit.Test;

public class TestHexagonFactory {

    @Test
    public void testHexagonNumberOfEdges() {
        Hexagon hex = HexagonFactory.createHexagon( new Point2D.Double(1,1), 1.0);
        
        // count edges through the PathIterator and make sure there are 6 segments on the way
        int edges = 0;
        PathIterator iterator = hex.getShape().getPathIterator(null);
        
        while( !iterator.isDone() ) {
            ++edges;
            iterator.next();
        }
        
        assert(edges == HexagonFactory.NUMBER_OF_VERTICES);
    }
    
    @Test
    public void testCreateHexagon() {
        // TODO: comparison with a simple hex
    }

}
