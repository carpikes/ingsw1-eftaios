package it.polimi.ingsw.exception;

public class DrawingModeException extends RuntimeException {

    private static final long serialVersionUID = 2865321288575957562L;

    public DrawingModeException() {
        super();
    }

    public DrawingModeException(String arg0, Throwable arg1, boolean arg2,
            boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public DrawingModeException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DrawingModeException(String message) {
        super(message); 
    }

    public DrawingModeException(Throwable cause) {
        super(cause); 
    }

}
