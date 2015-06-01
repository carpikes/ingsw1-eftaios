/**
 * 
 */
package it.polimi.ingsw.game.card.object;

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
	
	private static final int ADRENALINE_CARD = 0;
	private static final int ATTACK_CARD = 1;
	private static final int DEFENSE_CARD = 2;
	private static final int SEDATIVES_CARD = 3;
	private static final int SPOTLIGHT_CARD = 4;
	private static final int TELEPORT_CARD = 5;
	
	public abstract PlayerState doAction(GameState gameState);
	
	/* non è un granché, ma senza enum ... */
	public static ObjectCard getRandomCard() {
		Random generator = new Random();
		
		switch( generator.nextInt(OBJECT_CARD_TYPES) ) {
		case ADRENALINE_CARD:             return new AdrenalineCard();
		case ATTACK_CARD:                 return new AttackCard();
		case DEFENSE_CARD:                return new DefenseCard();
		case SEDATIVES_CARD:              return new SedativesCard();
		case SPOTLIGHT_CARD:              return new SpotlightCard();
		case TELEPORT_CARD:               return new TeleportCard();
		default:                          throw new InvalidCardException("Unknown card");
		}
	}
}
