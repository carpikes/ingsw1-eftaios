/**
 * 
 */
package it.polimi.ingsw.game.card;

/**
 * @author Michele
 * @since 25 May 2015
 */
public enum HatchCard {
    RED_HATCH(0),
    GREEN_HATCH(1);
    
    private int id;
    
    private HatchCard(int id) {
        this.id = id;
    }
    
    public static HatchCard getCardAt( int id ) {
        return HatchCard.values()[id];
    }
}
