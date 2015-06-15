package it.polimi.ingsw.server;

import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.Opcode;
import it.polimi.ingsw.game.config.Config;

/** Common client connection interface 
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 8, 2015
 */
public abstract class ClientConn implements Runnable {
    /** The client */
    protected Client mClient = null;

    /** Last time we received a packet */
    protected long mLastPingTime;

    /** True if the connection is online */
    protected boolean mIsConnected = false;

    public ClientConn() {
        resetTimeoutTimer();
    }

    /** Set a new client
     *
     * @param client The client
     */
    public void setClient(Client client) {
        if(mClient != null)
            throw new RuntimeException("This connection already has a client");
        mClient = client;
    }

    /** Check if the client is not responding (Ping timeout)
     * 
     * @return True if the client is not responding
     */
    public synchronized boolean isTimeoutTimerElapsed() {
        return (System.currentTimeMillis() - mLastPingTime) > Config.SERVER_CONNECTION_TIMEOUT; 
    }

    /** Call this on a new incoming packet. */
    public synchronized void resetTimeoutTimer() {
        mLastPingTime = System.currentTimeMillis();
    }

    /** Send a packet through this socket
     * 
     * @param pkt The packet
     */
    public abstract void sendPacket(GameCommand pkt);

    /** Close this connection */
    public abstract void disconnect();

    /** Check if this client is connected
     * 
     * @return True if this client is online
     */
    public boolean isConnected() {
        return mIsConnected;
    }

    /** Send a packet without arguments through this socket
     * 
     * @param opcode An opcode
     */
    public void sendPacket(Opcode opcode) {
        sendPacket(new GameCommand(opcode));
    }

}
