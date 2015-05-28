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

/** RMI Server Listener
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 16, 2015
 */
public class ServerRMI implements Listener, ServerRMIMask {
    private static final Logger LOG = Logger.getLogger(ServerRMI.class.getName());
    
    /** RMI service identifier */
    private static final String RMISERVER_STRING = "eftaiosRMI";
    
    /** HashMap with connected clients */
    private HashMap<String, ClientConnRMI> mMap;
    
    /** Random generator */
    private Random mRandom = new Random();
    
    /** RMI Registry instance */
    private Registry mRegistry;
    
    /** True if the RMI service is running */
    private boolean mIsUp = false;
    
    public ServerRMI() {
        mMap = new HashMap<String, ClientConnRMI>();
    }
    
    /** Run the RMI listener */
    @Override
    public void run() {
        try {
            try {
                mRegistry = LocateRegistry.getRegistry();
                mRegistry.list(); // This will throw an exception if the mRegistry does not exists
            } catch(Exception e) {
                LOG.log(Level.FINEST, e.toString(), e);
                mRegistry = LocateRegistry.createRegistry(1099);
            }
            
            ServerRMIMask stub = (ServerRMIMask) UnicastRemoteObject.exportObject(this, 0);
            mRegistry.bind(RMISERVER_STRING, stub);

            LOG.log(Level.INFO, "RMI server is running");
            mIsUp = true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "RMI server is down: " + e.toString(), e);
        }
    }
    
    /** [REMOTE] 
     * This method will get back a new unique id
     * It must be called before any other one.
     * 
     * @return A new unique id
     */
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

    /** [REMOTE]
     * Call this method to send a packet to the server
     * 
     * @param clientId  Your unique ID obtained by calling registerAndGetId()
     * @param pkt       Packet you want to send 
     */
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

    /** [REMOTE]
     * Due to RMI fault, call this method to read your pending messages
     * 
     * @param clientId  Your unique ID obtained by calling registerAndGetId()
     * @return          A NetworkPacket array
     */
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

    /** Unregister a clientId
     * 
     * @param clientId  Your unique ID obtained by calling registerAndGetId()
     */
    public void unregister(String clientId) {
        mMap.remove(clientId);
    }

    /** Shut down this listener */
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
            LOG.log(Level.FINEST, e.toString(), e);
        } catch (RemoteException e) {
            LOG.log(Level.FINEST, e.toString(), e);
        } catch (NotBoundException e) {
            LOG.log(Level.FINEST, e.toString(), e);
        }
    }

    /** Check if this listener is correctly shutted down
     * 
     * @return True if this listener is down
     */
    @Override
    public synchronized boolean isDown() {
        if(mRegistry != null)
            return false;
        return true;
    }

    /** Check if this listener is running
     * 
     * @return True if this listener is correctly listening
     */
    @Override
    public boolean isUp() {
        return mIsUp;
    }
    
}