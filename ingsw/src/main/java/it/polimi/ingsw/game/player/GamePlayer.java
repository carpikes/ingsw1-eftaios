package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.server.Client;

import java.awt.Point;
import java.util.ArrayList;

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
    
    /** Object card already used in current turn */
    private boolean objectCardUsed;
    
    /** Object cards the player owns */
    private ArrayList < ObjectCard > objectCards;
    
    /** How many sectors can I cross in total? */
    private int maxMoves;
    
    /** Alien or human? */
    private final Role role;
    
    /** Current state in game for the player */
    private PlayerState currentState;
    
    /** Connection to client */
    private Client connection;
    
    public GamePlayer( Role playerRole, Point startPosition ) {
        objectCards = new ArrayList<>();
        role = playerRole;
        currentState = PlayerState.NOT_MY_TURN;
        defense = false;
        maxMoves = role.getMaxMoves();
        position = startPosition; 
        objectCardUsed = false;
        // connection = 
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

    /**
     * @param p
     */
    public void notifyChange(GamePlayer p) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @return
     */
    public Role getRole() {
       return role;
    }

    /**
     * @return
     */
    public boolean isAlien() {
        return role instanceof Alien;
    }

    /**
     * @return
     */
    public boolean isHuman() {
        return role instanceof Human;
    }

    public Client getConnection() {
        return connection;
    }

    /**
     * @param i
     */
    public void sendPacket(int opcode) {
        connection.sendPacket(opcode);
    }

    /**
     * @param networkPacket
     */
    public void sendPacket(NetworkPacket networkPacket) {
        connection.sendPacket(networkPacket);
    }

    public ArrayList<ObjectCard> getObjectCards() {
        return objectCards;
    }
    
    public int getNumberOfCards() {
        return objectCards.size();
    }

    public boolean isObjectCardUsed() {
        return objectCardUsed;
    }

    public void setObjectCardUsed(boolean objectCardUsed) {
        this.objectCardUsed = objectCardUsed;
    }
    
    
}
