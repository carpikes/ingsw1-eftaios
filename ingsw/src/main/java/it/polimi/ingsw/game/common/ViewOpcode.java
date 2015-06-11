package it.polimi.ingsw.game.common;

import java.io.Serializable;

public enum ViewOpcode implements Serializable {
    CMD_ENABLEMAPVIEW("Choose a position on the map"),
    CMD_CHOOSEOBJECTCARD("Choose an object card"), 
    CMD_DRAWDANGEROUSCARD("Draw a dangerous card"), 
    CMD_ATTACK("Attack"), 
    CMD_ENDTURN("End Turn"), 
    
    // it could be merged with chooseobjectcard
    CMD_DISCARDOBJECTCARD("Discard an object card");
    
    private final String mText;
    
    private ViewOpcode(String text) {
        mText = text;
    }
    
    @Override
    public String toString() {
        return mText;
    }
}
