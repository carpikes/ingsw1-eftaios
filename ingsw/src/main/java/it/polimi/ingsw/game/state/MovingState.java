/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.hatch.HatchCard;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Point;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class MovingState extends PlayerState {

    private static final Logger LOG = Logger.getLogger(MovingState.class.getName());
    private Set< Point > availableSectors;
    
    public MovingState(GameState state) {
        super(state);
        availableSectors = state.getCellsWithMaxDistance();
    }
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        GamePlayer player = gameState.getCurrentPlayer();
        NetworkPacket packet = gameState.getPacketFromQueue();
        GameMap map = gameState.getMap();
        
        PlayerState nextState = this;
        
        // If we actually received a command from the client...
        if( packet != null ) {
            // If the user said he wanted to move..
            if( packet.getOpcode() == GameCommand.CMD_CS_MOVE ) {
                Point chosenPos = (Point)packet.getArgs()[0];
                if(availableSectors.contains(chosenPos)) {
                    nextState = handleMove(player, map, chosenPos);
                } else {
                    //invalid position
                    player.sendPacket(GameCommand.CMD_SC_MOVE_INVALID);
                }
            } else if( packet.getOpcode() == GameCommand.CMD_CS_USE_OBJ_CARD ) {
                gameState.startUsingObjectCard( (ObjectCard)packet.getArgs()[0] );
            } else {
                throw new IllegalStateOperationException("You can only move. Discarding command.");
            }
        }
        
        return nextState;
    }

    private PlayerState handleMove(GamePlayer player, GameMap map,
            Point chosenPos) {
        PlayerState nextState;
        gameState.rawMoveTo(player.getCurrentPosition(), chosenPos);
        
        // notify all players that current players has just moved
        gameState.addToOutputQueue( GameCommand.INFO_HAS_MOVED );
        
        Sector sector = map.getSectorAt( player.getCurrentPosition() );
        // If we are on an hatch sector, draw an hatch card and act accordingly
        if( sector.getId() == SectorBuilder.HATCH ) {
            nextState = drawHatchCard( gameState );
        } else {
            nextState =  new MoveDoneState(gameState);
        }
        return nextState;
    }

    private PlayerState drawHatchCard(GameState gameState) {
        GamePlayer player = gameState.getCurrentPlayer();
        GameMap map = gameState.getMap();
        
        // set current cell as no more accessible
        map.setType( player.getCurrentPosition(), SectorBuilder.USED_HATCH );
        gameState.addToOutputQueue( new NetworkPacket( GameCommand.INFO_USED_HATCH, player.getCurrentPosition() ) );
        
        return HatchCard.getRandomCard().getNextState( gameState );
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}

}
