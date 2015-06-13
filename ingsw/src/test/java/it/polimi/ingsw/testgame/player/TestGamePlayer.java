/**
 * 
 */
package it.polimi.ingsw.testgame.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.DefenseCard;
import it.polimi.ingsw.game.card.object.TeleportCard;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.player.Alien;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.Human;

import java.awt.Point;

import org.junit.Test;

/** Test GamePlayer
 * @author Michele
 * @since 2 Jun 2015
 */
public class TestGamePlayer {

    /**
     * Assert that aliens can't have defense card active
     */
    @Test
    public void testPreventAlienDefense() {
        GamePlayer alien = new GamePlayer( 0, new Alien(), new Point(0,0) );
        alien.setDefense( true );

        assertFalse( alien.isDefenseEnabled() );
    }

    /**
     * Check human defense
     */
    @Test
    public void testDropDefense() {
        GamePlayer human = new GamePlayer( 0,new Human(), new Point(0,0) );
        human.setDefense( true );
        assertTrue( human.isDefenseEnabled() );

        human.dropDefense();

        assertFalse( human.isDefenseEnabled() );
    }

    /**
     * Assert that role checks are working
     */
    @Test
    public void testIsCorrectRole() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );
        GamePlayer alien = new GamePlayer( 1, new Alien(), new Point(0,0) );

        assertTrue( human.getRole() instanceof Human );
        assertTrue( alien.getRole() instanceof Alien );
        assertTrue( human.isHuman() && alien.isAlien() );
        assertFalse( human.isAlien() || alien.isHuman() );
    }

    /**
     * Test adrenaline on humans
     */
    @Test
    public void testAdrenalineOnHuman() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );

        human.setAdrenaline( true );
        assertTrue( human.getAdrenaline() );
    }

    /** 
     * Test adrenaline on aliens
     */
    @Test
    public void testAdrenalineOnAlien() {
        GamePlayer alien = new GamePlayer( 0, new Alien(), new Point(0,0) );

        alien.setAdrenaline( true );
        assertFalse( alien.getAdrenaline() );
    }

    /** 
     * Test human reachable distance
     */
    @Test
    public void testHumanDistance() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );

        assertFalse( human.isValidDistance( 2 ) || human.isValidDistance( -1 ) );
    }

    /**
     * Test human reachable distance with adrenaline
     */
    @Test
    public void testAdrenalineHumanDistance() {
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );
        human.setAdrenaline(true);

        assertTrue( human.isValidDistance( 2 ) );

        human.setAdrenaline(false);
        assertFalse( human.isValidDistance( 2 ) );
    }

    /**
     * Test what happens when alien is full
     */
    @Test
    public void testAlienFull() {
        GamePlayer alien = new GamePlayer( 0, new Alien(), new Point(0,0) );
        alien.setFull( true );

        assertTrue( alien.isFull() );
        assertTrue( alien.getMaxMoves() == Config.MAX_ALIEN_FULL_MOVES );
    }
    
    /**
     * Test cards
     */
    @Test
    public void testCards() {
        GameState game = new GameState("YES", 1, 2, 0, true);
        GamePlayer human = new GamePlayer( 0, new Human(), new Point(0,0) );
        
        human.setObjectCardUsed(true);
        assertTrue(human.isObjectCardUsed());
        
        human.setShouldDrawDangerousCard(false);
        assertFalse(human.shouldDrawDangerousCard());
        
        assertEquals(human.getStateBeforeSpotlightCard(), null);
        
        human.addObjectCard(new DefenseCard(game, "Defense"));
        human.resetValues();
        
        assertEquals(human.getNumberOfCards(), 1);
        assertEquals(human.getNumberOfUsableCards(), 0);
        
        human.dropDefense();
        assertEquals(human.getNumberOfCards(), 0);
        
        human.addObjectCard(new TeleportCard(game, "Teleport"));
        human.useObjectCard(0);
        assertEquals(human.getNumberOfCards(), 0);
    }
}
