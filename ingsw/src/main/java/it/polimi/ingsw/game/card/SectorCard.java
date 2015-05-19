package it.polimi.ingsw.game.card;

import java.util.Random;

import it.polimi.ingsw.game.GamePlayer;
import it.polimi.ingsw.game.GameLogic;

public class SectorCard implements Card {
    public static final int NOISE_YOUR_SECTOR = 0;
    public static final int NOISE_ANY_SECTOR = 1;
    public static final int SILENCE = 2;
    
    private final int mType;
    
	@Override
	public void draw() {
	}

	/** Random card
	 * 
	 */
	public SectorCard() {
	    Random r = new Random();
	    mType = r.nextInt(3);
	}
	
	/** Do something
	 * 
	 * @param logic GameLogic
	 * @param player GamePlayer
	 * @return
	 */
	public boolean doAction(GameLogic logic, GamePlayer player) {
	    switch(mType) {
	        case NOISE_YOUR_SECTOR:
	            logic.signalAll("Noise in sector " + player.getSector());
	            return true;
	        case NOISE_ANY_SECTOR:
	            if(player.getLastChosenSector() != null) {
	                logic.signalAll("Noise in sector " + player.getLastChosenSector());
	                player.resetLastChosenSector();
	                return true;
	            } else {
	                player.askSector("Choose a fake sector");
	                return false;
	            }
	        case SILENCE:
	        default:
	            return true;
	    }
	        
	}
}
