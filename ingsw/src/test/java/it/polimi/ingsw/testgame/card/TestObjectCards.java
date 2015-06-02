/**
 * 
 */
package it.polimi.ingsw.testgame.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.object.AdrenalineCard;
import it.polimi.ingsw.game.card.object.AttackCard;
import it.polimi.ingsw.game.card.object.DefenseCard;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.card.object.SedativesCard;
import it.polimi.ingsw.game.card.object.SpotlightCard;
import it.polimi.ingsw.game.card.object.TeleportCard;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.MoveDoneState;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.GameManager;
import it.polimi.ingsw.testserver.ClientConnMock;

import java.awt.Point;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Michele
 * @since 2 Jun 2015
 */
public class TestObjectCards {
    private static GameManager manager;
    private static GameState game;
    private static Client c1, c2;
    private static GamePlayer human, alien;
    
    private static Point testValidPosition;
    private static Point humanStart;
    
    /**
     * Setup GameManager with fake connections in order to simulate receiving & sending packets 
     */
    @BeforeClass
    public static void runBeforeClass() {
        manager = new GameManager();
       
        c1 = new Client( new ClientConnMock(), manager );
        c2 = new Client( new ClientConnMock(), manager );
        
        c1.setUsername("Alien");
        c2.setUsername("Human");
        
        manager.addPlayer(c1);
        manager.addPlayer(c2);
        
        // GALILEI
        manager.setMap(c1, 1);
        
        manager.update();
        game = manager.getGameState();
        
        if( game.getPlayers().get(0).isAlien() ) {
            alien = game.getPlayers().get(0);
            human = game.getPlayers().get(1);
        } else {
            human = game.getPlayers().get(0);
            alien = game.getPlayers().get(1);
        }
       
        testValidPosition = new Point(0,1);
        humanStart = new Point(11, 7);
    }
    
    /**
     * Adrenaline card used in Moving State: set max moves to 2
     */
    @Test
    public void testGoodAdrenalineCard() { 
        human.resetValues();
        
        human.forceState( new MovingState(game, human) );
        ObjectCard card = new AdrenalineCard(game, human);
        card.doAction();
        
        assertEquals( human.getMaxMoves(), Config.MAX_HUMAN_ADRENALINE_MOVES );
    }
    
    /**
     * Adrenaline card used in other states: command discarded
     */
    @Test
    public void testBadAdrenalineCard() { 
        human.resetValues();
        
        human.forceState( new MoveDoneState(game, human) );
        
        ObjectCard card = new AdrenalineCard(game, human);
        card.doAction();
        assertEquals( human.getMaxMoves(), Config.MAX_HUMAN_MOVES );
    }
    
    /**
     * Put the two players in the same sector and let the human attack
     * There will be only one at the end of the day...
     */
    @Test
    public void testAttackCard() {
        human.resetValues();
        alien.resetValues();
        
        game.rawMoveTo( human, testValidPosition );
        game.rawMoveTo( alien, testValidPosition );
        
        assertEquals( game.getNumberOfPlayersInSector( testValidPosition ), 2 );
        
        ObjectCard card = new AttackCard(game, human);
        card.doAction();
        
        assertEquals( game.getNumberOfPlayersInSector( testValidPosition ), 1 );
    }
    
    /**
     * Check if the defense card enables defense for human player
     */
    @Test
    public void testDefenseCard() {
        human.resetValues();
        
        assertFalse( human.isDefenseEnabled() );
        ObjectCard card = new DefenseCard(game, human);

        card.doAction(); // return;
        assertTrue( human.isDefenseEnabled() );
    }
    
    /**
     * Disable drawing a card for this turn
     */
    @Test
    public void testSedativesCard() {
        human.resetValues();
        
        assertTrue( human.shouldDrawDangerousCard() );
        
        ObjectCard card = new SedativesCard(game, human);
        card.doAction();
        assertFalse( human.shouldDrawDangerousCard() );
    }
    
    // FIXME TO BE COMPLETED
    @Ignore
    @Test
    public void testSpotlightCard() {
        human.resetValues();
        
        game.rawMoveTo( human, testValidPosition );
        game.rawMoveTo( alien, testValidPosition );
        
        human.forceState( new MoveDoneState(game, human) );
        
        ObjectCard card = new SpotlightCard(game, human);
        game.queuePacket( new NetworkPacket( GameCommand.CMD_CS_SET_POSITION, testValidPosition ) );
        card.doAction();
        
        // ...
    }
    
    /**
     * Test if after using the card the human player returns to start point 
     */
    @Test
    public void testTeleportCard() {
        human.resetValues();
        
        game.rawMoveTo( human, testValidPosition );
        ObjectCard card = new TeleportCard(game, human);
        card.doAction();
        
        assertEquals( human.getCurrentPosition(), humanStart );
    }
}
