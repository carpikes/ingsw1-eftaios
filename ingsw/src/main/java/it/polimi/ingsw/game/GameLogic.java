package it.polimi.ingsw.game;

import it.polimi.ingsw.game.card.CharacterCard;
import it.polimi.ingsw.game.card.HatchCard;
import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.game.card.SectorCard;

import java.util.Deque;
import java.util.LinkedList;

/** Game
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 19, 2015
 */
public class GameLogic {
	private Deque<CharacterCard> characterCardDeck;
	private Deque<HatchCard> hatchCardDeck;
	private Deque<SectorCard> sectorCardDeck;
	private Deque<ObjectCard> objectCardDeck;
	
	private GameMap map;
	
    /** Set up game
     * 
     */
	public GameLogic() {
		/* create decks */
		characterCardDeck = new LinkedList<>();
		hatchCardDeck = new LinkedList<>();
		sectorCardDeck = new LinkedList<>();
		objectCardDeck = new LinkedList<>();
		
		fillDecks();
	}
	
	public void fillDecks() {
		// 
	}
	
	public void gameUpdate() {
		
	}
}
