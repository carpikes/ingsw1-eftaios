/**
 * 
 */
package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.InfoOpcode;
import it.polimi.ingsw.game.network.Opcode;
import it.polimi.ingsw.game.sector.SectorBuilder;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.game.state.NotMyTurnState;
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
        GameState game = playToMovingState( false, false );
        
        // send position and update game
        Point newPosition = dangerousPoints.iterator().next();
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, newPosition ) );
        game.update();
        
        assertTrue( game.getCurrentPlayer().getCurrentPosition().equals( newPosition ) );
    }
    
    /**
     * Test a human moving to an hatch and using it
     */
    @Test
    public void testMoveToHatchOK() {
        GameState game = playToMovingState( true, true );
        
        // send position and update game
        Point newPosition = hatchPoints.iterator().next();
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, newPosition ) );
        game.update();
        
        assertTrue( findGameCommandInQueue( game, InfoOpcode.INFO_USED_HATCH ) );
    }
    
    /*
     * Test if an alien CANNOT go to an hatch
     */
    @Test
    public void testAlienMoveToHatch(  ) {
        playToMovingState( true, false );
        
        // Alien cannot have hatch sector in their sets of possible moves!
        assertTrue( hatchPoints.isEmpty() );
    }

    /**
     * Helper method. Play a basic game until reaching MovingState for current player
     * @param forceRole Force role for current player
     * @param human If forceRole is true, force to human if this is true, alien otherwise
     * @return The game played up to this state
     */
    private GameState playToMovingState(boolean forceRole, boolean human) {
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);
        
        // ONLY HUMANS CAN MOVE TO HATCH SECTORS!
        if( forceRole ) {
            if( ( !human && game.getCurrentPlayer().isHuman() ) || ( human && game.getCurrentPlayer().isAlien() ) ) {
                swapPlayers(game);
            }
        }
        
        // move to moving state
        game.update();
        
        // drop previous messages
        clearMessageQueue(game);
        
        // create set of dangerous, not dangerous and hatch sectors available for current player
        createSetsOfPossibleSectors(game);
        
        return game;
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
    
    /*----- END OF TESTS ----- */

    private void swapPlayers(GameState game) {
        game.getCurrentPlayer().setCurrentState( new NotMyTurnState(game) );
        game.debugSetNextTurnId(1);
        game.moveToNextPlayer();
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
    
    private void clearMessageQueue(GameState game) {
        game.debugGetOutputQueue().clear();
    }
    
    

    /**
     * Find if the given command was sent in game.
     * @param game THe game you're testing
     * @param infoUsedHatch THe command you're looking for
     * @return Found or not?
     */
    private boolean findGameCommandInQueue(GameState game, Opcode infoUsedHatch) {
        boolean found = false;
        
        for( Entry<Integer, GameCommand> pkt : game.debugGetOutputQueue() )
            if( ((GameCommand)pkt.getValue()).getOpcode() == infoUsedHatch )
                found = true;
        
        return found;
    }
    
}
