package it.polimi.ingsw.client.gui;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Hexagon {
    
    private Point2D.Double center;
    private double size;
    private Path2D.Double path;
    
    public Hexagon(Point2D.Double center, double size, Path2D.Double hexagonPath) {
        this.center = center;
        this.size = size;
        this.path = hexagonPath;
    }
    
    public Path2D.Double getPath() {
        return path;
    }
    
    public Point2D.Double getCenter() {
        return center;
    }

    public double getSize() {
        return size;
    }
}
