/**
 * 
 */
package it.polimi.ingsw.game.card;

import it.polimi.ingsw.exception.MissingCardException;

/**
 * @author Michele
 * @since 23 May 2015
 */
public class ObjectCardBuilder {
    private ObjectCardBuilder() { }
    
    public static ObjectCard getCard( ObjectCardType type ) {
        switch( type ) {
        /*case ATTACK: return new Card("Attack", new AttackCommand(), true); 
        case TELEPORT: return new Card("Teleport", new MoveCommand(), true); 
        case SEDATIVES: return new Card("Sedatives", new DrawCardCommand(false), true);
        case SPOTLIGHT: return new Card("Spotlight", new SpotlightCommand(), true);
        case DEFENSE: return new Card("Defense", new setDefenseCommand(), false);
        case ADRENALINE: return new Card("Adrenaline", new setNumberOfMovesCommand(2), false);*/
        default: throw new MissingCardException("Unknown card.");
        }
    }
}
