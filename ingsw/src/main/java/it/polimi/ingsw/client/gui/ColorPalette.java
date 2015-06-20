package it.polimi.ingsw.client.gui;

import java.awt.Color;

/** Color palette
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 15 Jun 2015
 */
public enum ColorPalette {

    BACKGROUND       (Color.BLACK),
    PLAYER_ON        (Color.YELLOW),
    MOUSE_ON_THIS    (Color.CYAN),
    ALIEN            (new Color(0,207,70)),
    DANGEROUS        (new Color(170,170,170)),
    NOT_DANGEROUS    (new Color(255,255,255)),
    HATCH            (new Color(255,131,0)),
    USED_HATCH       (new Color(178,100,100)),
    HUMAN            (new Color(50,0,0)),
    STROKE           (Color.DARK_GRAY),
    USERNAME_ON_BOARD(Color.WHITE),
    NOT_VALID        (null),

    /** blinking colors */
    NOISE            (Color.GREEN),
    ATTACK           (Color.RED),
    SPOTLIGHT        (Color.BLACK);

    /** Color */
    private Color mColor;

    /** Construct a new color
     *
     * @param color The desired color
     */
    private ColorPalette( Color color ) {
        mColor = color;
    }

    /** Return this color
     *
     * @return The color
     */
    public Color getColor() {
        return mColor;
    }
}
