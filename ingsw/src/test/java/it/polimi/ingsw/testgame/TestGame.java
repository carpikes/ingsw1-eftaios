/**
 * 
 */
package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.GameManager;
import it.polimi.ingsw.testserver.ClientConnMock;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Michele
 * @since 1 Jun 2015
 */
public class TestGame {

    private static GameManager manager;
    private static Client c1, c2;
    
    /**
     * Setup GameManager with fake connections in order to simulate receiving & sending packets 
     */
    @BeforeClass
    public static void runBeforeClass() {
        manager = new GameManager();
       
        c1 = new Client( new ClientConnMock(), manager );
        c2 = new Client( new ClientConnMock(), manager );
        
        c1.setUsername("BalenoGatto");
        c2.setUsername("BurroPardo");
        
        manager.addPlayer(c1);
        manager.addPlayer(c2);
        
        manager.setMap(c1, 1);
        
        manager.update();
        assertTrue( manager.isRunning() );
    }
    
    @Test
    public void test() {
        
    }

}
