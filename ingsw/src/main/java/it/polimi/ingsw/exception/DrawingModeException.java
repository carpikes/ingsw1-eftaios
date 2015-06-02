package it.polimi.ingsw.exception;

/**
 * Exception thrown when trying to use an invalid DrawingMode in GUI canvas.
 * @author Michele
 * @since 2 Jun 2015
 */
public class DrawingModeException extends RuntimeException {

    private static final long serialVersionUID = 2865321288575957562L;

    /**
     * Invoke superclass constructor
     * @param message The message to be shown
     */
    public DrawingModeException(String message) {
        super(message); 
    }

}
