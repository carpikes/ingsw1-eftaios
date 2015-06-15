package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.common.Rand;

/** Class for getting a random Object Card.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 2 Jun 2015
 */
public class ObjectCardBuilder {
    /** Number of object cards */
    public static final int OBJECT_CARD_TYPES = 6;

    /** Adrenaline card */
    public static final int ADRENALINE_CARD = 0;

    /** Attack Card */
    public static final int ATTACK_CARD = 1;
    
    /** Defense Card */
    public static final int DEFENSE_CARD = 2;

    /** Sedatives Card */
    public static final int SEDATIVES_CARD = 3;

    /** Spotlight Card */
    public static final int SPOTLIGHT_CARD = 4;
    
    /** Teleport Card */
    public static final int TELEPORT_CARD = 5;

    /** Private Constructor */
    private ObjectCardBuilder() {
        // Nothing here
    }

    /** Get a new random Object Card
     *
     * @param game Current GameState
     * @param player The Player who wants to get an object card
     * @return The object card
     */
    public static ObjectCard getRandomCard(GameState game) {
        return idToObjectCard(Rand.nextInt(OBJECT_CARD_TYPES), game);
    }

    /** Get an object card using the id
     *
     * @param id The given id
     * @param game Game State
     * @return The object card
     */
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

    /** Get the object card name
     *
     * @param id The id
     * @return Object card name
     */
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
