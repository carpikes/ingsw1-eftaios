package it.polimi.ingsw.exception;

public class InvalidCardException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5943219704357029192L;

    public InvalidCardException(String message) {
        super(message);
    }
}