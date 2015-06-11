package it.polimi.ingsw.game.common;

import java.io.Serializable;
/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 17, 2015
 */

public class GameCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Opcode mOpcode;
    private final Serializable[] mArgs;

    public GameCommand(Opcode opcode, Serializable... args) {
        mOpcode = opcode;
        mArgs = args;
    }

    public Opcode getOpcode() {
        return mOpcode;
    }

    public Serializable[] getArgs() {
        return mArgs;
    }
}
