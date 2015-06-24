package it.polimi.ingsw.game.state;

import it.polimi.ingsw.common.GameCommand;
import it.polimi.ingsw.common.GameOpcode;
import it.polimi.ingsw.common.InfoOpcode;
import it.polimi.ingsw.game.GameState;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Loser State
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class LoserState extends PlayerState {
    
    /** Logger */
    private static final Logger LOG = Logger.getLogger(LoserState.class.getName());

    /** Constructor
     *
     * @param state Game State
     * @param loserPlayer Who lose?
     */
    public LoserState(GameState state, Integer loserPlayer) {
        super(state);
        LOG.log(Level.FINE, "Constructor");
        
        state.broadcastPacket( new GameCommand(InfoOpcode.INFO_LOSER, loserPlayer));
        state.sendPacketToPlayer(loserPlayer, new GameCommand(GameOpcode.CMD_SC_LOSE));
    }

    /** Update the game
     * 
     * @see it.polimi.ingsw.game.state.PlayerState#update()
     * @return New player state
     */
    @Override
    public PlayerState update() {
        return this;
    }

    /** Is the player still in game?
     * 
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     * @return True if the player is still in game
     */
    @Override
    public boolean stillInGame() {
        return false;
    }

    /** Build and send available commands
     * 
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    @Override
    protected void buildAndSendAvailableCommands() {
        /** No commands */
    }
}
