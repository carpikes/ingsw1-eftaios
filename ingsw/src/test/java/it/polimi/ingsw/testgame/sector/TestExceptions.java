package it.polimi.ingsw.testgame.sector;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.exception.CLIException;
import it.polimi.ingsw.exception.CommandNotValidException;
import it.polimi.ingsw.exception.DebugException;
import it.polimi.ingsw.exception.DefenseException;
import it.polimi.ingsw.exception.DrawingModeException;
import it.polimi.ingsw.exception.GUIException;
import it.polimi.ingsw.exception.GameException;
import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.exception.InvalidGameInfoException;
import it.polimi.ingsw.exception.InvalidMapIdException;
import it.polimi.ingsw.exception.InvalidViewException;
import it.polimi.ingsw.exception.RMIException;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.exception.ServerException;
import it.polimi.ingsw.exception.TCPException;
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
        assertTrue(new CLIException("test1").toString().contains("test1"));
        assertTrue(new CommandNotValidException("test1").toString().contains("test1"));
        assertTrue(new DebugException("test1").toString().contains("test1"));
        assertTrue(new DefenseException("test1").toString().contains("test1"));
        assertTrue(new DrawingModeException("test1").toString().contains("test1"));
        assertTrue(new GUIException("test1").toString().contains("test1"));
        assertTrue(new GameException("test1").toString().contains("test1"));
        assertTrue(new IllegalStateOperationException("test1").toString().contains("test1"));
        assertTrue(new InvalidCardException("test1").toString().contains("test1"));
        assertTrue(new InvalidGameInfoException("test1").toString().contains("test1"));
        assertTrue(new InvalidMapIdException("test1").toString().contains("test1"));
        assertTrue(new InvalidViewException("test1").toString().contains("test1"));
        assertTrue(new RMIException("test1").toString().contains("test1"));
        assertTrue(new SectorException("test1").toString().contains("test1"));
        assertTrue(new ServerException("test1").toString().contains("test1"));
        assertTrue(new TCPException("test1").toString().contains("test1"));
        assertTrue(new TooFewPlayersException("test1").toString().contains("test1"));
    }
}
