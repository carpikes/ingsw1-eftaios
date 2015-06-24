package it.polimi.ingsw.common;

import java.io.Serializable;

/** Info about the current player
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class PlayerInfo implements Serializable{
    /** Serial version */
    private static final long serialVersionUID = 1L;

    /** Username */
    private String mUsername;

    /** Number of cards he's holding */
    private int mNumberOfCards;

    /** Constructor
     *
     * @param username Username
     */
    public PlayerInfo(String username) {
        mUsername = username;
        mNumberOfCards = 0;
    }

    /** Number of cards this player have
     *
     * @return number of cards
     */
    public int getNumberOfCards() {
        return mNumberOfCards;
    }

    /** Change number of cards
     *
     * @param numberOfCards New number of cards
     */
    public void setNumberOfCards(int numberOfCards) {
        mNumberOfCards = numberOfCards;
    }

    /** Get the username
     *
     * @return Player username
     */
    public String getUsername() {
        return mUsername;
    }
}
