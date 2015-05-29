/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.DangerousCard;
import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class MoveDoneState extends PlayerState {
    
    public MoveDoneState(GameState state) {
        super(state);
        // TODO Auto-generated constructor stub
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
        GamePlayer player = gameState.getCurrentPlayer();
        
        // get a random dangerous card
        Random generator = new Random();
        int index = generator.nextInt( DangerousCard.values().length );
        DangerousCard card = DangerousCard.getCardAt(index);
        
        PlayerState nextState;
        
        // according to type, choose what to do next
        switch( card ) {
        case NOISE_IN_YOUR_SECTOR: // noise in your sector
            player.sendPacket( new NetworkPacket(GameCommand.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.NOISE_IN_YOUR_SECTOR) );
            gameState.getGameManager().broadcastPacket( new NetworkPacket(GameCommand.CMD_SC_NOISE, player.getCurrentPosition()) );
            
            nextState = gameState.getObjectCard( );
            break;
            
        case NOISE_IN_ANY_SECTOR: // noise in any sector
            player.sendPacket( new NetworkPacket(GameCommand.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.NOISE_IN_ANY_SECTOR) );
            nextState = new NoiseInAnySectorState(gameState);
            break;
            
        case SILENCE: // silence
            player.sendPacket( new NetworkPacket(GameCommand.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.SILENCE) );
            gameState.getGameManager().broadcastPacket( GameCommand.CMD_SC_SILENCE );
            
            nextState = new EndingTurnState(gameState);
            break;
            
        default:
            LOG.log(Level.SEVERE, "Unknown dangerous card. You should never get here!");
            nextState = new EndingTurnState(gameState); // end gracefully
        }
        
        return nextState;
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}
}
