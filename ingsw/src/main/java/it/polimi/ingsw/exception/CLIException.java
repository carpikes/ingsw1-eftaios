package it.polimi.ingsw.exception;

/** Exception thrown when ???.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class CLIException extends RuntimeException {

    /** Serial version */
    private static final long serialVersionUID = -3785617445339999499L;

    /** Invoke superclass constructor
     *
     * @param message The message to be shown
     */
    public CLIException(String message) {
        super(message);
    }
}
