package it.polimi.ingsw.game;


/**
 *
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 19, 2015
 */
public class GamePlayer {
    
    private PlayerState currentState;
    
    public GamePlayer( ) {
        currentState = PlayerState.NOT_PLAYING;
    }
    
    private enum PlayerState {
        NOT_PLAYING,
        AWAY,
        MOVING,
        USING_DANGEROUS_CARD,
        ATTACKING,
        USING_OBJECT_CARD,
        USING_HATCH_CARD,
        WINNER,
        LOSER
    }
}
