/**
 * 
 */
package it.polimi.ingsw.game.card.object;

import it.polimi.ingsw.exception.InvalidCardException;
import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.player.GamePlayer;

import java.util.Random;

/**
 * @author Michele
 * @since 2 Jun 2015
 */
public class ObjectCardBuilder {
    public static final int OBJECT_CARD_TYPES = 6;
    
    private static final int ADRENALINE_CARD = 0;
    private static final int ATTACK_CARD = 1;
    private static final int DEFENSE_CARD = 2;
    private static final int SEDATIVES_CARD = 3;
    private static final int SPOTLIGHT_CARD = 4;
    private static final int TELEPORT_CARD = 5;
    
    public static ObjectCard getRandomCard(GameState game, GamePlayer player) {
        Random generator = new Random();
        
        switch( generator.nextInt(OBJECT_CARD_TYPES) ) {
        case ADRENALINE_CARD:             return new AdrenalineCard(game, player);
        case ATTACK_CARD:                 return new AttackCard(game, player);
        case DEFENSE_CARD:                return new DefenseCard(game, player);
        case SEDATIVES_CARD:              return new SedativesCard(game, player);
        case SPOTLIGHT_CARD:              return new SpotlightCard(game, player);
        case TELEPORT_CARD:               return new TeleportCard(game, player);
        default:                          throw new InvalidCardException("Unknown card");
        }
    }
}
