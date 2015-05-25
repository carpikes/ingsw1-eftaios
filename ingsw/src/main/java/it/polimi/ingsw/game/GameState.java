package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.IllegalStateOperationException;
import it.polimi.ingsw.game.card.DangerousCardType;
import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.game.command.AttackCommand;
import it.polimi.ingsw.game.command.AwakeCommand;
import it.polimi.ingsw.game.command.ChoosePositionCommand;
import it.polimi.ingsw.game.command.Command;
import it.polimi.ingsw.game.command.CommandBuilder;
import it.polimi.ingsw.game.command.DiscardObjectCard;
import it.polimi.ingsw.game.command.DrawDangerousCardCommand;
import it.polimi.ingsw.game.command.MoveCommand;
import it.polimi.ingsw.game.command.NotMyTurnCommand;
import it.polimi.ingsw.game.command.UseObjectCardCommand;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.GameInfoContainer;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.PlayerState;
import it.polimi.ingsw.game.player.Role;
import it.polimi.ingsw.game.player.RoleFactory;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.GameManager;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Class representing the current state of the game. It holds the effective rules of the game and verify whether each action is valid or not.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 21, 2015
 */
public class GameState {
    private static final Logger LOG = Logger.getLogger(GameState.class.getName());
    
    private final GameManager gameManager;
    private final Queue<NetworkPacket> mEventQueue;
    private final GameMap mMap;
    private List<GamePlayer> mPlayers;
    private List<GamePlayer> noMorePlayingPlayers;
    private int mTurnId = 0;
    
    public GameState(GameManager mgr, int mapId, List<Client> clients) {
        GameMap tmpMap;
        try {
            tmpMap = GameMap.createFromId(mapId);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Missing map files: " + e.toString(), e);
            tmpMap = GameMap.generate();
        }
        mMap = tmpMap;
        
        gameManager = mgr;
        
        mEventQueue = new LinkedList<>();
        
        mPlayers = new ArrayList<>();
        noMorePlayingPlayers = new ArrayList<>();
        List<Role> roles = RoleFactory.generateRoles(clients.size());
        
        for(int i = 0;i<clients.size(); i++) {
            Role role = roles.get(i);
            GamePlayer player = new GamePlayer(role, mMap.getStartingPoint(role));
            
            mPlayers.add(player);
        }
    }
    
    // FIXME: separate commands refactoring
    // ACHTUNG: COMMANDS ARE NOT IMPLEMENTED YET!
    
    public void update() {
        GamePlayer player = getCurrentPlayer();
        
        NetworkPacket pkt = getPacketFromQueue();
        Command command = getCommandFromPacket(pkt);
        
        Random rnd = new Random();
        
        // Choose what to do according to current state
        switch( player.getCurrentState() ) {
        
        // invoked when the server need to notify that someone is the new current game player
        case START_TURN:
            // calculate the cells where the player can go
            ArrayList< Point > availablePoints = getCellsWithMaxDistance( player.getMaxMoves() );
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_START_TURN, availablePoints) );
            player.setCurrentState( PlayerState.MOVING );
            break;
            
        case MOVING:
            if( command instanceof MoveCommand ) {
                // after moving, check where I went to
                // move to hatch states when going on a hatch sector
                command.execute(this);
                
                Sector sector = mMap.getSectorAt( player.getCurrentPosition() );
                if( sector.isCrossable() && sector.getId() == SectorBuilder.HATCH ) {
                    // draw an hatch card and choose accordingly
                    mMap.setType( player.getCurrentPosition(), SectorBuilder.USED_HATCH );
                    
                    boolean isRed = rnd.nextBoolean();
                    
                    if( isRed ) {
                        player.setCurrentState( PlayerState.NOT_MY_TURN );
                        getCurrentPlayer().sendPacket( GameCommands.CMD_SC_END_OF_TURN );
                    } else {
                        player.setCurrentState( PlayerState.WINNER );
                        getCurrentPlayer().sendPacket( GameCommands.CMD_SC_WIN );
                        
                        // spostati al prossimo giocatore
                        noMorePlayingPlayers.add( mPlayers.remove(mTurnId) );
                    }
                    
                    sector = SectorBuilder.getSectorFor( SectorBuilder.USED_HATCH );
                } else {
                    // tell the client it has choose what to do after moving
                    getCurrentPlayer().sendPacket( GameCommands.CMD_SC_MOVE_DONE );
                    player.setCurrentState( PlayerState.MOVE_DONE );
                }
            } else if( command instanceof UseObjectCardCommand ) {
                startUsingObjectCard();
            } else {
                throw new IllegalStateOperationException("You can only move. Discarding command.");
            }
            break;
            
