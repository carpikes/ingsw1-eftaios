package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

/** Class for Hexagon view. Contains mCenter, mSize, and shape to be drawn on screen.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 *
 */
public class Hexagon {
    private static final Color PLAYER_ON = Color.YELLOW;
    private Point mCenter;
    private double mSize;
    private Polygon mPath;
    private Color mColor;
    
    /**
     * Instantiates a new hexagon view.
     *
     * @param mCenter Coordinates of mCenter
     * @param mSize Radius Size
     * @param hexagonPath Shape of hexagon
     */
    public Hexagon(Point center, int size, Polygon hexagonPath, int type) {
        this.mCenter = center;
        this.mSize = size;
        this.mPath = hexagonPath;

        switch(type) {
            case SectorBuilder.ALIEN:         mColor = new Color(0,50,0); break;
            case SectorBuilder.DANGEROUS:     mColor = new Color(150,150,150); break;
            case SectorBuilder.NOT_DANGEROUS: mColor = new Color(255,255,255); break;
            case SectorBuilder.HATCH:         mColor = new Color(47,53,87); break;
            case SectorBuilder.HUMAN:         mColor = new Color(50,0,0); break;
            case SectorBuilder.NOT_VALID:     mColor = null; 
        }
    }
    
    /**
     * Gets the mPath.
     *
     * @return the mPath
     */
    public Polygon getPath() {
        return mPath;
    }
    
    /**
     * Gets the mCenter.
     *
     * @return the mCenter
     */
    public Point getCenter() {
        return mCenter;
    }

    /**
     * Gets the mSize.
     *
     * @return the mSize
     */
    public double getSize() {
        return mSize;
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
        if( mColor == null )
            return;

        boolean drawStroke = true;

        Color real = mColor;
        if(playerOn)
            real = PLAYER_ON;
        
        if(!enabled) {
            drawStroke = false;
            real = new Color(real.getRed()/2, real.getGreen()/2, real.getBlue()/2, 0xa0);
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
