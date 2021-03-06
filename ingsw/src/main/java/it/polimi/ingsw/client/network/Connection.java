package it.polimi.ingsw.client.network;

import it.polimi.ingsw.common.GameCommand;

import java.io.IOException;

/** Handle connection to the server 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 10, 2015
 */
public abstract class Connection {
    /** This listener is called on each received packet */
    protected OnReceiveListener mListener = null;

    /** Sets the listener
     * 
     * @param listener The new listener
     */
    public void setOnReceiveListener(OnReceiveListener listener) {
        mListener = listener;
    }

    /** Set the connection host
     * 
     * @param host The host
     */
    public abstract void setHost(String host);

    /** Start the connection
     * 
     * @throws IOException Can't establish a connection
     */
    public abstract void connect() throws IOException;

    /** Disconnect */
    public abstract void disconnect();
    
    /** Send a packet to the server
     * 
     * @param cmd The command
     */
    public abstract void sendCommand(GameCommand cmd);

    /** Check if the client is correctly connected
     * 
     * @return True if is online
     */
    public abstract boolean isOnline();
}
