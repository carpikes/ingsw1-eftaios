package it.polimi.ingsw.game.network;

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
	
	public GameInfoContainer(String[] usernames, boolean isHuman) {
		mUsers = usernames;
		mHuman = isHuman;
	}

	/**
	 * @return the mUsers
	 */
	public String[] getPlayersList() {
		return mUsers;
	}

	/**
	 * @return the mHuman
	 */
	public boolean isHuman() {
		return mHuman;
	}
}
