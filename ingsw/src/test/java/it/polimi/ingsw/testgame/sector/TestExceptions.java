package it.polimi.ingsw.testgame.sector;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.exception.CommandNotValidException;
import it.polimi.ingsw.exception.DebugException;
import it.polimi.ingsw.exception.DrawingModeException;
import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.exception.TooFewPlayersException;

import org.junit.Test;

/** Test exceptions
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since Jun 13, 2015
 */
public class TestExceptions {

    /** Check if all exceptions are working */
    @Test
    public void TestException() {
        assertTrue(new CommandNotValidException("test1").toString().contains("test1"));
        assertTrue(new DebugException("test1").toString().contains("test1"));
        assertTrue(new DrawingModeException("test1").toString().contains("test1"));
        assertTrue(new IllegalStateOperationException("test1").toString().contains("test1"));
        assertTrue(new InvalidCardException("test1").toString().contains("test1"));
        assertTrue(new SectorException("test1").toString().contains("test1"));
        assertTrue(new TooFewPlayersException("test1").toString().contains("test1"));
    }
}
