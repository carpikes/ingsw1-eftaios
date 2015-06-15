package it.polimi.ingsw.client.gui;

import java.awt.Color;

/**
 * @author Michele Albanese <michele.albanese93@gmail.com>
 * @since 15 Jun 2015
 */
public enum ColorPalette {

    BACKGROUND       (Color.WHITE),
    PLAYER_ON        (Color.YELLOW),
    MOUSE_ON_THIS    (Color.CYAN),
    ALIEN            (new Color(0,50,0)),
    DANGEROUS        (new Color(150,150,150)),
    NOT_DANGEROUS    (new Color(255,255,255)),
    HATCH            (new Color(47,53,87)),
    USED_HATCH       (new Color(178,0,0)),
    HUMAN            (new Color(50,0,0)),
    STROKE           (Color.DARK_GRAY),

    NOT_VALID        (null),

    // blinking colors
    NOISE            (Color.GREEN),
    ATTACK           (Color.RED),
    SPOTLIGHT        (Color.BLACK);

    private Color mColor;

    /** Construct a new color
     * @param color The desired color
     */
    private ColorPalette( Color color ) {
        mColor = color;
    }

    
    /** Return this color
     * @return The color
     */
    public Color getColor() {
        return mColor;
    }
}