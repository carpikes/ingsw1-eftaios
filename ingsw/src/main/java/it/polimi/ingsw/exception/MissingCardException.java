package it.polimi.ingsw.exception;

public class MissingCardException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5943219704357029192L;

    public MissingCardException(String message) {
        super(message);
    }
}