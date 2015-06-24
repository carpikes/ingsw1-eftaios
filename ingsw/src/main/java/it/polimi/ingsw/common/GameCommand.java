package it.polimi.ingsw.common;

import java.io.Serializable;
/** Game command
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 17, 2015
 */
public class GameCommand implements Serializable {

    /** Serial version */
    private static final long serialVersionUID = 1L;

    /** Opcode */
    private final Opcode mOpcode;
    
    /** Arguments */
    private final Serializable[] mArgs;

    /** Constructor
     *
     * @param opcode Command opcode
     * @param args Command arguments
     */
    public GameCommand(Opcode opcode, Serializable... args) {
        mOpcode = opcode;
        mArgs = args;
    }

    /** Get the opcode 
     * 
     * @return The opcode
     */
    public Opcode getOpcode() {
        return mOpcode;
    }

    /** Get the arguments 
     *
     * @return Arguments
     */
    public Serializable[] getArgs() {
        return mArgs;
    }
}
