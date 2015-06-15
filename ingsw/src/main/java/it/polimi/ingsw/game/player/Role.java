package it.polimi.ingsw.game.player;

/** Human or alien?
 *
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 3 Jun 2015
 */
public interface Role {
    /** Get max moves for this role. 
     * Please note that it is NOT the max number 
     * of moves the player can do in any moment, 
     * but just in the beginning.
     *
     * @return Max number of moves
     */
    public int getMaxMoves();
}
