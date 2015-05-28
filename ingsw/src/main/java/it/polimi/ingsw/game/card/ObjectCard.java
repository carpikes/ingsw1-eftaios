/**
 * 
 */
package it.polimi.ingsw.game.card;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.state.PlayerState;

import java.io.Serializable;
import java.util.Random;


/**
 * @author Michele
 * @since 23 May 2015
 */
public abstract class ObjectCard implements Serializable {

	public static final int OBJECT_CARD_TYPES = 6;
	
	public abstract PlayerState doAction(GameState gameState);
	
	/* non è un granché, ma senza enum ... */
	public static ObjectCard getRandomCard() {
		Random generator = new Random();
		
		switch( generator.nextInt(OBJECT_CARD_TYPES) ) {
		case 0:			return new AdrenalineCard();
		case 1:			return new AttackCard();
		case 2:			return new DefenseCard();
		case 3:			return new SedativesCard();
		case 4:			return new SpotlightCard();
		case 5:			return new TeleportCard();
		default:		throw new InvalidCardException("Unknown card");
		}
	}
}
