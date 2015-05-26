package it.polimi.ingsw.game.player;

public interface Role {
    // icon?
 
    public void move();

    /** Get max moves for this role. Please note that it is NOT the max number 
     * of moves the player can do in any moment, but just in the beginning.
     * @return
     */
    public int getMaxMoves();
}
