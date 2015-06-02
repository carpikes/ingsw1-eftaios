package it.polimi.ingsw.exception;

/**
 * Exception thrown when ???.
 * @author Michele
 * @since 2 Jun 2015
 */
public class CommandNotValidException extends RuntimeException {

    private static final long serialVersionUID = -3785617445339999499L;

    /**
     * Invoke superclass constructor
     * @param message The message to be shown
     */
    public CommandNotValidException(String message) {
        super(message);
    }
}