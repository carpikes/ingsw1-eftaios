/**
 * 
 */
package it.polimi.ingsw.exception;

/**
 * @author Michele
 * @since 8 Jun 2015
 */
public class TooFewPlayersException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1718771915030754653L;

    public TooFewPlayersException(String message) {
        super(message);
    }
}
