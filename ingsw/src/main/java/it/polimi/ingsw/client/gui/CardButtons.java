package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.card.object.ObjectCardBuilder;

import java.io.File;

/** Card button enum
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 6 Jun 2015
 */
public enum CardButtons {
    ADRENALINE( ObjectCardBuilder.ADRENALINE_CARD, new File("img/adrenaline.png"), true),
    ATTACK( ObjectCardBuilder.ATTACK_CARD, new File("img/attack.png"), true),
    DEFENSE( ObjectCardBuilder.DEFENSE_CARD, new File("img/defense.png"), false),
    SEDATIVES( ObjectCardBuilder.SEDATIVES_CARD, new File("img/sedatives.png"), true),
    SPOTLIGHT( ObjectCardBuilder.SPOTLIGHT_CARD, new File("img/spotlight.png"), true),
    TELEPORT( ObjectCardBuilder.TELEPORT_CARD, new File("img/teleport.png"), true),
    NULL( -1, new File("img/null.png"), false);

    /** Card id */
    private int mId;
    /** Image file */
    private File mImgFile;
    /** Is enabled? */
    private boolean mEnabled;

    /** Private constructor
     *
     * @param id Id
     * @param file File
     * @param enabled Enabled
     */
    private CardButtons( int id, File file, boolean enabled ) {
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
     */
    public File getImageFile() {
        return mImgFile;
    }

    /** Is this card enables?
     *
     * @return True if is enabled
     */
    public boolean isEnabled() {
        return mEnabled;
    }

}
