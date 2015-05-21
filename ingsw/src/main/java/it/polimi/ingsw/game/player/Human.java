package it.polimi.ingsw.game.player;

public class Human implements Role {
    
    public static final int MAX_MOVES = 1;
    
    @Override
    public void move() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.player.Role#getMaxMoves()
     */
    @Override
    public int getMaxMoves() {
       return MAX_MOVES;
    }

}
