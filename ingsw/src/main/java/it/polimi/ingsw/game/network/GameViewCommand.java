package it.polimi.ingsw.game.network;

import java.io.Serializable;

public class GameViewCommand implements Serializable{
    private final GameViewOpcode mOpcode;
    private final Serializable[] mArgs;
    
    public GameViewCommand(GameViewOpcode opcode, Serializable... args) {
        mOpcode = opcode;
        mArgs = args;
    }
    
    public GameViewOpcode getOpcode() {
        return mOpcode;
    }
    
    public Serializable[] getArgs() {
        return mArgs;
    }
}
