package it.polimi.ingsw.exception;

/** Exception thrown when trying to use an invalid DrawingMode in GUI canvas.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class DrawingModeException extends RuntimeException {

    /** Serial version */
    private static final long serialVersionUID = 2865321288575957562L;

    /** Invoke superclass constructor
     *
     * @param message The message to be shown
     */
    public DrawingModeException(String message) {
        super(message); 
    }

}
