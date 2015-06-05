package it.polimi.ingsw.game.network;

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
	private final EnemyInfo[] mUsers;
	
	/** Are you human or alien? */
	private final boolean mHuman;
	private final GameMap mMap;
	
	public GameStartInfo(EnemyInfo[] usernames, boolean isHuman, GameMap map) {
		mUsers = usernames;
		mHuman = isHuman;
		mMap = map;
	}

	/**
	 * @return the mUsers
	 */
	public EnemyInfo[] getPlayersList() {
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
