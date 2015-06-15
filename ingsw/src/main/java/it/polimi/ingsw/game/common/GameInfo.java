package it.polimi.ingsw.game.common;

import it.polimi.ingsw.game.GameMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Initial game info sent from server to clients
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 22/mag/2015
 */
public class GameInfo implements Serializable {
    /** Serial version */
    private static final long serialVersionUID = 1L;

    /** List of player usernames (and id!) */
    private final PlayerInfo[] mPlayers;

    /** Are you human or alien? */
    private final boolean mHuman;

    /** Game map */
    private final GameMap mMap;

    /** Id */
    private final int id;

    /** List of cards */
    private final List<Integer> listOfCards;

    /** Constructor
     *
     * @param info Game info
     * @param idUsername Player id
     * @param isHuman True if the player is human
     * @param map Game map
     */
    public GameInfo(PlayerInfo[] info, int idUsername, boolean isHuman, GameMap map) {
        mPlayers = info;
        mHuman = isHuman;
        mMap = map;
        id = idUsername;
        listOfCards = new ArrayList<>();
    }

    /** Get the player list
     * @return Player list
     */
    public PlayerInfo[] getPlayersList() {
        return mPlayers;
    }

    /** Get the map
     * @return the Map
     */
    public GameMap getMap() {
        return mMap;
    }

    /** Is the player human?
     * @return True if is human
     */
    public boolean isHuman() {
        return mHuman;
    }

    /** Get the player id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /** Get the list of cards
     *
     * @return List of cards
     */
    public List<Integer> getListOfCards() {
        return listOfCards;
    }
}
