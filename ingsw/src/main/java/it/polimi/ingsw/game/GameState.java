package it.polimi.ingsw.game;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.player.GamePlayer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/** Class representing the current state of the game. The GameLogic on the server and every player
 *  has an instance of it. It holds the effective rules of the game and verify whether each action is valid or not.
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
        // update game
    }
    
    public synchronized GamePlayer getCurrentPlayer() {
        if(mPlayers != null && mTurnId < mPlayers.size())
            return mPlayers.get(mTurnId);
        
        return null;
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
}
