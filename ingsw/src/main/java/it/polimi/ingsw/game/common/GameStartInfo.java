package it.polimi.ingsw.game.common;

import it.polimi.ingsw.game.GameMap;

import java.io.Serializable;

/**
 * @author Alain
 * @since 22/mag/2015
 *
 */
public class GameStartInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** List of player usernames (and id!) */
	private final PlayerInfo[] mUsers;
	
	/** Are you human or alien? */
	private final boolean mHuman;
	private final GameMap mMap;
	private final int id;
	
	public GameStartInfo(PlayerInfo[] info, int idUsername, boolean isHuman, GameMap map) {
		mUsers = info;
		mHuman = isHuman;
		mMap = map;
		id = idUsername;
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
}
