package it.polimi.ingsw.exception;

public class SectorException extends RuntimeException {

    private static final long serialVersionUID = -8112962578669558769L;

    public SectorException() {
        super();
        
    }

    public SectorException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        
    }

    public SectorException(String message, Throwable cause) {
        super(message, cause);
        
    }

    public SectorException(String message) {
        super(message);
        
    }

    public SectorException(Throwable cause) {
        super(cause);
        
    }
}