/**
 * 
 */
package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;

import java.util.Random;

/**
 * @author Michele
 * @since 2 Jun 2015
 */
public class HatchCardBuilder {
    
    public static final int HATCH_CARD_TYPES = 2;
    
    public static final int GREEN_HATCH_CARD = 0;
    public static final int RED_HATCH_CARD = 1;
    
    public static HatchCard getRandomCard( GameState state, GamePlayer player ) {
        Random generator = new Random();
        
        switch( generator.nextInt(HATCH_CARD_TYPES) ) {
        case GREEN_HATCH_CARD:        return new GreenHatchCard(state, player);
        case RED_HATCH_CARD:          return new RedHatchCard(state, player);
        default:          throw new InvalidCardException("Unknown card");
        }
    }
}
