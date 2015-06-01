/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.dangerous.DangerousCard;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class MoveDoneState extends PlayerState {
    
    public MoveDoneState(GameState state) {
        super(state);

        // tell the client it has to choose what to do after moving
        state.getCurrentPlayer().sendPacket( GameCommand.CMD_SC_MOVE_DONE );
    }

    private static final Logger LOG = Logger.getLogger(MoveDoneState.class.getName());
    
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
            if( packet.getOpcode() == GameCommand.CMD_CS_USE_OBJ_CARD ) {
                gameState.startUsingObjectCard( (ObjectCard)packet.getArgs()[0] );
            } else {
                // DANGEROUS: either draw a card OR attack
                if( map.getSectorAt( player.getCurrentPosition() ).getId() == SectorBuilder.DANGEROUS ) { 
                    if( packet.getOpcode() == GameCommand.CMD_CS_DRAW_DANGEROUS_CARD ) {
                        nextState = drawDangerousCard( gameState );
                    } else if( player.isAlien() && packet.getOpcode() == GameCommand.CMD_CS_ATTACK ) {
                        gameState.attack( player.getCurrentPosition() );
                        nextState = new EndingTurnState(gameState);
                    } else {
                        throw new IllegalStateOperationException("Discarding command.");
                    }
                } else {
                    // NOT DANGEROUS: either attack or pass
                    if( packet.getOpcode() == GameCommand.CMD_CS_NOT_MY_TURN ) {
                        nextState = new NotMyTurnState(gameState);
                    } else if( packet.getOpcode() == GameCommand.CMD_CS_ATTACK ) {
                        gameState.attack( player.getCurrentPosition() );
                        nextState = new EndingTurnState(gameState);
                    } else {
                        throw new IllegalStateOperationException("You can only attack or pass. Discarding command.");
                    }
                }
            }
        }
        
        return nextState;
    }

    /**
     * @param gameState
     * @return 
     */
    private PlayerState drawDangerousCard(GameState gameState) {                
        return DangerousCard.getRandomCard().doAction( gameState );
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}
}
