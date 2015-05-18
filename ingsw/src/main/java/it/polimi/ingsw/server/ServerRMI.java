package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.game.network.ServerRMIMask;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
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
public class ServerRMI implements Listener, ServerRMIMask {
    private static final Logger LOG = Logger.getLogger(ServerRMI.class.getName());
    private static final String RMISERVER_STRING = "eftaiosRMI";
    private HashMap<String, ClientConnRMI> mMap;
    private Random mRandom = new Random();
    private Registry mRegistry;
    private boolean mIsUp = false;
    ServerRMIMask mStub;
    
    public ServerRMI() {
        mMap = new HashMap<String, ClientConnRMI>();
    }
    
    @Override
    public void run() {
        try {
            try {
                mRegistry = LocateRegistry.getRegistry();
                mRegistry.list(); // This will throw an exception if the mRegistry does not exists
            } catch(Exception e) {
                mRegistry = LocateRegistry.createRegistry(1099);
            }
            
            mStub = (ServerRMIMask) UnicastRemoteObject.exportObject(this, 0);
            mRegistry.bind(RMISERVER_STRING, mStub);

            LOG.log(Level.INFO, "RMI server is running");
            mIsUp = true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "RMI server is down: " + e.toString());
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
            LOG.log(Level.INFO, "Received an unknown message");
        }
    }

    @Override
    public NetworkPacket[] readCommands(String clientId) throws RemoteException {
        if(clientId == null)
            return new NetworkPacket[0];
        
        if(mMap.containsKey(clientId)) {
            ClientConnRMI conn = mMap.get(clientId);
            return conn.readCommands();
        } else {
            LOG.log(Level.INFO, "Received an unknown message");
            return new NetworkPacket[0];
        }
    }

    public void unregister(String id) {
        mMap.remove(id);
    }

    @Override
    public synchronized void tearDown() {
        try {
            if(mRegistry != null) {
                UnicastRemoteObject.unexportObject(this, true);
                mRegistry.unbind(RMISERVER_STRING);
                mRegistry = null;
            }
            mIsUp = false;
        } catch (AccessException e) {
            LOG.log(Level.FINEST, e.toString());
        } catch (RemoteException e) {
            LOG.log(Level.FINEST, e.toString());
        } catch (NotBoundException e) {
            LOG.log(Level.FINEST, e.toString());
        }
    }

    @Override
    public synchronized boolean isDown() {
        if(mRegistry != null)
            return false;
        return true;
    }

    @Override
    public boolean isUp() {
        return mIsUp;
    }
    
}
