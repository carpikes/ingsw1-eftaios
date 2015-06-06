/**
 * 
 */
package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;

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
    
    /**
     * Get a new random Object Card
     * @param game Current GameState
     * @param player The Player who wants to get an object card
     * @return The object card
     */
    public static ObjectCard getRandomCard(GameState game, GamePlayer player) {
        Random generator = new Random();
        
        return idToObjectCard(generator.nextInt(OBJECT_CARD_TYPES), game, player);
    }
    
    public static ObjectCard idToObjectCard(int id, GameState game, GamePlayer player) {
        switch( id ) {
            case ADRENALINE_CARD:             return new AdrenalineCard(game, player);
            case ATTACK_CARD:                 return new AttackCard(game, player);
            case DEFENSE_CARD:                return new DefenseCard(game, player);
            case SEDATIVES_CARD:              return new SedativesCard(game, player);
            case SPOTLIGHT_CARD:              return new SpotlightCard(game, player);
            case TELEPORT_CARD:               return new TeleportCard(game, player);
            default:                           throw new InvalidCardException("Unknown card");
        }
    }
}
