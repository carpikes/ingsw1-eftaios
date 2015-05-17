package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 16, 2015
 */

public class ClientConnRMI extends ClientConn {
    private Queue<NetworkPacket> mOutgoingQueue;
    private final String mUniqueId;
    private final ServerRMI mServer;
    private boolean mIsDisconnecting = false;
    
    public ClientConnRMI(ServerRMI server, String uniqueId) {
        super();
        mOutgoingQueue = new LinkedList<NetworkPacket>();
        mUniqueId = uniqueId;
        mServer = server;
    }
    
    @Override
    public void run() {
    }

    @Override
    public void sendPacket(NetworkPacket pkt) {
        synchronized(mOutgoingQueue) {
            mOutgoingQueue.add(pkt);
        }
    }

    @Override
    public void disconnect() {
        sendPacket(GameCommands.CMD_BYE);
        mIsDisconnecting = true;
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
                return null;
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
