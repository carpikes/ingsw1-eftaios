/**
 * 
 */
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

/**
 * @author Michele
 * @since 2 Jun 2015
 */
public class TestRoles {

    @Test
    public void testRoleFactory() {
        // test for odd and even numbers
        testRoleFactoryHelper( 5 );
        testRoleFactoryHelper( 6 );
    }

    @Test(expected=TooFewPlayersException.class)
    public void tooFewPlayers() {
        RoleBuilder.generateRoles(0, true);
    }
    
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

    /**
     * @param numberOfPlayers
     * @return
     */
    private int numberOfAliens(int numberOfPlayers) {
        return numberOfPlayers - numberOfHumans( numberOfPlayers );
    }

    /**
     * @return
     */
    private int numberOfHumans( int numberOfPlayers ) {
        return numberOfPlayers / 2;
    }
}
