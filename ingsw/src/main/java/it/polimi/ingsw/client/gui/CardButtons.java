package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.common.ResourceLoader;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;

import java.io.IOException;
import java.io.InputStream;

/** Card button enum
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 6 Jun 2015
 */
public enum CardButtons {
    ADRENALINE( ObjectCardBuilder.ADRENALINE_CARD, "img/adrenaline.png", true),
    ATTACK( ObjectCardBuilder.ATTACK_CARD, "img/attack.png", true),
    DEFENSE( ObjectCardBuilder.DEFENSE_CARD, "img/defense.png", false),
    SEDATIVES( ObjectCardBuilder.SEDATIVES_CARD, "img/sedatives.png", true),
    SPOTLIGHT( ObjectCardBuilder.SPOTLIGHT_CARD, "img/spotlight.png", true),
    TELEPORT( ObjectCardBuilder.TELEPORT_CARD, "img/teleport.png", true),
    NULL( -1, "img/null.png", false);

    /** Card id */
    private int mId;
    /** Image file */
    private String mImgFile;
    /** Is enabled? */
    private boolean mEnabled;

    /** Private constructor
     *
     * @param id Id
     * @param file File
     * @param enabled Enabled
     */
    private CardButtons( int id, String file, boolean enabled ) {
        mId = id;
        mImgFile = file;
        mEnabled = enabled;
    }

    /** Get the card id
     *
     * @return The id
     */
    public int getId() {
        return mId;
    }

    /** The the card image file
     *
     * @return The image file
     * @throws IOException File not found
     */
    public InputStream getImageFile() throws IOException {
        return ResourceLoader.getInstance().loadResource(mImgFile);
    }

    /** Is this card enables?
     *
     * @return True if is enabled
     */
    public boolean isEnabled() {
        return mEnabled;
    }

}
