package it.polimi.ingsw.game.network;

import java.io.Serializable;

public class EnemyInfo implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String mUsername;
    private int mNumberOfCards;
    
    public EnemyInfo(String username) {
        mUsername = username;
        mNumberOfCards = 0;
    }
    
    public int getNumberOfCards() {
        return mNumberOfCards;
    }
    public void setNumberOfCards(int numberOfCards) {
        mNumberOfCards = numberOfCards;
    }
    public String getUsername() {
        return mUsername;
    }
}
