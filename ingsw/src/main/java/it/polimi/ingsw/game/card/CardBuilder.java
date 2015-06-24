package it.polimi.ingsw.game.card;

import it.polimi.ingsw.game.GameState;

/** Interface for all card builders
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 23 Jun 2015
 */
public interface CardBuilder {
    
    /** Get a random card among the available ones
     * @param game The game state
     * @return A card
     */
    public Card getRandomCard(GameState game);
    
    /** Get a card specified by an id
     * @param gameState The game state
     * @param id The card ID you want
     * @return The card
     */
    public Card getCard( GameState gameState, int id );
}
