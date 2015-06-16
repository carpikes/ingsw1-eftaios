package it.polimi.ingsw.game.state;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.hatch.HatchCardBuilder;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.InfoOpcode;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Moving State
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 25 May 2015
 */
public class MovingState extends PlayerState {
    
    /** Logger */
    private static final Logger LOG = Logger.getLogger(MovingState.class.getName());

    /** Available sectors */
    private Set< Point > availableSectors;

    /** Constructor
     *
     * @param state Game State
     */
    public MovingState(GameState state) {
        super(state);
        LOG.log(Level.FINE, "Constructor");

        availableSectors = state.getCellsWithMaxDistance();

        buildAndSendAvailableCommands();
    }

    /** Build and send available commands
     * 
     * @see it.polimi.ingsw.game.state.PlayerState#buildAndSendAvailableCommands()
     */
    protected void buildAndSendAvailableCommands() {
        ArrayList<ViewCommand> availableCommands = new ArrayList<>();
        availableCommands.add(new ViewCommand(ViewOpcode.CMD_ENABLEMAPVIEW, mGamePlayer.getCurrentPosition(), mGamePlayer.getMaxMoves()));

        addObjectCardIfPossible(availableCommands);
        sendAvailableCommands(availableCommands);
    }

    /** Update the game
     * @see it.polimi.ingsw.game.state.PlayerState#update()
     * @return New player state
     */
    @Override
    public PlayerState update() {
        GameCommand packet = mGameState.getPacketFromQueue();
        GameMap map = mGameState.getMap();

        PlayerState nextState = this;

        /** If we actually received a command from the client... */
        if( packet != null ) {
            /** If the user said he wanted to move.. */
            if( packet.getOpcode() == GameOpcode.CMD_CS_CHOSEN_MAP_POSITION ) {
                Object obj = packet.getArgs()[0];
                if(obj != null && obj instanceof Point) {
                    Point chosenPos = (Point) obj;
                    if(chosenPos != null && availableSectors.contains(chosenPos)) {
                        nextState = handleMove(mGamePlayer, map, chosenPos);
                    } else {
                        /** invalid position */
                        mGameState.sendPacketToCurrentPlayer(GameOpcode.CMD_SC_MOVE_INVALID);
                    }
                } else
                    mGameState.sendPacketToCurrentPlayer(GameOpcode.CMD_SC_MOVE_INVALID);
            } else if( packet.getOpcode() == GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD && mGamePlayer.getNumberOfUsableCards() > 0) {
                nextState = useObjectCard(this, packet);

                /** This call is important (Adrenaline card!)  */
                availableSectors = mGameState.getCellsWithMaxDistance();
            } else {
                throw new IllegalStateOperationException("You can only move. Discarding command.");
            }
        }

        return nextState;
    }

    /** Handle a move
     *
     * @param player The player
     * @param map The map
     * @param chosenPos The chosen position
     * @return Next state
     */
    private PlayerState handleMove(GamePlayer player, GameMap map, Point chosenPos) {
        PlayerState nextState;
        mGameState.rawMoveTo(player, chosenPos);

        Sector sector = map.getSectorAt( player.getCurrentPosition() );
        /** If we are on an hatch sector, draw an hatch card and act accordingly */
        if( sector.getId() == SectorBuilder.HATCH )
            nextState = drawHatchCard( );
        else
            nextState = new MoveDoneState(mGameState);
        return nextState;
    }

    /** Draw an hatch card
     *
     * @return The hatch card
     */
    private PlayerState drawHatchCard( ) {
        GameMap map = mGameState.getMap();

        map.useHatch(mGamePlayer.getCurrentPosition());
        /** set current cell as no more accessible */
        mGameState.broadcastPacket( new GameCommand( InfoOpcode.INFO_USED_HATCH, mGamePlayer.getCurrentPosition() ) );

        return HatchCardBuilder.getRandomCard(mGameState).getNextState( );
    }

    /** Is the player still in game?
     *
     * @see it.polimi.ingsw.game.state.PlayerState#stillInGame()
     */
    @Override
    public boolean stillInGame() {
        return true;
    }

}
