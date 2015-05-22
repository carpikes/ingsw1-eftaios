package it.polimi.ingsw.game.network;

import java.io.Serializable;
/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 17, 2015
 */

public class NetworkPacket implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int mOpcode;
    private final Serializable[] mArgs;
    
    public NetworkPacket(int opcode, Serializable... args) {
        mOpcode = opcode;
        mArgs = args;
    }
    
    public int getOpcode() {
        return mOpcode;
    }
    
    public Serializable[] getArgs() {
        return mArgs;
    }
}
