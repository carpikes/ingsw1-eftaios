/**
 * 
 */
package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.game.state.StartTurnState;

import java.awt.Point;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/**
 * @author Michele
 * @since 8 Jun 2015
 */
public class TestGameState {

    public static final int DEBUG_ID = 1;
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int START_ID = 0;
    
    // Entry<Integer,GameCommand> pkt : game.debugGetOutputQueue()
    
    /**
     * Checks if a game moves to "moving state" after first update for current player
     */
    @Test
    public void testStartGame() {
        GameState game = new GameState("YES", DEBUG_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        assertTrue( game.getCurrentPlayer().getCurrentState() instanceof StartTurnState ); 
        game.update();

        assertTrue( game.getCurrentPlayer().getCurrentState() instanceof MovingState ); 
    }
    
    /**
     * Check if server acknowlodges a wrong position
     */
    @Test
    public void testInvalidMove() {
        GameState game = new GameState("YES", DEBUG_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        // move to moving state
        game.update();
        
        // drop previous messages
        clearMessageQueue(game);
        
        // send an invalid position
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, new Point(-1, -1) ) );
        game.update();
        
        boolean found = findGameCommandInQueue(game, GameOpcode.CMD_SC_MOVE_INVALID);
        
        assertTrue( found );
    }
    
    /**
     * Check if server acknowlodges a wrong argument type
     */
    @Test
    public void testMoveWrongParameterType() {
        GameState game = new GameState("YES", DEBUG_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        // move to moving state
        game.update();
        
        // drop previous messages
        clearMessageQueue(game);
        
        // send an invalid position
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, new GameStartInfo(null, 0, false, null) ) );
        game.update();
        
        boolean found = findGameCommandInQueue(game, GameOpcode.CMD_SC_MOVE_INVALID);
        
        assertTrue( found );
    }
    
    /**
     * Test a correct move
     */
    @Test
    public void testMoveOK() {
        GameState game = new GameState("YES", DEBUG_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        // move to moving state
        game.update();
        
        // drop previous messages
        clearMessageQueue(game);
        
        // Get first point among the available ones
        Set<Point> points = game.getCellsWithMaxDistance();
        Point newPosition = points.iterator().next();
        
        // send position and update game
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, newPosition ) );
        game.update();
        
        assertTrue( game.getCurrentPlayer().getCurrentPosition().equals( newPosition ) );
    }
    
    @Test
    
    
    /*
    @Test
    public void testInvalidActionInMovingState() {
        GameState game = new GameState("YES", DEBUG_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        // move to moving state
        game.update();
        
        // drop previous messages
        clearMessageQueue(game);
        
        // send an invalid position
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_DISCARD_OBJECT_CARD) );
        game.update();
    }
    */
    private void clearMessageQueue(GameState game) {
        game.debugGetOutputQueue().clear();
    }
    
    

    /**
     * Find if the given command was sent in game.
     * @param game THe game you're testing
     * @param opcode THe command you're looking for
     * @return Found or not?
     */
    private boolean findGameCommandInQueue(GameState game, GameOpcode opcode) {
        boolean found = false;
        
        for( Entry<Integer, GameCommand> pkt : game.debugGetOutputQueue() )
            if( ((GameCommand)pkt.getValue()).getOpcode() == opcode )
                found = true;
        
        return found;
    }
    
    

}
