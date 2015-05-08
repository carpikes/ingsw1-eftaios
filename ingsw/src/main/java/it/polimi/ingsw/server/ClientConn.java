package it.polimi.ingsw.server;

public abstract class ClientConn implements Runnable{
	protected Client mClient = null;
	
	public void setClient(Client client) {
		mClient = client;
	}
	
	public abstract String getUsername();
}
