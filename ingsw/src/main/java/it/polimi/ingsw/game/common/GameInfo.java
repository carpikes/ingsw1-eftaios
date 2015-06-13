package it.polimi.ingsw.game.common;

import it.polimi.ingsw.game.GameMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alain
 * @since 22/mag/2015
 *
 */
public class GameInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** List of player usernames (and id!) */
    private final PlayerInfo[] mUsers;

    /** Are you human or alien? */
    private final boolean mHuman;
    private final GameMap mMap;
    private final int id;
    private final ArrayList<Integer> listOfCards;

    public GameInfo(PlayerInfo[] info, int idUsername, boolean isHuman, GameMap map) {
        mUsers = info;
        mHuman = isHuman;
        mMap = map;
        id = idUsername;
        listOfCards = new ArrayList<>();
    }

    /**
     * @return the Users
     */
    public PlayerInfo[] getPlayersList() {
        return mUsers;
    }

    /**
     * @return the Map
     */
    public GameMap getMap() {
        return mMap;
    }

    /**
     * @return True if is human
     */
    public boolean isHuman() {
        return mHuman;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public List<Integer> getListOfCards() {
        return listOfCards;
    }
}
