/**
 * 
 *//*
package it.polimi.ingsw.testgame.card;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.dangerous.DangerousCard;
import it.polimi.ingsw.game.card.dangerous.NoiseInAnySectorCard;
import it.polimi.ingsw.game.card.dangerous.NoiseInYourSectorCard;
import it.polimi.ingsw.game.card.dangerous.SilenceCard;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.MoveDoneState;
import it.polimi.ingsw.game.state.NoiseInAnySectorState;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.GameManager;
import it.polimi.ingsw.testserver.ClientConnMock;

import java.awt.Point;

import org.junit.BeforeClass;
import org.junit.Test;

*//**
 * @author Michele
 * @since 2 Jun 2015
 *//*
public class TestDangerousCards {
    private static GameManager manager;
    private static GameState game;
    private static Client c1, c2;
    private static GamePlayer human, alien;
    
    private static Point testValidPosition;
    private static Point humanStart;
    
    *//**
     * Setup GameManager with fake connections in order to simulate receiving & sending packets 
     *//*
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
    
    // FIXME check for incoming packet    
    @Test
    public void testNoiseInAnySectorCard() {
        human.resetValues();
        
        human.setCurrentState( new MoveDoneState(game ) );
        DangerousCard card = new NoiseInAnySectorCard(game, human);
        human.setCurrentState( card.doAction() ); // move to NoiseInAnySectorState
        
        assertTrue( human.getCurrentState() instanceof NoiseInAnySectorState );
        
        // from here on drawObjectCard() is invoked, see tests below
    }
    
    @Test
    public void testNoiseInYourSectorCard() {
        human.resetValues();
        
        human.setCurrentState( new MoveDoneState(game ) );
        DangerousCard card = new NoiseInYourSectorCard(game, human);
        human.setCurrentState( card.doAction() );
        
        // object card routine already passed too
        assertTrue( human.getCurrentState() instanceof EndingTurnState );
    }
    
    @Test
    public void testSilenceSectorCard() {
        human.resetValues();
        
        human.setCurrentState( new MoveDoneState(game ) );
        DangerousCard card = new SilenceCard(game, human);
        human.setCurrentState( card.doAction() );
        
        // object card routine already passed too
        assertTrue( human.getCurrentState() instanceof EndingTurnState );
    }

}
*/