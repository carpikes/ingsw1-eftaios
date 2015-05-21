package it.polimi.ingsw.game;

import it.polimi.ingsw.game.player.GamePlayer;

import java.util.ArrayList;

/** Class representing the current state of the game. The GameLogic on the server and every player
 *  has an instance of it. It holds the effective rules of the game and verify whether each action is valid or not.
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 21, 2015
 */
public class GameState {
    
    private GameMap map;
    private ArrayList<GamePlayer> players;
    private int turnId = 0;
    
    public GameState( GameMap map ) {
        this.map = map;
        players = new ArrayList<>();
    }
    
    public void update() {
        // update game
    }
    
    public GamePlayer getCurrentPlayer() {
        return players.get(turnId);
    }

    public GameMap getMap() {
        return map;
    }

}
