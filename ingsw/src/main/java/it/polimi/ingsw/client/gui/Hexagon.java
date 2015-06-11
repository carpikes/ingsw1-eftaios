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
            case SectorBuilder.ALIEN:         mColor = ColorPalette.ALIEN; break;
            case SectorBuilder.DANGEROUS:     mColor = ColorPalette.DANGEROUS; break;
            case SectorBuilder.NOT_DANGEROUS: mColor = ColorPalette.NOT_DANGEROUS; break;
            case SectorBuilder.HATCH:         mColor = ColorPalette.HATCH; break;
            case SectorBuilder.USED_HATCH:    mColor = ColorPalette.USED_HATCH; break;
            case SectorBuilder.HUMAN:         mColor = ColorPalette.HUMAN; break;
            case SectorBuilder.NOT_VALID:     mColor = ColorPalette.NOT_VALID; 
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

    public void draw(Graphics2D g2d, boolean playerOn, boolean enabled, boolean mouseOnThis, boolean noise) {
        if( mColor == null )
            return;

        boolean drawStroke = true;

        // Color for current position: low priority
        Color real = mColor;
        if(playerOn)
            real = ColorPalette.PLAYER_ON;

        if( noise ) {
            double k = System.currentTimeMillis() / 250.0;
            real = ColorPalette.NOISE;
            real = new Color((int)(Math.abs(Math.cos(k)) * (255-real.getRed()) + real.getRed()), 
                    (int)(Math.abs(Math.cos(k)) * (255-real.getGreen()) + real.getGreen()), 
                    (int)(Math.abs(Math.cos(k)) * (255-real.getBlue()) + real.getBlue()), 0xff);
        }

        if(!enabled) {
            drawStroke = false;
            // Overwrite in case it is not selectable and make it gray-ish
            real = new Color(real.getRed()/2, real.getGreen()/2, real.getBlue()/2, 0xa0);
        } else if(mouseOnThis)
            // hovering color has higher priority than PlayerOn
            real = ColorPalette.MOUSE_ON_THIS;

        g2d.setColor( real );
        g2d.fill(getPath());

        // border
        if(drawStroke) {
            g2d.setColor( ColorPalette.STROKE );
            g2d.draw(getPath());
        }
    }
}
