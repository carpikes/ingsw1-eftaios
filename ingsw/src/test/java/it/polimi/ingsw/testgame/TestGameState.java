/**
 * 
 */
package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.sector.SectorBuilder;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.game.state.StartTurnState;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/**
 * @author Michele
 * @since 8 Jun 2015
 */
public class TestGameState {

    public static final int MAP_ID = 3;
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int START_ID = 0;
    
    Set<Point> dangerousPoints = new HashSet<>();
    Set<Point> notDangerousPoints = new HashSet<>();
    Set<Point> hatchPoints = new HashSet<>();
    
    /**
     * Checks if a game moves to "moving state" after first update for current player
     */
    @Test
    public void testStartGame() {
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        assertTrue( game.getCurrentPlayer().getCurrentState() instanceof StartTurnState ); 
        game.update();

        assertTrue( game.getCurrentPlayer().getCurrentState() instanceof MovingState ); 
    }
    
    /**
     * Check if server acknowlodges a wrong position
     */
    @Test
    public void testInvalidMove() {
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
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
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
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
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        // move to moving state
        game.update();
        
        // drop previous messages
        clearMessageQueue(game);
        
        // create set of dangerous, not dangerous and hatch sectors available for current player
        createSetsOfPossibleSectors(game);
        
        // send position and update game
        Point newPosition = dangerousPoints.iterator().next();
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, newPosition ) );
        game.update();
        
        assertTrue( game.getCurrentPlayer().getCurrentPosition().equals( newPosition ) );
    }

    private void createSetsOfPossibleSectors(GameState game) {
        dangerousPoints.clear();
        notDangerousPoints.clear();
        hatchPoints.clear();
        
        // Get first point among the available ones
        Set<Point> points = game.getCellsWithMaxDistance();
        Iterator<Point> it = points.iterator();
        
        while( it.hasNext() ) {
            Point p = it.next();
            
            if( game.getMap().getSectorAt(p).getId() == SectorBuilder.DANGEROUS )
                dangerousPoints.add(p);
            else if( game.getMap().getSectorAt(p).getId() == SectorBuilder.NOT_DANGEROUS )
                notDangerousPoints.add(p);
            else if( game.getMap().getSectorAt(p).getId() == SectorBuilder.HATCH )
                hatchPoints.add(p);
        }
    }
    

    
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
