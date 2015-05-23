package it.polimi.ingsw.exception;

public class NetworkPacketNotValidException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NetworkPacketNotValidException(String message) {
        super(message);
    }
}