package it.polimi.ingsw.game.common;

import java.io.Serializable;

/* View opcodes
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public enum ViewOpcode implements Serializable {
    
    /** Choose a position on the map */
    CMD_ENABLEMAPVIEW("Choose a position on the map"),

    /** Choose an object card */
    CMD_CHOOSEOBJECTCARD("Choose an object card"), 

    /** Draw a dangerous card */
    CMD_DRAWDANGEROUSCARD("Draw a dangerous card"), 

    /** Attack */
    CMD_ATTACK("Attack"), 

    /** End of turn */
    CMD_ENDTURN("End Turn"), 

    /** Discard an object card */
    CMD_DISCARDOBJECTCARD("Discard an object card");

    /** Text */
    private final String mText;

    /** Constructor
     *
     * @param text Text
     */
    private ViewOpcode(String text) {
        mText = text;
    }

    /** Get the text
     *
     * @return the text
     */
    @Override
    public String toString() {
        return mText;
    }
}
