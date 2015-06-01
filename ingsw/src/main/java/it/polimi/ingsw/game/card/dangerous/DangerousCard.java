/**
 * 
 */
package it.polimi.ingsw.game.card.dangerous;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

import java.io.Serializable;
import java.util.Random;


/**
 * @author Michele
 * @since 23 May 2015
 */
public abstract class DangerousCard implements Serializable {

	public static final int DANGEROUS_CARD_TYPES = 3;
	
	public static final int NOISE_IN_YOUR_SECTOR = 0;
	public static final int NOISE_IN_ANY_SECTOR = 1;
	public static final int SILENCE = 2;
	
	public abstract PlayerState doAction(GameState gameState);
	
	public static DangerousCard getRandomCard() {
		Random generator = new Random();
		
		switch( generator.nextInt(DANGEROUS_CARD_TYPES) ) {
		case NOISE_IN_YOUR_SECTOR:        return new NoiseInYourSectorCard();
		case NOISE_IN_ANY_SECTOR:         return new NoiseInAnySectorCard();
		case SILENCE:                     return new SilenceCard();
		default:                          throw new InvalidCardException("Unknown card");
		}
	}
}
