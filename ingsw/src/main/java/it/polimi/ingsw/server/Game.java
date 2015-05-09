package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int mMaxPlayers = 8;
    private List<Client> mClients = new ArrayList<Client>(); 

	public synchronized boolean addPlayer(Client client) {
        if(mClients.size() >= mMaxPlayers)
            return false;

        mClients.add(client);
		return true;
	}

	public synchronized boolean isFull() {
        return mClients.size() >= mMaxPlayers;
	}

    public synchronized boolean canSetName(String name) {
        for(Client c : mClients) {
            if(name.equalsIgnoreCase(c.getUsername()))
                return false;
        } 
        return true;
    }
}
