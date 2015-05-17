package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.network.ServerRMIMask;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 16, 2015
 */

// TODO: remove client unique id from mMap on disconnect
// TODO: something to check if a client is still alive, indipendent from connectiong (e.g. ping)
public class ServerRMI implements Runnable, ServerRMIMask {
    private static final Logger mLog = Logger.getLogger(ServerRMI.class.getName());
    private static final String RMISERVER_STRING = "eftaiosRMI";
    private HashMap<String, ClientConnRMI> mMap;
    private Random mRandom = new Random();
    
    public ServerRMI() {
        mMap = new HashMap<String, ClientConnRMI>();
    }
    
    @Override
    public void run() {
        try {
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry();
                registry.list(); // This will throw an exception if the registry does not exists
            } catch(Exception e) {
                registry = LocateRegistry.createRegistry(1099);
            }
            
            ServerRMIMask stub = (ServerRMIMask) UnicastRemoteObject.exportObject(this, 0);
            registry.bind(RMISERVER_STRING, stub);

            mLog.log(Level.INFO, "RMI server is running");
            
        } catch (Exception e) {
            mLog.log(Level.SEVERE, "RMI server is down: " + e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    public String registerAndGetId() throws RemoteException {
        StringBuilder id = new StringBuilder();
        for(int i=0;i<32;i++)
            id.append((char)('0' + mRandom.nextInt(10)));
        
        String ids = id.toString();
        ClientConnRMI conn = new ClientConnRMI(this, ids);
        if(Server.getInstance().addClient(conn))
            mMap.put(ids, conn);
        else
            conn.disconnect();
        return ids;
    }

    @Override
    public void onRMICommand(String clientId, NetworkPacket pkt) throws RemoteException {
        if(clientId == null || pkt == null)
            return;
        
        if(mMap.containsKey(clientId)) {
            ClientConnRMI conn = mMap.get(clientId);
            conn.onRMICommand(pkt);
        } else {
            mLog.log(Level.INFO, "Received an unknown message");
        }
    }

    @Override
    public NetworkPacket[] readCommands(String clientId) throws RemoteException {
        if(clientId == null)
            return null;
        
        if(mMap.containsKey(clientId)) {
            ClientConnRMI conn = mMap.get(clientId);
            return conn.readCommands();
        } else {
            mLog.log(Level.INFO, "Received an unknown message");
            return null;
        }
    }

    public void unregister(String id) {
        mMap.remove(id);
    } 
}
