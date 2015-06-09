package it.polimi.ingsw.exception;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 *
 */
public class DebugException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DebugException(String msg) {
        super(msg);
    }
}
