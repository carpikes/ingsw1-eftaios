package it.polimi.ingsw.server;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 16, 2015
 */

public class ClientRMIConn extends ClientConn{
    private static final Logger mLog = Logger.getLogger(ClientRMIConn.class.getName());
    private boolean mIsOnline = true; // FIXME: Let's assume that our client is always online
    private LinkedBlockingQueue<String> mIncomingQueue;
    private Queue<String> mOutgoingQueue;
    private final String mUniqueId;
    private final ServerRMI mServer;
    
    public ClientRMIConn(ServerRMI server, String uniqueId) {
        super();
        mIncomingQueue = new LinkedBlockingQueue<String>();
        mOutgoingQueue = new LinkedList<String>();
        mUniqueId = uniqueId;
        mServer = server;
    }
    
    @Override
    public void run() {
        /*try {
            while(mIsOnline) {
                String msg = mIncomingQueue.poll(1, TimeUnit.SECONDS);
                if(msg != null && mIsOnline && mClient != null)
                    mClient.handleMessage(msg);
            }
        } catch(InterruptedException e) {
            mLog.log(Level.FINE, "RMI Connection Thread interrupted (" + e.toString() + "). Disconnecting...");
        } finally {
            mClient.handleDisconnect();
        }*/
    }

    @Override
    public void sendCommand(String cmd) {
        synchronized(mOutgoingQueue) {
            mOutgoingQueue.add(cmd);
        }
    }

    @Override
    public void disconnect() {
        mIsOnline = false;
        synchronized(mOutgoingQueue) {
            mOutgoingQueue.add("BYE");
        }
    }

    public void onRMICommand(String msg) throws RemoteException {
        mLog.log(Level.INFO, "Reading " + msg + " via RMI");
        if(mClient != null && msg != null)
            mClient.handleMessage(msg);
    }

    public String[] readCommands() {
        synchronized(mOutgoingQueue) {
            if(mOutgoingQueue.isEmpty())
                return null;
            String[] out = new String[mOutgoingQueue.size()];
            
            int i = 0;
            while(!mOutgoingQueue.isEmpty())
                out[i++] = mOutgoingQueue.poll();
            return out;
        }
    }

}
