/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.sector.SectorBuilder;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class MoveDoneState implements State {

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
            if( packet.getOpcode() == GameCommand.CMD_CS_USE_OBJ_CARD ) {
                // TODO where should I put this?
                nextState = gameState.startUsingObjectCard();
            } else {
                // DANGEROUS: either draw a card OR attack
                if( map.getSectorAt( player.getCurrentPosition() ).getId() == SectorBuilder.DANGEROUS ) { 
                    if( packet.getOpcode() == GameCommand.CMD_CS_DRAW_DANGEROUS_CARD ) {
                        nextState = drawDangerousCard( gameState );
                    } else if( packet.getOpcode() == GameCommand.CMD_CS_ATTACK ) {
                        nextState = gameState.attack( player.getCurrentPosition() );
                    } else {
                        throw new IllegalStateOperationException("You can only attack or draw a dangerous sector card. Discarding command.");
                    }
                } else {
                    // NOT DANGEROUS: either attack or pass
                    if( packet.getOpcode() == GameCommand.CMD_CS_NOT_MY_TURN ) {
                        nextState = new NotMyTurnState();
                    } else if( packet.getOpcode() == GameCommand.CMD_CS_ATTACK ) {
                        nextState = gameState.attack( player.getCurrentPosition() );
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
    private State drawDangerousCard(GameState gameState) {
        
        /*Random generator = new Random();
        int index = generator.nextInt(3);
        
        switch( index ) {
        case 0: // noise in your sector
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.NOISE_IN_YOUR_SECTOR) );
            gameManager.broadcastPacket( new NetworkPacket(GameCommands.CMD_SC_NOISE, getCurrentPlayer().getCurrentPosition()) );
            
            // --> preleva carta oggetto
            getObjectCard(player);
            break;
            
        case 1: // noise in any sector
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.NOISE_IN_ANY_SECTOR) );
            player.setCurrentState( PlayerState.USING_DANGEROUS_CARD );
            break;
            
        case 2: // silence
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.SILENCE) );
            gameManager.broadcastPacket( GameCommands.CMD_SC_SILENCE );
            
            player.setCurrentState( PlayerState.ENDING_TURN );
            break;
        }*/
        
        return null;
    }

}
