package it.polimi.ingsw.exception;

public class IllegalStateOperationException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -437060460576133955L;

    public IllegalStateOperationException(String message) {
        super(message);
    }
}