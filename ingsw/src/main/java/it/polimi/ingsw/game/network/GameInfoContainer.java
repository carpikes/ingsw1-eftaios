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
	private final String[] mPlayers;
	
	/** First player */
	private final int mFirstTurn;
	
	/** Are you human or alien? */
	private final boolean mHuman;
	
	public GameInfoContainer(String[] players, int firstTurn, boolean isHuman) {
		mPlayers = players;
		mFirstTurn = firstTurn;
		mHuman = isHuman;
	}

	/**
	 * @return the mPlayers
	 */
	public String[] getPlayersList() {
		return mPlayers;
	}

	/**
	 * @return the mFirstTurn
	 */
	public int getFirstTurn() {
		return mFirstTurn;
	}

	/**
	 * @return the mHuman
	 */
	public boolean isHuman() {
		return mHuman;
	}
}