        case MOVE_DONE:
            
            if( command instanceof UseObjectCardCommand ) {
                startUsingObjectCard();
            } else {
                // DANGEROUS: either draw a card OR attack
                if( mMap.getSectorAt( player.getCurrentPosition() ).getId() == SectorBuilder.DANGEROUS ) {
                    if( command instanceof DrawDangerousCardCommand ) {
                        command.execute(this);
                        drawDangerousCard(player);
                    } else if( command instanceof AttackCommand ) {
                        attack();
                        player.setCurrentState( PlayerState.ENDING_TURN );
                    } else {
                        throw new IllegalStateOperationException("You can only attack or draw a dangerous sector card. Discarding command.");
                    }
                } else {
                    // NOT DANGEROUS: either attack or pass
                    if( command instanceof NotMyTurnCommand ) {
                        command.execute(this);
                        player.setCurrentState( PlayerState.NOT_MY_TURN );
                    } else if( command instanceof AttackCommand ) {
                        attack();
                        player.setCurrentState( PlayerState.ENDING_TURN );
                    } else {
                        throw new IllegalStateOperationException("You can only attack or pass. Discarding command.");
                    }
                }
            }
            break;
            
        // NOISE IN ANY SECTOR    
        case USING_DANGEROUS_CARD:
            if( command instanceof ChoosePositionCommand ) {
                // --->  getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCard.NOISE_IN_YOUR_SECTOR) );
                // ---> gameManager.broadcastPacket( new NetworkPacket(GameCommands.CMD_SC_NOISE, getCurrentPlayer().getCurrentPosition()) );
                command.execute(this);
                
                // --> preleva carta oggetto
                getObjectCard(player);
                break;
            } else {
                throw new IllegalStateOperationException("You can only choose a position here. Discarding packet.");
            }
        
        case DISCARDING_OBJECT_CARD:
            if( command instanceof DiscardObjectCard ) {
                // scarta la carta indicata dal commando
                command.execute(this);
                
                player.setCurrentState( PlayerState.ENDING_TURN );
            } else {
                throw new IllegalStateOperationException("You can only choose what object card to discard here. Discarding packet.");
            }
            break;
            
        case USING_OBJECT_CARD:
            player.setObjectCardUsed(true);
            break;
            
        case ENDING_TURN:
            if( command instanceof UseObjectCardCommand ) {
                startUsingObjectCard();
            } else if( command instanceof NotMyTurnCommand ) {
                command.execute(this);
                player.setCurrentState( PlayerState.NOT_MY_TURN );
            } else {
                throw new IllegalStateOperationException("You can only choose what object card to discard here. Discarding packet.");
            }
            break;
        
        case NOT_MY_TURN:    
        case WINNER:
        case LOSER:
            // ignore any communication (except for updating GameInfoContainer through broadcastPacket method below)
            break;
            
