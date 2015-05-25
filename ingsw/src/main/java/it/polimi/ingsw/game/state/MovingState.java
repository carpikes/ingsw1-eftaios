/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.PlayerState;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.util.Random;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class MovingState implements State {

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public State update( GameState gameState ) {
        GamePlayer player = gameState.getCurrentPlayer();
        NetworkPacket packet = gameState.getPacketFromQueue();
        GameMap map = gameState.getMap();
        
        State nextState = this;
        
        // If we actually received a command from the client...
        if( packet != null ) {
            // If the user said he wanted to move..
            if( packet.getOpcode() == GameCommands.CMD_CS_MOVE ) {
                // TODO: MUOVI EFFETTIVAMENTE!
                // moveTo()
                
                Sector sector = map.getSectorAt( player.getCurrentPosition() );
                // If we are on an hatch sector, draw an hatch card and act accordingly
                if( sector.getId() == SectorBuilder.HATCH ) {
                    nextState = drawHatchCard( gameState );
                } else {
                    // tell the client it has choose what to do after moving
                    player.sendPacket( GameCommands.CMD_SC_MOVE_DONE );
                    nextState =  new MoveDoneState();
                }
            } else if( packet.getOpcode() == GameCommands.CMD_CS_USE_OBJ_CARD ) {
                // TODO where should I put this?
                nextState = gameState.startUsingObjectCard();
            } else {
                throw new IllegalStateOperationException("You can only move. Discarding command.");
            }
        }
        
        return nextState;
    }

    private State drawHatchCard(GameState gameState) {
        Random generator = new Random();
        GamePlayer player = gameState.getCurrentPlayer();
        GameMap map = gameState.getMap();
        
        State nextState;
        
        // set current cell as no more accessible
        map.setType( player.getCurrentPosition(), SectorBuilder.USED_HATCH );
        
        // draw an hatch card and choose accordingly
        boolean isRed = generator.nextBoolean();
        
        if( isRed ) {
            // OUCH! You cannot use that hatch!
            player.sendPacket( GameCommands.CMD_SC_END_OF_TURN );
            nextState = new EndingTurnState();
        } else {
            player.sendPacket( GameCommands.CMD_SC_WIN );
            nextState = new WinnerState();
            
            // remove player
            gameState.removePlayer( player.getId() );
        }
        
        return nextState;
    }

}
