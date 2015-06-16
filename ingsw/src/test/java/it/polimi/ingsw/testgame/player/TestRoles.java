package it.polimi.ingsw.testgame.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.exception.TooFewPlayersException;
import it.polimi.ingsw.game.player.Alien;
import it.polimi.ingsw.game.player.Human;
import it.polimi.ingsw.game.player.Role;
import it.polimi.ingsw.game.player.RoleBuilder;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/** Test game roles
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class TestRoles {

    /**
     * Check if the role factory is working as expected
     */
    @Test
    public void testRoleFactory() {
        /** test for odd and even numbers */
        testRoleFactoryHelper( 5 );
        testRoleFactoryHelper( 6 );
    }

    /**
     * Check what happens if there are too few players
     */
    @Test(expected=TooFewPlayersException.class)
    public void tooFewPlayers() {
        RoleBuilder.generateRoles(0, true);
    }
    
    /**
     * Test the Role Generator
     */
    @Test
    public void testGenerator() {
        for(int i = 2; i < 8; i++) {
            for(int j = 0; j < 100; j++) {
                List<Role> l = RoleBuilder.generateRoles(i, true);
                int c = 0;
                for(Role r : l) {
                    if(r instanceof Alien)
                        c++;
                    else
                        c--;
                    assertTrue(r.getMaxMoves() > 0 && r.getMaxMoves() < 3);
                }
                assertTrue(c == 0 || c == 1);
            }
            
        }
        
    }

    /**
     * Helper of TestRoleFactory
     * @param numberOfPlayers Number of players
     */
    private void testRoleFactoryHelper( int numberOfPlayers ) {
        int humans = 0; 
        int aliens = 0;

        List<Role> roles = RoleBuilder.generateRoles( numberOfPlayers, true);
        Iterator<Role> it = roles.iterator();

        while( it.hasNext() ) {
            if(it.next() instanceof Human) 
                humans++; 
            else 
                aliens++;
        }

        assertEquals( humans, numberOfHumans(numberOfPlayers) );
        assertEquals( aliens, numberOfAliens(numberOfPlayers) );
    }

    /** Get the number of aliens given a number of players
     * @param numberOfPlayers Number of players in game
     * @return Number of aliens
     */
    private int numberOfAliens(int numberOfPlayers) {
        return numberOfPlayers - numberOfHumans( numberOfPlayers );
    }

    /** Get the number of humans given a number of players
     * @param numberOfPlayers Number of players in game
     * @return Number of humans
     */
    private int numberOfHumans( int numberOfPlayers ) {
        return numberOfPlayers / 2;
    }
}
