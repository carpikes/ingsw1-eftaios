package it.polimi.ingsw.server;

import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 8, 2015
 */

public abstract class ClientConn implements Runnable {
    protected Client mClient = null;
    protected long mLastPingTime;
    protected boolean mIsConnected = false;
    public ClientConn() {
        resetTimeoutTimer();
    }
    
    public void setClient(Client client) {
        mClient = client;
    }
    
    public synchronized boolean isTimeoutTimerElapsed() {
        return (System.currentTimeMillis() - mLastPingTime) > Config.SERVER_CONNECTION_TIMEOUT; 
    }
    
    public synchronized void resetTimeoutTimer() {
        mLastPingTime = System.currentTimeMillis();
    }

    public abstract void sendPacket(NetworkPacket pkt);
    public abstract void disconnect();
    
    public boolean isConnected() {
        return mIsConnected;
    }
    
    public void sendPacket(int opcode) {
        sendPacket(new NetworkPacket(opcode));
    }
    
}
