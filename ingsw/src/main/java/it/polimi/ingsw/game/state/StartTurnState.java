/**
 * 
 */
package it.polimi.ingsw.game.state;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.InfoOpcode;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michele
 * @since 25 May 2015
 */
public class StartTurnState extends PlayerState {
    private static final Logger LOG = Logger.getLogger(StartTurnState.class.getName());
        public StartTurnState(GameState state) {
            super(state);
                LOG.log(Level.FINE, "Constructor");
                
                mGamePlayer.resetValues();
                
                // tell everybody I'm starting playing!
                state.broadcastPacket( new GameCommand(InfoOpcode.INFO_START_TURN, state.getTurnId()) );
        }
    
        /* (non-Javadoc)
         * @see it.polimi.ingsw.game.state.State#update()
         */
        @Override
        public PlayerState update() {       
            return new MovingState(mGameState);
        }
    
        /* (non-Javadoc)
         * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
         */
        @Override
        public boolean stillInGame() {
            return true;
        }
    
        /* (non-Javadoc)
         * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
         */
        @Override
        protected void buildAndSendAvailableCommands() {
            
        }
}
