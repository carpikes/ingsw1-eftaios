/**
 * 
 */
package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.InfoOpcode;
import it.polimi.ingsw.game.common.Opcode;
import it.polimi.ingsw.game.sector.SectorBuilder;
import it.polimi.ingsw.game.state.MoveDoneState;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.game.state.NotMyTurnState;
import it.polimi.ingsw.game.state.PlayerState;
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
     * Check if server acknowledges a wrong position
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
     * Check if server acknowledges a wrong argument type
     */
    @Test
    public void testMoveWrongParameterType() {
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);

        // move to moving state
        game.update();

        // drop previous messages
        clearMessageQueue(game);

        // send an invalid position
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, new GameInfo(null, 0, false, null) ) );
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
        int id = game.getCurrentPlayer().getId();
        game.update();

        assertTrue( findGameCommandInQueue( game, InfoOpcode.INFO_USED_HATCH ) );

        PlayerState state = game.getCurrentPlayer().getCurrentState();

        // since we're playing a 2-player game, there are only two possibilities:
        // 1) The player has won, leaving the other player alone -> the game ends
        // 2) The player has moved into a broken hatch -> let the other player start his turn
        assertTrue( game.debugGameEnded() || (state instanceof StartTurnState && game.getCurrentPlayer().getId() != id));
    }

    /*
     * Test if an alien CANNOT go to an hatch
     */
    @Test
    public void testAlienMoveToHatch( ) {
        playToMovingState( true, false );

        // Alien cannot have hatch sector in their sets of possible moves!
        assertTrue( hatchPoints.isEmpty() );
    }

    @Test
    public void testAlienAttack() {
        // play as an alien till reaching move done state
        GameState game = this.playToMoveDoneState(true, false, true);

        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_ATTACK) );
        clearMessageQueue(game);
        game.update();

        assertTrue( findGameCommandInQueue(game, InfoOpcode.INFO_PLAYER_ATTACKED) );
    }

    @Test
    public void testHumanAttack() {
        // play as an alien till reaching move done state
        GameState game = this.playToMoveDoneState(true, true, true);

        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_ATTACK) );
        clearMessageQueue(game);
        game.update();

        // Humans cannot attack!
        assertFalse( findGameCommandInQueue(game, InfoOpcode.INFO_PLAYER_ATTACKED) );
    }

    @Test
    public void testDrawDangerousCard() {
        GameState game = playToMoveDoneState(false, false, true);

        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_DRAW_DANGEROUS_CARD) );
        clearMessageQueue(game);
        game.update();


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

    /**
     * Helper method. Play a basic game until reaching MovingState for current player
     * @param forceRole Force role for current player
     * @param human If forceRole is true, force to human if this is true, alien otherwise
     * @return The game played up to this state
     */
    private GameState playToMovingState(boolean forceRole, boolean human) {
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);

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

    /**
     * Play a game until reaching a MoveDone State
     * @param forceRole
     * @param human
     * @param dangerous
     * @return
     */
    private GameState playToMoveDoneState(boolean forceRole, boolean human, boolean dangerous) {
        GameState game = playToMovingState(forceRole, human);

        Point newPosition;
        if( dangerous ) {
            // send position and update game
            newPosition = dangerousPoints.iterator().next();
        } else {
            newPosition = notDangerousPoints.iterator().next();
        }

        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, newPosition ) );
        game.update();

        assertTrue( game.getCurrentPlayer().getCurrentState() instanceof MoveDoneState );

        return game;
    }

    /**
     * Force second player as first one in a basic two-player game
     * @param game The game
     */
    private void swapPlayers(GameState game) {
        game.getCurrentPlayer().setCurrentState( new NotMyTurnState(game) );
        game.debugSetNextTurnId(1);
        game.moveToNextPlayer();
    }

    /** 
     * Populate sets of dangerous, not dangerous and hatch sectors.
     * @param game The game you're playing
     */
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

    /**
     * Clear message queue.
     * @param game The game
     */
    private void clearMessageQueue(GameState game) {
        game.debugGetOutputQueue().clear();
    }



    /**
     * Find if the given command was sent in game.
     * @param game THe game you're testing
     * @param infoUsedHatch THe command you're looking for
     * @return Found or not?
     */
    private boolean findGameCommandInQueue(GameState game, Opcode code) {
        boolean found = false;

        for( Entry<Integer, GameCommand> pkt : game.debugGetOutputQueue() )
            if( ((GameCommand)pkt.getValue()).getOpcode() == code )
                found = true;

        return found;
    }

}
