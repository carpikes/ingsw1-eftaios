package it.polimi.ingsw.exception;

/** DebugException
 * Trying to execute a debug command in a non-debug context
 * or Trying to execute a non-debug command i a debug context
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 *
 */
public class DebugException extends RuntimeException{

    /** Serial Version */
    private static final long serialVersionUID = 1L;

    /** Constructor
     *
     * @param msg Message
     */
    public DebugException(String msg) {
        super(msg);
    }
}
