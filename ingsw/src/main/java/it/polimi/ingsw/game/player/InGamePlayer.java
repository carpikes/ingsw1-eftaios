package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.server.Client;

import java.util.ArrayList;

/** Game manager
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 19, 2015
 */
public class InGamePlayer {
	/** Role of the player */
	private Role role; 
	
	/** Handle connection to server */
	private Client client; 
	
	/** Card objects owned by the player */
	ArrayList<ObjectCard> objectCards;
	
	/** Current state of player in game */
	InGameState state;
}
