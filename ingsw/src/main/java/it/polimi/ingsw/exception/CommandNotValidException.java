package it.polimi.ingsw.exception;

public class CommandNotValidException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -3785617445339999499L;

    public CommandNotValidException(String message) {
        super(message);
    }
}