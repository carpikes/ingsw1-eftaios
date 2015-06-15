/**
 * 
 */
package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.card.object.ObjectCardBuilder;

import java.io.File;

/**
 * @author Michele Albanese <michele.albanese93@gmail.com>
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

    private int id;
    private File imgFile;
    private boolean enabled;

    private CardButtons( int id, File file, boolean enabled ) {
        this.id = id;
        this.imgFile = file;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public File getImageFile() {
        return imgFile;
    }

    /**
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

}
