package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameCommand;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

/** RMI Connection Handler
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 16, 2015
 */
public class ClientConnRMI extends ClientConn {
    /** Outgoing packets */
    private Queue<GameCommand> mOutgoingQueue;
    
    /** Unique client identifier */
    private final String mUniqueId;
    
    /** The listening server */
    private final ServerRMI mServer;
    
    /** True if the client is disconnecting.
     * A disconnecting client can't send other packets
     */
    private boolean mIsDisconnecting = false;
    
    /** The constructor
     * 
     * @param server    RMI Server listener
     * @param uniqueId  A unique Identifier
     */
    public ClientConnRMI(ServerRMI server, String uniqueId) {
        super();
        mIsConnected = true;
        mOutgoingQueue = new LinkedList<GameCommand>();
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
    public void sendPacket(GameCommand pkt) {
        synchronized(mOutgoingQueue) {
            mOutgoingQueue.add(pkt);
        }
    }

    /** Close this connection */
    @Override
    public void disconnect() {
        sendPacket(GameOpcode.CMD_BYE);
        mIsDisconnecting = true;
        mIsConnected = false;
    }

    /** This method is called on each received command
     * 
     * @param pkt The packet
     * @throws RemoteException
     */
    public void onRMICommand(GameCommand pkt) throws RemoteException {
        resetTimeoutTimer();
        if(mClient != null && pkt != null && !mIsDisconnecting)
            mClient.handlePacket(pkt);
    }

    /** This method is called from a client 
     * 
     * @return A list of packets
     */
    public GameCommand[] readCommands() {
        resetTimeoutTimer();
        
        // Last read with good bye message
        if(mIsDisconnecting)
            mServer.unregister(mUniqueId);
        
        synchronized(mOutgoingQueue) {
            if(mOutgoingQueue.isEmpty())
                return new GameCommand[0];
            GameCommand[] out = new GameCommand[mOutgoingQueue.size()];
            
            int i = 0;
            while(!mOutgoingQueue.isEmpty()) {
                GameCommand msg = mOutgoingQueue.poll();
                out[i++] = msg;
            }
            return out;
        }
    }
}
