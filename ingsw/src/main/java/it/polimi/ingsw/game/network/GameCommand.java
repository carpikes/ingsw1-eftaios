package it.polimi.ingsw.game.network;

import java.io.Serializable;
/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 17, 2015
 */

public class GameCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final GameOpcode mOpcode;
    private final Serializable[] mArgs;
    
    public GameCommand(GameOpcode opcode, Serializable... args) {
        mOpcode = opcode;
        mArgs = args;
    }
    
    public GameOpcode getOpcode() {
        return mOpcode;
    }
    
    public Serializable[] getArgs() {
        return mArgs;
    }
}
