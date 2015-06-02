/**
 * 
 */
package it.polimi.ingsw.game.card.dangerous;

import java.util.Random;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;

/** Class used to get a random Dangerous Card.
 * @author  Michele
 * @since  2 Jun 2015
 */
public class DangerousCardBuilder {
    public static final int DANGEROUS_CARD_TYPES = 3;
    
    public static final int NOISE_IN_YOUR_SECTOR = 0;
    public static final int NOISE_IN_ANY_SECTOR = 1;
    public static final int SILENCE = 2;
    
    private DangerousCardBuilder() { }
    
    /** 
     * Get a random Dangerous Sector Card and give it to the player.
     * @param gameState Current GameState
     * @param gamePlayer The Player who is getting the card
     * @return A dangerous sector card
     */
    public static DangerousCard getRandomCard( GameState gameState, GamePlayer gamePlayer ) {
        Random generator = new Random();
        
        switch( generator.nextInt(DANGEROUS_CARD_TYPES) ) {
        case NOISE_IN_YOUR_SECTOR:        return new NoiseInYourSectorCard(gameState, gamePlayer);
        case NOISE_IN_ANY_SECTOR:         return new NoiseInAnySectorCard(gameState, gamePlayer);
        case SILENCE:                     return new SilenceCard(gameState, gamePlayer);
        default:                          throw new InvalidCardException("Unknown card");
        }
    }
}