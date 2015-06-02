package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.exception.DrawingModeException;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.HashMap;

/** Class for Hexagon view. Contains center, size, and shape to be drawn on screen.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 *
 */
public class Hexagon {
    private static final Color PLAYER_ON = Color.RED;
    private Point center;
    private double size;
    private Polygon path;
    private Color color;
    
    /**
     * Instantiates a new hexagon view.
     *
     * @param center Coordinates of center
     * @param size Radius Size
     * @param hexagonPath Shape of hexagon
     */
    public Hexagon(Point center, int size, Polygon hexagonPath, int type) {
        this.center = center;
        this.size = size;
        this.path = hexagonPath;

        switch(type) {
            case SectorBuilder.ALIEN:         color = new Color(0,50,0); break;
            case SectorBuilder.DANGEROUS:     color = new Color(150,150,150); break;
            case SectorBuilder.NOT_DANGEROUS: color = new Color(255,255,255); break;
            case SectorBuilder.HATCH:         color = new Color(47,53,87); break;
            case SectorBuilder.HUMAN:         color = new Color(50,0,0); break;
            case SectorBuilder.NOT_VALID:     color = null; 
        }
    }
    
    /**
     * Gets the path.
     *
     * @return the path
     */
    public Polygon getPath() {
        return path;
    }
    
    /**
     * Gets the center.
     *
     * @return the center
     */
    public Point getCenter() {
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
    
    
    /**
     * The Enum DrawingMode. Used by {@link MapCanvasPanel#drawHexAt(Graphics2D, Point, DrawingMode)}
     */
    private enum DrawingMode {
        NORMAL,             // set color from sectorColors map
        SELECTED_HEX,       // hover color
        DISABLED            // grey
    }
    
    /**
     * Draw hex at given coordinates.
     *
     * @param g2d The Graphics2D object where to draw on
     * @param playerOn True if the current player is on this sector
     * @param enabled True if this sector is enabled
     * @param mouseOnThis True if the mouse is hovering this sector
     */

    public void draw(Graphics2D g2d, boolean playerOn, boolean enabled, boolean mouseOnThis) {
        if( color == null )
            return;

        boolean drawStroke = true;

        Color real = color;
        if(playerOn)
            real = PLAYER_ON;
        
        if(!enabled) {
            if(!playerOn) {
                drawStroke = false;
                real = new Color(real.getRed()/2, real.getGreen()/2, real.getBlue()/2, 0xa0);
            }
        } else if(mouseOnThis)
            real = Color.CYAN;
        
        g2d.setColor( real );
        g2d.fill(getPath());
        
        // border
        if(drawStroke) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(getPath());
        }
    }
}
