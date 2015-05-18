package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

/** RMI Connection
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 16, 2015
 */
public class ClientConnRMI extends ClientConn {
    /** Outgoing packets */
    private Queue<NetworkPacket> mOutgoingQueue;
    
    /** Unique client identifier */
    private final String mUniqueId;
    
    /** The listening server */
    private final ServerRMI mServer;
    
    /** True if the client is disconnecting.
     * A disconnecting client can't send other packets
     */
    private boolean mIsDisconnecting = false;
    
    public ClientConnRMI(ServerRMI server, String uniqueId) {
        super();
        mIsConnected = true;
        mOutgoingQueue = new LinkedList<NetworkPacket>();
        mUniqueId = uniqueId;
        mServer = server;
    }
    
    /** Empty method, RMI does not need a separate thread */
    @Override
    public void run() {
    }

    /** Send a packet through this socket
     * 
     * @param pkt The packet
     */
    @Override
    public void sendPacket(NetworkPacket pkt) {
        synchronized(mOutgoingQueue) {
            mOutgoingQueue.add(pkt);
        }
    }

    /** Close this connection */
    @Override
    public void disconnect() {
        sendPacket(GameCommands.CMD_BYE);
        mIsDisconnecting = true;
        mIsConnected = false;
    }

    public void onRMICommand(NetworkPacket pkt) throws RemoteException {
        resetTimeoutTimer();
        if(mClient != null && pkt != null && !mIsDisconnecting)
            mClient.handlePacket(pkt);
    }

    public NetworkPacket[] readCommands() {
        resetTimeoutTimer();
        
        // Last read with good bye message
        if(mIsDisconnecting)
            mServer.unregister(mUniqueId);
        
        synchronized(mOutgoingQueue) {
            if(mOutgoingQueue.isEmpty())
                return new NetworkPacket[0];
            NetworkPacket[] out = new NetworkPacket[mOutgoingQueue.size()];
            
            int i = 0;
            while(!mOutgoingQueue.isEmpty()) {
                NetworkPacket msg = mOutgoingQueue.poll();
                out[i++] = msg;
            }
            return out;
        }
    }
}
