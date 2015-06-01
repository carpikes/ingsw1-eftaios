/**
 * 
 */
package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

import java.io.Serializable;
import java.util.Random;


/**
 * @author Michele
 * @since 23 May 2015
 */
public abstract class HatchCard implements Serializable {

	public static final int HATCH_CARD_TYPES = 2;
	
	public static final int GREEN_HATCH_CARD = 0;
	public static final int RED_HATCH_CARD = 1;
	
	public abstract PlayerState getNextState(GameState gameState);
	
	public static HatchCard getRandomCard() {
		Random generator = new Random();
		
		switch( generator.nextInt(HATCH_CARD_TYPES) ) {
		case GREEN_HATCH_CARD:        return new GreenHatchCard();
		case RED_HATCH_CARD:          return new RedHatchCard();
		default:          throw new InvalidCardException("Unknown card");
		}
	}
}