        case AWAY:
            if( command instanceof AwakeCommand ) {
                player.setCurrentState( PlayerState.NOT_MY_TURN );
            } else {
                throw new IllegalStateOperationException("You can only awake. Discarding command.");
            }
            break;
        default:
            throw new IllegalStateOperationException("Unknown state.");
        }
        
        // TODO notifica modifiche a tutti
        gameManager.broadcastPacket( new NetworkPacket(GameCommands.CMD_SC_UPDATE_LOCAL_INFO, null) );
    }

    private void drawDangerousCard(GamePlayer player) {
        Random generator = new Random();
        int index = generator.nextInt(3);
        
        switch( index ) {
        case 0: // noise in your sector
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardType.NOISE_IN_YOUR_SECTOR) );
            gameManager.broadcastPacket( new NetworkPacket(GameCommands.CMD_SC_NOISE, getCurrentPlayer().getCurrentPosition()) );
            
            // --> preleva carta oggetto
            getObjectCard(player);
            break;
            
        case 1: // noise in any sector
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardType.NOISE_IN_ANY_SECTOR) );
            player.setCurrentState( PlayerState.USING_DANGEROUS_CARD );
            break;
            
        case 2: // silence
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_DANGEROUS_CARD_DRAWN, DangerousCardType.SILENCE) );
            gameManager.broadcastPacket( GameCommands.CMD_SC_SILENCE );
            
            player.setCurrentState( PlayerState.ENDING_TURN );
            break;
        }
    }

    private void attack() {
        
    }
    
    private void getObjectCard(GamePlayer player) {
        ObjectCard newCard = null;
        
        // TODO: create object card
        if( player.getNumberOfCards() < 3 ) {
            player.getObjectCards().add( newCard );
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_OBJECT_CARD_OBTAINED, null /*tipo di carta ottenuto*/) );
            
            player.setCurrentState( PlayerState.ENDING_TURN );
        } else {
            getCurrentPlayer().sendPacket( GameCommands.CMD_SC_DISCARD_OBJECT_CARD );
        }
    }
    
    /**
     * @param gameState
     */
    private void startUsingObjectCard() {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param maxMoves
     * @return
     */
    private ArrayList<Point> getCellsWithMaxDistance(int maxMoves) {
        ArrayList<Point> points = new ArrayList<>();
        
        // TODO NOT IMPLEMENTED YET!
        points.add( new Point(0,0) );
        return points;
    }

    private NetworkPacket getPacketFromQueue( ) {
    	synchronized(mEventQueue) {
            return mEventQueue.poll();
        }
    }
    
    private Command getCommandFromPacket( NetworkPacket pkt ) {
        if( pkt == null )
            return null;
        
        return CommandBuilder.getCommandFromNetworkPacket(pkt);
    }
    
    private boolean executeCommand( Command command ) {
		if( command.isValid( this ) ) {
    		command.execute(this);
    		return true;
		} else {
			LOG.log( Level.WARNING, "Command " + command + " not valid in state: " + getCurrentPlayer().getCurrentState() );
			return false;
		}
    }
    
    public synchronized GamePlayer getCurrentPlayer() {
        if(mPlayers != null && mTurnId < mPlayers.size())
            return mPlayers.get(mTurnId);
        
        return null; // FIXME THROW EXCEPTION HERE!
    }

    public GameMap getMap() {
        return mMap;
    }
    
    public void queuePacket(NetworkPacket pkt) {
        synchronized(mEventQueue) {
            mEventQueue.add(pkt);
        }
    }
    
    public boolean hasGameEnded() {
        if( mPlayers == null || mPlayers.isEmpty() )
            return true;
        
        for( GamePlayer p : mPlayers )
            if( p.getCurrentState() == PlayerState.WINNER || p.getCurrentState() == PlayerState.LOSER )
                return false;
        
        return true;
    }
    
    public boolean moveToNextPlayer() {
        if( hasGameEnded() )
            return false;
        else {
            // check if there are players still playing
            for( int i = mTurnId; i < mTurnId + mPlayers.size(); ++i )
                if( mPlayers.get( i % mPlayers.size() ).getCurrentState() == PlayerState.NOT_MY_TURN ) {
                    mTurnId = i % mPlayers.size();
                    mPlayers.get(mTurnId).setCurrentState(PlayerState.START_TURN);
                    
                    return true;
                }
            
            return false;
        }
    }
    
    public void notifyChangesToAll() {
        for( GamePlayer p : mPlayers )
            p.notifyChange(p);
    }

    public GameInfoContainer buildInfoContainer(String[] userList, int i) {
        GameInfoContainer info = new GameInfoContainer(userList, mPlayers.get(i).isHuman(), mMap);
        return info;
    }

    public List<GamePlayer> getPlayers() {
        return mPlayers;
    }
}
