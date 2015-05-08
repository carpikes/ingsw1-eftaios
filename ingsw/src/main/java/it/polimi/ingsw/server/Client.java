package it.polimi.ingsw.server;

public class Client {
	private static enum States {
		WAITING_FOR_USERNAME,
	};
	private final ClientConn mConn;
	private final Game mGame;
	private String mUser = null;
	private States mState;
	
	public Client(ClientConn conn, Game game) {
		mConn = conn;
		mGame = game;
		mState = States.WAITING_FOR_USERNAME;
		
		/* Enable bidirectional communication Client <-> ClientConn */
		mConn.setClient(this);  
	}
	
	public void handleMessage(String msg) {
		switch(mState) {
			case WAITING_FOR_USERNAME:
				// msg = username
				
				break;
		}
	}
	
	public void handleDisconnect() {
		// TODO
	}
}
