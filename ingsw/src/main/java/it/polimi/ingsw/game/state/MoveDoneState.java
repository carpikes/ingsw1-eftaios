/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.dangerous.DangerousCardBuilder;
import it.polimi.ingsw.game.card.object.ObjectCard;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.network.GameViewOpcode;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class MoveDoneState extends PlayerState {
    
    public MoveDoneState(GameState state, GamePlayer player) {
        super(state, player);

        // tell the client it has to choose what to do after moving
        mGameState.sendPacketToCurrentPlayer( GameOpcode.CMD_SC_MOVE_DONE );
    }
    
    @Override
    protected void buildAndSendAvailableCommands() {
        GameMap map = mGameState.getMap();
        ArrayList<GameViewCommand> availableCommands = new ArrayList<>();
        availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_ENABLEMAPVIEW, mGamePlayer.getCurrentPosition(), mGamePlayer.getMaxMoves()));
        
        if(!mGamePlayer.isObjectCardUsed())
            availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_CHOOSEOBJECTCARD));
        
        if(mGamePlayer.isAlien())
            availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_ATTACK));
        
        if(map.getSectorAt( mGamePlayer.getCurrentPosition() ).getId() == SectorBuilder.DANGEROUS)
            availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_DRAWDANGEROUSCARD));
        else
            availableCommands.add(new GameViewCommand(GameViewOpcode.CMD_ENDTURN));
            
        sendAvailableCommands(availableCommands);
    }

    private static final Logger LOG = Logger.getLogger(MoveDoneState.class.getName());
    
    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.state.State#update()
     */
    @Override
    public PlayerState update() {
        GameCommand packet = mGameState.getPacketFromQueue();
        GameMap map = mGameState.getMap();

        PlayerState nextState = this;

        // If we actually received a command from the client...
        if( packet != null ) {
            if( packet.getOpcode() == GameOpcode.CMD_CS_USE_OBJ_CARD ) {
                nextState = useObjectCard(this, packet);
            } else if( mGamePlayer.isAlien() && packet.getOpcode() == GameOpcode.CMD_CS_ATTACK ) {
                mGameState.attack( mGamePlayer.getCurrentPosition() );
                nextState = new EndingTurnState(mGameState, mGamePlayer);
            } else if( map.getSectorAt( mGamePlayer.getCurrentPosition() ).getId() == SectorBuilder.DANGEROUS ) {
                // DANGEROUS: either draw a card OR attack
                if( packet.getOpcode() == GameOpcode.CMD_CS_DRAW_DANGEROUS_CARD ) {
                    nextState = drawDangerousCard( );
                } else {
                    throw new IllegalStateOperationException("Discarding command.");
                }
            } else {
                // NOT DANGEROUS: either attack or pass
                if( packet.getOpcode() == GameOpcode.CMD_CS_END_TURN ) {
                    nextState = new NotMyTurnState(mGameState, mGamePlayer);
                } else {
                    throw new IllegalStateOperationException("You can only attack or pass. Discarding command.");
                }
            }
        }
        
        return nextState;
    }

    /**
     * @param data.mGameState
     * @return 
     */
    private PlayerState drawDangerousCard( ) {                
        return DangerousCardBuilder.getRandomCard(mGameState, mGamePlayer).doAction( );
    }
    
    @Override
	public boolean stillInGame() {
		return true;
	}
}
