package it.polimi.ingsw.game.network;

public class EnemyInfo {
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
