package it.polimi.ingsw.testgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.exception.DebugException;
import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.exception.InvalidMapIdException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.GameState.LastThings;
import it.polimi.ingsw.game.card.dangerous.DangerousCard;
import it.polimi.ingsw.game.card.dangerous.NoiseInAnySectorCard;
import it.polimi.ingsw.game.card.object.AttackCard;
import it.polimi.ingsw.game.card.object.DefenseCard;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.InfoOpcode;
import it.polimi.ingsw.game.common.Opcode;
import it.polimi.ingsw.game.sector.SectorBuilder;
import it.polimi.ingsw.game.state.DiscardingObjectCardState;
import it.polimi.ingsw.game.state.EndingTurnState;
import it.polimi.ingsw.game.state.MoveDoneState;
import it.polimi.ingsw.game.state.MovingState;
import it.polimi.ingsw.game.state.NoiseInAnySectorState;
import it.polimi.ingsw.game.state.NotMyTurnState;
import it.polimi.ingsw.game.state.PlayerState;
import it.polimi.ingsw.game.state.SpotlightCardState;
import it.polimi.ingsw.game.state.StartTurnState;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.ClientConn;
import it.polimi.ingsw.server.GameManager;
import it.polimi.ingsw.testserver.ServerToClientMock;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/** GameState tests
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 8 Jun 2015
 */
public class TestGameState {

    /** Map id */
    public static final int MAP_ID = 3;
    
    /** Number of players */
    public static final int NUMBER_OF_PLAYERS = 2;
    
    /** First player id */
    public static final int START_ID = 0;

    /** Dangerous Points */
    private Set<Point> dangerousPoints = new HashSet<>();
    
    /** Not dangerous points */
    private Set<Point> notDangerousPoints = new HashSet<>();
    
    /** Hatch */
    private Set<Point> hatchPoints = new HashSet<>();

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
     * Check if the real constructor(not the debug one) is working as expected 
     */
    @Test
    public void testRealConstructor() {
        GameManager gm = new GameManager();
        ClientConn c1 = new ServerToClientMock();
        ClientConn c2 = new ServerToClientMock();
        c1.run();
        c2.run();
        gm.addPlayer(new Client(c1, gm));
        gm.addPlayer(new Client(c2, gm));
        
        GameState game = new GameState(gm, 1);
        assertFalse(game.isDebugModeEnabled());
        
        game.flushOutputQueue();
        game.update();
        
        // Check if at least 1 packet has arrived
        GameCommand cmd = ((ServerToClientMock)c1).getPacketFromList();
        assertTrue(  cmd != null );
        
        game.clearOutputQueue();
        assertTrue( game.getPacketFromQueue() == null );
    }
    
    /**
     * Check if an invalid map exception is thrown when loading a fake map
     */
    @Test(expected=InvalidMapIdException.class)
    public void testRealConstructorMissingMap() {
        GameManager gm = new GameManager();
        ClientConn c1 = new ServerToClientMock();
        ClientConn c2 = new ServerToClientMock();
        c1.run();
        c2.run();
        gm.addPlayer(new Client(c1, gm));
        gm.addPlayer(new Client(c2, gm));
        
        GameState game = new GameState(gm, 50000);
    }
    
    /**
     * Check if the class prevents you from using debug functionalities when not in debug mode
     */
    @Test(expected=DebugException.class)
    public void testDebugConstructorError() {
        GameState gm = new GameState("NO", 0, 0, 0, false);
    }
       
    
    /**
     * Check if server acknowledges a wrong position
     */
    @Test
    public void testInvalidMove() {
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);

        /** move to moving state */
        game.update();

        /** drop previous messages */
        clearMessageQueue(game);

        /** send an invalid position */
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

        /** move to moving state */
        game.update();

        /** drop previous messages */
        clearMessageQueue(game);

        /** send an invalid position */
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

        /** send position and update game */
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

