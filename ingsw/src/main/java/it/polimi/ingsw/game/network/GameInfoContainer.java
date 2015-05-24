package it.polimi.ingsw.game.network;

import it.polimi.ingsw.game.GameMap;

import java.io.Serializable;

/**
 * @author Alain
 * @since 22/mag/2015
 *
 */
public class GameInfoContainer implements Serializable {

	private static final long serialVersionUID = 1L;

	/** List of player usernames (and id!) */
	private final String[] mUsers;
	
	/** Are you human or alien? */
	private final boolean mHuman;
	private final GameMap mMap;
	
	public GameInfoContainer(String[] usernames, boolean isHuman, GameMap map) {
		mUsers = usernames;
		mHuman = isHuman;
		mMap = map;
	}

	/**
	 * @return the mUsers
	 */
	public String[] getPlayersList() {
		return mUsers;
	}

	/**
     * @return the mMap
     */
    public GameMap getMap() {
        return mMap;
    }

    /**
	 * @return the mHuman
	 */
	public boolean isHuman() {
		return mHuman;
	}
}
