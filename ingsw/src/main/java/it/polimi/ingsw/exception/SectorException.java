package it.polimi.ingsw.exception;

/**
 * Exception thrown when someone tries to construct a new sector with an invalid ID.
 * @author Michele
 * @since 2 Jun 2015
 */
public class SectorException extends RuntimeException {

    private static final long serialVersionUID = -8112962578669558769L;

    /**
     * Invoke superclass constructor
     * @param message The message to be shown
     */
    public SectorException(String message) {
        super(message);
    }
}
