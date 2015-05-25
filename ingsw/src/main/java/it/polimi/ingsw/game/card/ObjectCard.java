/**
 * 
 */
package it.polimi.ingsw.game.card;

import java.util.Random;

/**
 * @author Michele
 * @since 23 May 2015
 */
public enum ObjectCard {
    ATTACK(0),
    TELEPORT(1),
    ADRENALINE(2),
    SEDATIVES(3),
    SPOTLIGHT(4),
    DEFENSE(5);
    
    private int id;
    
    private ObjectCard(int id) {
        this.id = id;
    }
    
    public static ObjectCard getCardAt( int id ) {
        return ObjectCard.values()[id];
    }
    
    public static ObjectCard getRandomCard() {
        Random generator = new Random();
        return getCardAt( generator.nextInt(ObjectCard.values().length) );
    }
}
