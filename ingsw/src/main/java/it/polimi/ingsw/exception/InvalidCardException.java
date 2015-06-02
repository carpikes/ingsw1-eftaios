package it.polimi.ingsw.exception;

/**
 * Exception invoked when trying to construct a non-existent card in card builders.
 * @author Michele
 * @since 2 Jun 2015
 */
public class InvalidCardException extends RuntimeException {

    private static final long serialVersionUID = -5943219704357029192L;

    /**
     * Invoke superclass constructor
     * @param message The message to be shown
     */
    public InvalidCardException(String message) {
        super(message);
    }
}