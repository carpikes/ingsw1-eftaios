/**
 * 
 */
package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;

import java.util.Random;

/**
 * Class for getting a random Object Card.
 * @author Michele
 * @since 2 Jun 2015
 */
public class ObjectCardBuilder {
    public static final int OBJECT_CARD_TYPES = 6;

    public static final int ADRENALINE_CARD = 0;
    public static final int ATTACK_CARD = 1;
    public static final int DEFENSE_CARD = 2;
    public static final int SEDATIVES_CARD = 3;
    public static final int SPOTLIGHT_CARD = 4;
    public static final int TELEPORT_CARD = 5;

    private ObjectCardBuilder() {

    }

    /**
     * Get a new random Object Card
     * @param game Current GameState
     * @param player The Player who wants to get an object card
     * @return The object card
     */

    private static Random generator = new Random();

    public static ObjectCard getRandomCard(GameState game) {
        return new DefenseCard(game, "Defense");
        //return idToObjectCard(generator.nextInt(OBJECT_CARD_TYPES), game);
    }

    public static ObjectCard idToObjectCard(int id, GameState game) {
        switch( id ) {
            case ADRENALINE_CARD:             return new AdrenalineCard(game, idToString(id));
            case ATTACK_CARD:                 return new AttackCard(game, idToString(id));
            case DEFENSE_CARD:                return new DefenseCard(game, idToString(id));
            case SEDATIVES_CARD:              return new SedativesCard(game, idToString(id));
            case SPOTLIGHT_CARD:              return new SpotlightCard(game, idToString(id));
            case TELEPORT_CARD:               return new TeleportCard(game, idToString(id));
            default:                           throw new InvalidCardException("Unknown card");
        }
    }

    public static String idToString(int id) {
        switch( id ) {
            case ADRENALINE_CARD:             return "Adrenaline";
            case ATTACK_CARD:                 return "Attack";
            case DEFENSE_CARD:                return "Defense";
            case SEDATIVES_CARD:              return "Sedatives";
            case SPOTLIGHT_CARD:              return "Spotlight";
            case TELEPORT_CARD:               return "Teleport";
            default:                          throw new InvalidCardException("Unknown card");
        }
    }
}
