package it.polimi.ingsw.exception;

/** Exception thrown when someone tries to construct a new sector with an invalid ID.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class SectorException extends RuntimeException {

    /** Serial version */
    private static final long serialVersionUID = -8112962578669558769L;

    /** Invoke superclass constructor
     *
     * @param message The message to be shown
     */
    public SectorException(String message) {
        super(message);
    }
}
