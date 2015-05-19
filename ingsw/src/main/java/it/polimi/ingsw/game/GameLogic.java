package it.polimi.ingsw.game;

import it.polimi.ingsw.game.sector.Sector;

import java.util.ArrayList;

/** Game
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 19, 2015
 */
public class GameLogic {
    
    private enum States {
        READY,
        WAITING_FOR_MOVE, 
    }
    
	private ArrayList<GamePlayer> players;
	private int turnId = 0;
	
	private States curState;
	
    /** Set up game
     * 
     */
	public GameLogic() {
		players = new ArrayList<>(); // could be a circular linked list better? ( ptr(lastelement)++ -> first element)
		curState = States.READY;
	}
	
	/** This function is called (N times)/sec
	 * 
	 */
	public void gameUpdate() {
	    GamePlayer curPlayer = players.get(turnId);
	    
	    // What do I do?
	    switch(curState) {
            // Ready! Ask something to the player
    	    case READY:
    	        curPlayer.askSector("Where do you want to move?");
    	        // And change state, now I'm waiting a response
    	        curState = States.WAITING_FOR_MOVE;
    	        break;
    	    // Wait for a player response
    	    case WAITING_FOR_MOVE:
    	        Sector chosenSector = curPlayer.getLastChosenSector();
    	        
    	        if(chosenSector == null) // player hasn't choose yet
    	            return;
    	        
    	        // Player wants to move to chosenSector.
    	        if(canMoveTo(curPlayer, chosenSector)) {
    	            // move the player and change player
    	            moveToSector(curPlayer, chosenSector);
    	            
    	            if(curPlayer.getType() == GamePlayer.PLAYER_ALIEN && curPlayer.wantAttack())
    	                curPlayer.attack();
    	            
    	            
    	            chosenSector.doSectorAction(this,curPlayer);
    	            curPlayer.resetAttackState();
    	            
    	            // reset "FSA" state
    	            curState = States.READY;
    	            
    	            // change turn
    	            turnId = (turnId + 1) % players.size();
    	        } else
    	            curPlayer.askSector("Invalid move. Where do you want to move?");
    	        break;
	    }
	}

    /** Can the player move to the chosen sector?
     * @param player
     * @param sector
     * @return
     */
    private boolean canMoveTo(GamePlayer player, Sector sector) {
        // TODO Auto-generated method stub
        return false;
    }

    /** Let's move!
     * @param player
     * @param sector
     */
    private void moveToSector(GamePlayer player, Sector sector) {
        // TODO Auto-generated method stub
        
    }

    /** Send a message to all connected players
     * @param message the message
     */
    public void signalAll(String message) {
        // TODO Auto-generated method stub
        
    }
}