        /** send position and update game */
        Point newPosition = hatchPoints.iterator().next();
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, newPosition ) );
        int id = game.getCurrentPlayer().getId();
        game.update();

        assertTrue( findGameCommandInQueue( game, InfoOpcode.INFO_USED_HATCH ) );

        PlayerState state = game.getCurrentPlayer().getCurrentState();

        /** Since we're playing a 2-player game, there are only two possibilities:
          * 1) The player has won, leaving the other player alone -> the game ends
          * 2) The player has moved into a broken hatch -> let the other player start his turn
          */
        assertTrue( game.debugGameEnded() || (state instanceof StartTurnState && game.getCurrentPlayer().getId() != id));
    }

    /**
     * Test if an alien CANNOT go to an hatch
     */
    @Test
    public void testAlienMoveToHatch( ) {
        playToMovingState( true, false );

        /** Alien cannot have hatch sector in their sets of possible moves! */
        assertTrue( hatchPoints.isEmpty() );
    }

    /**
     * Test if an alien can attack
     */
    @Test
    public void testAlienAttack() {
        /** play as an alien till reaching move done state */
        GameState game = this.playToMoveDoneState(true, false, true);

        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_ATTACK) );
        clearMessageQueue(game);
        game.update();

        assertTrue( findGameCommandInQueue(game, InfoOpcode.INFO_PLAYER_ATTACKED) );
    }
    
    @Test
    public void testLastThingAfterKillingHuman() {
        /** play as an alien till reaching move done state */
        GameState game = this.playToMoveDoneState(true, false, true);
        
        // Set a defense card, simulate its effects for the enemy and move to the same sector
        int enemyId = ( game.getCurrentPlayer().getId() == 0 ) ? 1 : 0;
        game.debugGetPlayer(enemyId).setCurrentPosition( game.getCurrentPlayer().getCurrentPosition() );
        game.update();
        
        // try to attack
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_ATTACK) );
        clearMessageQueue(game);
        game.update();
        
        assertTrue( game.debugGetLastThingDid() == LastThings.KILLED_HUMAN );
    }
    
    @Test
    public void testDefendedAttack() {
        /** play as an alien till reaching move done state */
        GameState game = this.playToMoveDoneState(true, false, true);
        
        // Set a defense card, simulate its effects for the enemy and move to the same sector
        int enemyId = ( game.getCurrentPlayer().getId() == 0 ) ? 1 : 0;
        game.debugGetPlayer(enemyId).addObjectCard( new DefenseCard(game, "Defense") );
        game.debugGetPlayer(enemyId).setDefense(true);
        game.debugGetPlayer(enemyId).setCurrentPosition( game.getCurrentPlayer().getCurrentPosition() );
        game.update();
        
        assertTrue( game.debugGetPlayer(enemyId).getNumberOfCards() == 1 && game.debugGetPlayer(enemyId).getNamesOfCards()[0].equals("Defense") );
        
        // try to attack
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_ATTACK) );
        clearMessageQueue(game);
        game.update();
        
        // defense card dropped!
        assertTrue( game.debugGetPlayer(enemyId).getNumberOfCards() == 0 );
    }

    /**
     * Test the DiscardObjectCardState
     */
    @Test
    public void testDiscardObjectCardState() {
        GameState game = this.playToMoveDoneState(true, true, true);
        PlayerState p = new DiscardingObjectCardState(game);
        game.getCurrentPlayer().setCurrentState(p);
        
        game.update();
        assertTrue(p.update() instanceof EndingTurnState);
        
        game.update();
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_END_TURN, 0));
        
        game.update();
        assertTrue(game.getCurrentPlayer().getCurrentState() instanceof StartTurnState);

        for(int i = 0; i < 4; i++)
            game.getCurrentPlayer().addObjectCard(new AttackCard(game, "Attack"));
        p = new DiscardingObjectCardState(game);
        p.update();
        
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_DISCARD_OBJECT_CARD, 0));
        assertTrue(p.update() instanceof EndingTurnState);
        
        game.getCurrentPlayer().addObjectCard(new AttackCard(game, "Attack"));
        p = new DiscardingObjectCardState(game);
        p.update();
        
        game.getCurrentPlayer().setCurrentState(p);
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD, 0));
        assertTrue(p.update() != null);
    }
    
    /**
     * Test the startUsingObjectCardState
     */
    @Test
    public void testStartUsingObjectCardState() {
        GameState game = this.playToMoveDoneState(true, true, true);
        PlayerState p = new DiscardingObjectCardState(game);
        
        game.getCurrentPlayer().setCurrentState(p.update());
        
        assertTrue(game.getCurrentPlayer().getCurrentState() instanceof EndingTurnState);
        
        game.getCurrentPlayer().addObjectCard(new AttackCard(game, "Attack"));
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD, 0));
        game.update();
        assertTrue(game.getCurrentPlayer().getCurrentState() instanceof EndingTurnState);
        
        for(int i = 0; i < 4; i++)
            game.getCurrentPlayer().addObjectCard(new AttackCard(game, "Attack"));
        p = new DiscardingObjectCardState(game);
        
        game.getCurrentPlayer().setObjectCardUsed(false);
        game.getCurrentPlayer().setCurrentState(p);
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD, 0));
        p = p.update();
        assertTrue(p.update() instanceof EndingTurnState);
    }
    
    /**
     * Test the SectorState
     */
    @Test
    public void testSectorState() {
        GameState game = this.playToMoveDoneState(true, true, true);
        PlayerState p = new NoiseInAnySectorState(game);
        p.update();
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, hatchPoints.iterator().next()));
        assertTrue(p.update() != null);
        assertTrue(p.stillInGame());
    }
    
    /**
     * Test Spotlight state
     */
    @Test
    public void testSpotlightState() {
        GameState game = this.playToMoveDoneState(true, true, true);
        game.getCurrentPlayer().setCurrentState(new SpotlightCardState(game));
        game.update();
        
        assertTrue(game.getCurrentPlayer().getCurrentState().stillInGame());
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, hatchPoints.iterator().next()));
        game.getCurrentPlayer().setStateBeforeSpotlightCard(new MoveDoneState(game));
        game.update();
        assertTrue( findGameCommandInQueue(game, InfoOpcode.INFO_SPOTLIGHT) );
    }
    
    /**
     * Test the exception thrown while choosing an impossible map position
     */
    @Test(expected=IllegalStateOperationException.class)
    public void testSectorStateFailure() {
        GameState game = this.playToMoveDoneState(true, true, true);
        PlayerState p = new NoiseInAnySectorState(game);
        p.update();
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, new Point(900,900)));
        p.update();
    }
    
    /**
     * Test the exception thrown if the user tries a wrong command
     */
    @Test(expected=IllegalStateOperationException.class)
    public void testSectorStateFailure2() {
        GameState game = this.playToMoveDoneState(true, true, true);
        DangerousCard c = new NoiseInAnySectorCard(game);
        PlayerState p = c.doAction();
        p.update();
        game.enqueuePacket(new GameCommand(GameOpcode.CMD_CS_ATTACK));
        p.update();
    }

    /**
     * Test a human attack
     */
    @Test
    public void testHumanAttack() {
        /** play as an alien till reaching move done state */
        GameState game = this.playToMoveDoneState(true, true, true);

        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_ATTACK) );
        clearMessageQueue(game);
        game.update();

        /** Humans cannot attack! */
        assertFalse( findGameCommandInQueue(game, InfoOpcode.INFO_PLAYER_ATTACKED) );
    }

    /**
     * Try to draw a dangerous card
     */
    @Test
    public void testDrawDangerousCard() {
        GameState game = playToMoveDoneState(false, false, true);

        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_DRAW_DANGEROUS_CARD) );
        clearMessageQueue(game);
        game.update();
    }
    
    /**
     * Test and invalid action in MovingState
     */
    @Test(expected=IllegalStateOperationException.class)
    public void testInvalidActionInMovingState() {
        GameState game = playToMoveDoneState(false, false, true);
    
        /** move to moving state */
        game.update();
        
        /** drop previous messages */
        clearMessageQueue(game);
        
        assertTrue(GameOpcode.CMD_CS_DISCARD_OBJECT_CARD.toString().length() > 0);
        /** send an invalid position */
        game.enqueuePacket( new GameCommand(GameOpcode.CMD_CS_DISCARD_OBJECT_CARD) );
        PlayerState p = game.getCurrentPlayer().getCurrentState();
        p.update();
    }
    
    /** 
     * Test last thing did
     */
    @Test
    public void testLastThing() {
        GameState game = playToMoveDoneState(false, false, true);
        game.setLastThingDid(GameState.LastThings.GONE_OFFLINE);
        assertEquals(game.debugGetLastThingDid(),GameState.LastThings.GONE_OFFLINE);
    }
        
    /** Check if number of players in sector() gives the right value in a blank sector
     * 
     */
    @Test
    public void testNumberOfPlayersInSector() {
        GameState game = new GameState("YES", MAP_ID, NUMBER_OF_PLAYERS, START_ID, true);

        /** move to moving state */
        game.update();

        /** drop previous messages */
        clearMessageQueue(game);

        assertTrue( game.getNumberOfPlayersInSector( new Point(0, 2) ) == 0);
    }
    
    /** Test if the game manager sets this game to not runnning after only one player remained
     * 
     */
    @Test
    public void testEndGameAfterDisconnect() {
        GameManager gm = new GameManager();
        ClientConn c1 = new ServerToClientMock();
        ClientConn c2 = new ServerToClientMock();
        c1.run();
        c2.run();
        gm.addPlayer(new Client(c1, gm));
        gm.addPlayer(new Client(c2, gm));
        
        GameState game = new GameState(gm, 1);
        game.update();
        
        c1.disconnect();
        game.update();
        assertFalse( gm.isRunning() );
    }
       
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

        /** move to moving state */
        game.update();

        /** drop previous messages */
        clearMessageQueue(game);

        /** create set of dangerous, not dangerous and hatch sectors available for current player */
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
            /** send position and update game */
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

        /** Get first point among the available ones */
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
