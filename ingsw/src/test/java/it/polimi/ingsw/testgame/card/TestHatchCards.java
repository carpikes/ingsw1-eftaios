/**
 * 
 *//*
package it.polimi.ingsw.testgame.card;

import static org.junit.Assert.*;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.hatch.GreenHatchCard;
import it.polimi.ingsw.game.card.hatch.HatchCard;
import it.polimi.ingsw.game.card.hatch.HatchCardBuilder;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.WinnerState;
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
public class TestHatchCards {
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
    
    @Test
    public void testHatchCard() {
        HatchCard card = HatchCardBuilder.getRandomCard(game, human);
        PlayerState next = card.getNextState();
        
        if( card instanceof GreenHatchCard)
            assertTrue( next instanceof WinnerState );
        else
            assertTrue( next instanceof EndingTurnState );
        
    }
}
*/