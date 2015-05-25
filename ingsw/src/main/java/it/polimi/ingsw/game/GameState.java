package it.polimi.ingsw.game;

import it.polimi.ingsw.game.network.GameInfoContainer;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.Role;
import it.polimi.ingsw.game.player.RoleFactory;
import it.polimi.ingsw.game.state.State;
import it.polimi.ingsw.server.Client;
import it.polimi.ingsw.server.GameManager;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
        
        State nextState = player.getCurrentState().update(this);
        player.setCurrentState(nextState);
        
       /*
        
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
           

        
        */
        
        // TODO notifica modifiche a tutti
        gameManager.broadcastPacket( new NetworkPacket(GameCommand.CMD_SC_UPDATE_LOCAL_INFO, null) );
    }

    /*private void attack() {
        
    }*/
    
    //private void getObjectCard(GamePlayer player) {
        /*ObjectCard newCard = null;
        
        // TODO: create object card
        if( player.getNumberOfCards() < 3 ) {
            player.getObjectCards().add( newCard );
            getCurrentPlayer().sendPacket( new NetworkPacket(GameCommands.CMD_SC_OBJECT_CARD_OBTAINED, null tipo di carta ottenuto) );
            
            player.setCurrentState( PlayerState.ENDING_TURN );
        } else {
            getCurrentPlayer().sendPacket( GameCommands.CMD_SC_DISCARD_OBJECT_CARD );
        }*/
   // }
    
    /**
     * @param gameState
     * @return 
     */
    // TODO to be implemented
    public State startUsingObjectCard() {
       return null;
    }

    public void removePlayer( int id ) {
        noMorePlayingPlayers.add( mPlayers.remove(id) );
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

    public NetworkPacket getPacketFromQueue( ) {
    	synchronized(mEventQueue) {
            return mEventQueue.poll();
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
    
    public GameInfoContainer buildInfoContainer(String[] userList, int i) {
        GameInfoContainer info = new GameInfoContainer(userList, mPlayers.get(i).isHuman(), mMap);
        return info;
    }

    public List<GamePlayer> getPlayers() {
        return mPlayers;
    }

    /**
     * @param currentPosition
     * @return 
     */
    public State attack(Point currentPosition) {
        return null;
    }
    
    /**
     * @param player
     * @return
     */
    public State getObjectCard( ) {
        // TODO Auto-generated method stub
        return null;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
