package it.polimi.ingsw.game;

import it.polimi.ingsw.game.network.GameInfoContainer;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.PlayerState;
import it.polimi.ingsw.game.player.Role;
import it.polimi.ingsw.game.player.RoleFactory;
import it.polimi.ingsw.server.Client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** Class representing the current state of the game. It holds the effective rules of the game and verify whether each action is valid or not.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 21, 2015
 */
public class GameState {
    private final Queue<NetworkPacket> mEventQueue;
    private final GameMap mMap;
    private final List<GamePlayer> mPlayers;
    private int mTurnId = 0;
    
    public GameState(int mapId, List<Client> clients) {
        mMap = GameMap.createFromId(mapId);
        mEventQueue = new LinkedList<>();
        
        mPlayers = new ArrayList<>();
        List<Role> roles = RoleFactory.generateRoles(clients.size());
        
        for(int i = 0;i<clients.size(); i++) {
            Role role = roles.get(i);
            GamePlayer player = new GamePlayer(role, mMap.getStartingPoint(role));
            
            mPlayers.add(player);
        }
    }
    
    public void update() {
        // get action from queue
        // process it
        // change state of current player if necessary
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
                    mPlayers.get(mTurnId).setCurrentState(PlayerState.START_OF_TURN);
                    
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
        GameInfoContainer info = new GameInfoContainer(userList, mPlayers.get(i).isHuman());
        return info;
    }

    public List<GamePlayer> getPlayers() {
        return mPlayers;
    }
}
