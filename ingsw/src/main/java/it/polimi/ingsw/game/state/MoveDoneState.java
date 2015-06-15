package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.dangerous.DangerousCardBuilder;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** MoveDoneState
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class MoveDoneState extends PlayerState {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(MoveDoneState.class.getName());

    /** Constructor
     *
     * @param state Game State
     */
    public MoveDoneState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        /** tell the client it has to choose what to do after moving */
        buildAndSendAvailableCommands();
    }

    /** Build and send available commands
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    @Override
    protected void buildAndSendAvailableCommands() {
        GameMap map = mGameState.getMap();
        ArrayList<ViewCommand> availableCommands = new ArrayList<>();


        if(mGamePlayer.isAlien())
            availableCommands.add(new ViewCommand(ViewOpcode.CMD_ATTACK));

        if(map.getSectorAt( mGamePlayer.getCurrentPosition() ).getId() == SectorBuilder.DANGEROUS && mGamePlayer.shouldDrawDangerousCard())
            availableCommands.add(new ViewCommand(ViewOpcode.CMD_DRAWDANGEROUSCARD));
        else
            availableCommands.add(new ViewCommand(ViewOpcode.CMD_ENDTURN));

        addObjectCardIfPossible(availableCommands);
        sendAvailableCommands(availableCommands);
    }

    /** Update the game
     * @see it.polimi.ingsw.game.state.State#update()
     * @return New player state
     */
    @Override
    public PlayerState update() {
        GameCommand packet = mGameState.getPacketFromQueue();
        GameMap map = mGameState.getMap();

        PlayerState nextState = this;

        /** If we actually received a command from the client... */
        if( packet != null ) {
            /** if you used an object card... */
            if( packet.getOpcode() == GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD  && mGamePlayer.getNumberOfUsableCards() > 0) {
                nextState = useObjectCard(this, packet);
                /** if you attacked.. */
            } else if( mGamePlayer.isAlien() && packet.getOpcode() == GameOpcode.CMD_CS_ATTACK ) {
                mGameState.attack( mGamePlayer.getCurrentPosition() );
                nextState = new EndingTurnState(mGameState);
            } else if( map.getSectorAt( mGamePlayer.getCurrentPosition() ).getId() == SectorBuilder.DANGEROUS && mGamePlayer.shouldDrawDangerousCard() ) {
                /** DANGEROUS: either draw a card OR attack */
                if( packet.getOpcode() == GameOpcode.CMD_CS_DRAW_DANGEROUS_CARD ) {
                    nextState = drawDangerousCard( );
                } else {
                    throw new IllegalStateOperationException("Discarding command.");
                }
            } else {
                if( packet.getOpcode() == GameOpcode.CMD_CS_END_TURN ) {
                    nextState = new NotMyTurnState(mGameState);
                } else {
                    throw new IllegalStateOperationException("You can only attack or pass. Discarding command.");
                }
            }
        }

        return nextState;
    }

    /** Draw a dangerous card
     * @return
     */
    private PlayerState drawDangerousCard( ) {                
        return DangerousCardBuilder.getRandomCard(mGameState).doAction( );
    }

    /** Is the player still in game?
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     * @return True if the player is still in game
     */
    @Override
    public boolean stillInGame() {
        return true;
    }
}
