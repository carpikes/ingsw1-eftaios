/**
 * 
 */
package it.polimi.ingsw.testgame.player;

import static org.junit.Assert.assertEquals;
import it.polimi.ingsw.exception.TooFewPlayersException;
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
        RoleBuilder.generateRoles(0);
    }
    
    private void testRoleFactoryHelper( int numberOfPlayers ) {
        int humans = 0; 
        int aliens = 0;
        
        List<Role> roles = RoleBuilder.generateRoles( numberOfPlayers );
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
