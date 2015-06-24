package it.polimi.ingsw.game.card.hatch;

import it.polimi.ingsw.common.Rand;
import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.CardBuilder;

/** Class for getting a random hatch card.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class HatchCardBuilder implements CardBuilder {
    /** Number of hatch card types */
    public static final int HATCH_CARD_TYPES = 2;

    /** Green hatch card */
    public static final int GREEN_HATCH_CARD = 0;

    /** Red hatch card */
    public static final int RED_HATCH_CARD = 1;

    /** Give a random hatch card to set player
     * 
     * @param state GameState of the player
     * @return An Hatch Card
     */
    @Override
    public HatchCard getRandomCard( GameState state ) {
        return getCard( state, Rand.nextInt(HATCH_CARD_TYPES) );
    }

    /** Get a specified hatch card
     * @param game The game state
     * @param id The id of the card
     * @return An hatch card
     */
    @Override
    public HatchCard getCard(GameState game, int id) {
        switch( Rand.nextInt(HATCH_CARD_TYPES) ) {
        case GREEN_HATCH_CARD:        return new GreenHatchCard(game);
        case RED_HATCH_CARD:          return new RedHatchCard(game);
        default:                      throw new InvalidCardException("Unknown card");
        }
    }
}
