package it.polimi.ingsw.client.gui;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/** Class for Hexagon view. Contains center, size, and shape to be drawn on screen.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 *
 */
public class Hexagon {
    
    private Point2D.Double center;
    private double size;
    private Path2D.Double path;
    
    /**
     * Instantiates a new hexagon view.
     *
     * @param center Coordinates of center
     * @param size Radius Size
     * @param hexagonPath Shape of hexagon
     */
    public Hexagon(Point2D.Double center, double size, Path2D.Double hexagonPath) {
        this.center = center;
        this.size = size;
        this.path = hexagonPath;
    }
    
    /**
     * Gets the path.
     *
     * @return the path
     */
    public Path2D.Double getPath() {
        return path;
    }
    
    /**
     * Gets the center.
     *
     * @return the center
     */
    public Point2D.Double getCenter() {
        return center;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public double getSize() {
        return size;
    }
}
