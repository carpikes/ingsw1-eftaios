package it.polimi.ingsw.game.common;

import java.io.Serializable;

/** View command
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class ViewCommand implements Serializable{
    
    /** Serial version */
    private static final long serialVersionUID = 1L;

    /** Opcode */
    private final ViewOpcode mOpcode;

    /** Arguments */
    private final Serializable[] mArgs;

    /** Constructor
     *
     * @param opcode The opcode
     * @param args Arguments
     */
    public ViewCommand(ViewOpcode opcode, Serializable... args) {
        mOpcode = opcode;
        mArgs = args;
    }

    /** Get the opcode
     *
     * @return the opcode
     */
    public ViewOpcode getOpcode() {
        return mOpcode;
    }

    /** Get the arguments
     *
     * @return command arguments
     */
    public Serializable[] getArgs() {
        return mArgs;
    }
}
