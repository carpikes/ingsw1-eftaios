/**
 * 
 */
package it.polimi.ingsw.testgame.player;

import static org.junit.Assert.*;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.player.Alien;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.Human;

import java.awt.Point;

import org.junit.Test;

/**
 * @author Michele
 * @since 2 Jun 2015
 */
public class TestGamePlayer {

    @Test
    public void testPreventAlienDefense() {
        GamePlayer alien = new GamePlayer( 0, new Alien(), new Point(0,0) );
        alien.setDefense( true );

        assertFalse( alien.isDefenseEnabled() );
    }

    @Test
    public void testDropDefense() {
        GamePlayer human = new GamePlayer( 0,new Human(), new Point(0,0) );
        human.setDefense( true );
        assertTrue( human.isDefenseEnabled() );

        human.dropDefense();

        assertFalse( human.isDefenseEnabled() );
    }

    @Test
    public void testIsCorrectRole() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );
        GamePlayer alien = new GamePlayer( 1, new Alien(), new Point(0,0) );

        assertTrue( human.isHuman() && alien.isAlien() );
        assertFalse( human.isAlien() || alien.isHuman() );
    }

    @Test
    public void testAdrenalineOnHuman() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );

        human.setAdrenaline( true );
        assertTrue( human.getAdrenaline() );
    }

    @Test
    public void testAdrenalineOnAlien() {
        GamePlayer alien = new GamePlayer( 0, new Alien(), new Point(0,0) );

        alien.setAdrenaline( true );
        assertFalse( alien.getAdrenaline() );
    }

    @Test
    public void testHumanDistance() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );
        
        assertFalse( human.isValidDistance( 2 ) || human.isValidDistance( -1 ) );
    }
    
    @Test
    public void testAdrenalineHumanDistance() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );
        human.setAdrenaline(true);
        
        assertTrue( human.isValidDistance( 2 ) );
        
        human.setAdrenaline(false);
        assertFalse( human.isValidDistance( 2 ) );
    }
    
    @Test
    public void testAlienFull() {
        GamePlayer alien = new GamePlayer( 0, new Alien(), new Point(0,0) );
        alien.setFull( true );
        
        assertTrue( alien.isFull() );
        assertTrue( alien.getMaxMoves() == Config.MAX_ALIEN_FULL_MOVES );
    }
}
