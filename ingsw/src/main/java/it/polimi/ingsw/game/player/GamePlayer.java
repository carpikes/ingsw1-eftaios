package it.polimi.ingsw.game.player;

import it.polimi.ingsw.server.Client;

import java.awt.Point;

/**
 *
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 19, 2015
 */
public class GamePlayer {
    
    /** Position on board */
    private Point position;
    
    /** Defense enabled? (defense card used or not) */
    private boolean defense;
    
    /** How many sectors can I cross in total? */
    private int maxMoves;
    
    /** Alien or human? */
    private Role role; 
    
    /** Handle connection to server */
    private Client client; 
    
    /** Current state in game for the player */
    private PlayerState currentState;
    
    public GamePlayer( Point startPosition ) {
        // playerstate
        defense = false;
        maxMoves = role.getMaxMoves();
        position = startPosition; 
    }

    public PlayerState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PlayerState currentState) {
        this.currentState = currentState;
    }

    public boolean isDefenseEnable() {
        return defense;
    }

    public void setDefense(boolean defense) {
        this.defense = defense;
    }

    /**
     * @return
     */
    public int getMaxMoves() {
        return maxMoves;
    }

    /**
     * @return
     */
    public Point getCurrentPosition() {
        return position;
    }

    /**
     * @param distance
     * @return
     */
    public boolean isValidDistance(int distance) {
        return (distance > 0 && distance <= this.getMaxMoves());
    }

    /**
     * @param destination
     */
    public void setCurrentPosition(Point position) {
        this.position = position;
    }
}
