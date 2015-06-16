package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.Rand;

/** Class for getting a random hatch card.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class HatchCardBuilder {
    /** Number of hatch card types */
    public static final int HATCH_CARD_TYPES = 2;

    /** Green hatch card */
    public static final int GREEN_HATCH_CARD = 0;
    
    /** Red hatch card */
    public static final int RED_HATCH_CARD = 1;

    /** Private Constructor */
    private HatchCardBuilder() {
        /** Empty constructor */
    }

    /** Give a random hatch card to set player
     * 
     * @param state GameState of the player
     * @return An Hatch Card
     */
    public static HatchCard getRandomCard( GameState state ) {
        switch( Rand.nextInt(HATCH_CARD_TYPES) ) {
            case GREEN_HATCH_CARD:        return new GreenHatchCard(state);
            case RED_HATCH_CARD:          return new RedHatchCard(state);
            default:                      throw new InvalidCardException("Unknown card");
        }
    }
}
