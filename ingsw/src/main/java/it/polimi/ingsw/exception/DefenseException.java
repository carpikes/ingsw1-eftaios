package it.polimi.ingsw.exception;

/**
 * Exception thrown when ???.
 * @author Michele
 * @since 2 Jun 2015
 */
public class DefenseException extends RuntimeException {


    /**
     * 
     */
    private static final long serialVersionUID = 8333712152333516001L;

    /**
     * Invoke superclass constructor
     * @param message The message to be shown
     */
    public DefenseException(String message) {
        super(message);
    }
}
