package it.polimi.ingsw.exception;

/** Too Few Players
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 8 Jun 2015
 */
public class TooFewPlayersException extends RuntimeException {

    /** Serial version */
    private static final long serialVersionUID = 1718771915030754653L;

    /** Invoke superclass constructor
     *
     * @param message msg
     */
    public TooFewPlayersException(String message) {
        super(message);
    }
}
