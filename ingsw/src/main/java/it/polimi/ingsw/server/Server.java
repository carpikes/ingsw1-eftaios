package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private static class Holder {
        private static final Server INSTANCE = new Server();
    }
    
    public static Server getInstance() {
        return Holder.INSTANCE;
    }
    
    /* TODO: mTCPPort must not be hardcoded here
     * Why port 3834? 3834 = 0xEFA -> Escape From Aliens
     * Note: ports < 1024 are privileges 
     */
    private static final int mTCPPort = 3834;
    private List<Runnable> mServerList;
    private List<Client> mClientList;

    private Server() { 
    	mServerList = new ArrayList<Runnable>();
    	mClientList = new ArrayList<Client>();
    	
        ServerTCP tcp = new ServerTCP(mTCPPort);
        mServerList.add(tcp);
    }
    
    public void addClient(Client client) {
    	mClientList.add(client);
    }

    public void runServer() {
    	for(Runnable server : mServerList)
    		new Thread(server).start();
    	
		try {
			for(;;)
				Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
