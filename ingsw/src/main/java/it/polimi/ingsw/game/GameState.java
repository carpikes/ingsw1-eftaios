package it.polimi.ingsw.game;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.PlayerState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/** Class representing the current state of the game. It holds the effective rules of the game and verify whether each action is valid or not.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 21, 2015
 */
public class GameState {
    private Queue<NetworkPacket> mEventQueue;
    private GameMap mMap;
    private ArrayList<GamePlayer> mPlayers;
    private int mTurnId = 0;
    
    public GameState(GameMap map) {
        mMap = map;
        mEventQueue = new LinkedList<>();
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

    public synchronized void setPlayers(ArrayList<GamePlayer> players) {
        this.mPlayers = players;
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
                if( mPlayers.get( i % mPlayers.size() ).getCurrentState() == PlayerState.END_OF_TURN ) {
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

    /**
     * @return
     */
    public ArrayList<GamePlayer> getPlayers() {
        return mPlayers;
    }
}
