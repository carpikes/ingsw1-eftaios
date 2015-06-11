package it.polimi.ingsw.game.common;

import java.io.Serializable;

public class ViewCommand implements Serializable{
    private static final long serialVersionUID = 1L;
    private final ViewOpcode mOpcode;
    private final Serializable[] mArgs;

    public ViewCommand(ViewOpcode opcode, Serializable... args) {
        mOpcode = opcode;
        mArgs = args;
    }

    public ViewOpcode getOpcode() {
        return mOpcode;
    }

    public Serializable[] getArgs() {
        return mArgs;
    }
}
