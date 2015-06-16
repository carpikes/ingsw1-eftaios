package it.polimi.ingsw.client.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

/** Class for Hexagon view. 
 * Contains mCenter, mSize, and shape to be drawn on screen.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class Hexagon {

    /** Center */
    private Point mCenter;

    /** Size */
    private double mSize;

    /** Polygon path */
    private Polygon mPath;

    /** Color */
    private Color mColor;

    /** Instantiates a new hexagon view.
     *
     * @param center Coordinates of mCenter
     * @param size Radius Size
     * @param hexagonPath Shape of hexagon
     * @param type Type of hexagon
     */
    public Hexagon(Point center, int size, Polygon hexagonPath, int type) {
        mCenter = center;
        mSize = size;
        mPath = hexagonPath;
        
        mColor = HexagonFactory.getHexagonColorPalette(type).getColor();
    }
    
    /** Gets the mPath.
     *
     * @return the mPath
     */
    public Polygon getPath() {
        return mPath;
    }

    /** Gets the mCenter.
     *
     * @return the mCenter
     */
    public Point getCenter() {
        return mCenter;
    }

    /** Gets the mSize.
     *
     * @return the mSize
     */
    public double getSize() {
        return mSize;
    }

    
    /** Draw an hexagon in the Graphics2d object given (the mapcanvas panel actually)
     *
     * @param g2d The graphics2D used to draw
     * @param playerOn The player is in this position?
     * @param enabled Can this sector be selected or not?
     * @param mouseOnThis Is mouse hovering on this sector
     * @param shouldBlink True if this should blink (attack, spotlight and noise actions)
     * @param colorBlink Color to blink
     */
    public void draw(Graphics2D g2d, boolean playerOn, boolean enabled, boolean mouseOnThis, boolean shouldBlink, Color colorBlink) {
        if( mColor == null )
            return;

        boolean drawStroke = true;

        /** Color for current position: low priority */
        Color real = mColor;
        if(playerOn)
            real = ColorPalette.PLAYER_ON.getColor();

        if( shouldBlink ) {
            /** blinking effect */
            double k = System.currentTimeMillis() / 250.0;
            real = colorBlink;
            real = new Color((int)(Math.abs(Math.cos(k)) * (255-real.getRed()) + real.getRed()), 
                    (int)(Math.abs(Math.cos(k)) * (255-real.getGreen()) + real.getGreen()), 
                    (int)(Math.abs(Math.cos(k)) * (255-real.getBlue()) + real.getBlue()), 0xff);
        }

        if(!enabled) {
            drawStroke = false;
            /** Overwrite in case it is not selectable and make it gray-ish */
            real = new Color(real.getRed()/2, real.getGreen()/2, real.getBlue()/2, 0xa0);
        } else if(mouseOnThis)
            /** hovering color has higher priority than PlayerOn */
            real = ColorPalette.MOUSE_ON_THIS.getColor();

        g2d.setColor( real );
        g2d.fill(getPath());

        /** border */
        if(drawStroke) {
            g2d.setColor( ColorPalette.STROKE.getColor() );
            g2d.draw(getPath());
        }
    }
}
