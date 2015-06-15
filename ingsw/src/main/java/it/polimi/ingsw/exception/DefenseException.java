package it.polimi.ingsw.exception;

/** Exception thrown if user is trying to use defense when he shouldn't
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class DefenseException extends RuntimeException {
    /** Serial version */
    private static final long serialVersionUID = 8333712152333516001L;

    /** Invoke superclass constructor
     *
     * @param message The message to be shown
     */
    public DefenseException(String message) {
        super(message);
    }
}
