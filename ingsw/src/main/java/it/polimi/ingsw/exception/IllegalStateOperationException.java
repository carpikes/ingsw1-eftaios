package it.polimi.ingsw.exception;

/** Exception invoked when a client sends a command not supported in current player state.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class IllegalStateOperationException extends RuntimeException {

    /** Serial version */
    private static final long serialVersionUID = -437060460576133955L;

    /** Invoke superclass constructor
     *
     * @param message The message to be shown
     */
    public IllegalStateOperationException(String message) {
        super(message);
    }
}
