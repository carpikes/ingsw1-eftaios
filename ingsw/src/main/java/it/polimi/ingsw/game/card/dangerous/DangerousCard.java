/**
 * 
 */
package it.polimi.ingsw.game.card.dangerous;

/**
 * @author Michele
 * @since 25 May 2015
 */
public enum DangerousCard {
    NOISE_IN_YOUR_SECTOR(0),
    NOISE_IN_ANY_SECTOR(1),
    SILENCE(2);
    
    private int id;
    
    private DangerousCard(int id) {
        this.id = id;
    }
    
    public static DangerousCard getCardAt( int id ) {
        return DangerousCard.values()[id];
    }
}
