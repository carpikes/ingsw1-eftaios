package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.player.playerstate.AttackingState;
import it.polimi.ingsw.game.player.playerstate.AwayState;
import it.polimi.ingsw.game.player.playerstate.DangerousCardState;
import it.polimi.ingsw.game.player.playerstate.HatchCardState;
import it.polimi.ingsw.game.player.playerstate.LoserState;
import it.polimi.ingsw.game.player.playerstate.MovingState;
import it.polimi.ingsw.game.player.playerstate.NotPlayingState;
import it.polimi.ingsw.game.player.playerstate.ObjectCardState;
import it.polimi.ingsw.game.player.playerstate.PlayerState;
import it.polimi.ingsw.game.player.playerstate.WinnerState;
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
    
    /** Static references to all possible states */
    private static AttackingState attackingState;
    private static AwayState awayState;
    private static DangerousCardState dangerousCardState;
    private static HatchCardState hatchCardState;
    private static LoserState loserState;
    private static MovingState movingState;
    private static NotPlayingState notPlayingState;
    private static ObjectCardState objectCardState;
    private static WinnerState winnerState;
    
    public GamePlayer( Point startPosition ) {
        // playerstate
        defense = false;
        maxMoves = role.getMaxMoves();
        position = startPosition; 
        
        initializeStates();
    }

    private void initializeStates() {
        attackingState = new AttackingState();
        awayState = new AwayState();
        dangerousCardState = new DangerousCardState();
        hatchCardState = new HatchCardState();
        loserState = new LoserState();
        movingState = new MovingState();
        notPlayingState = new NotPlayingState();
        objectCardState = new ObjectCardState();
        winnerState = new WinnerState();
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
    
    /* Delegate all possible actions to state object */
    public void attack() {
        currentState.attack();
    }
    
    public void move() {
        currentState.move();
    }
    
    public void drawCard() {
        currentState.drawCard();
    }
    
    public void useObjectCard() {
        currentState.useObjectCard();
    }
}
