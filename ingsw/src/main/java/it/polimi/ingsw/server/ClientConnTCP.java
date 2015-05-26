package it.polimi.ingsw.server;

import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** TCP connection Handler
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 8, 2015
 */
public class ClientConnTCP extends ClientConn {
    private static final Logger LOG = Logger.getLogger(ClientConnTCP.class.getName());
    
    /** Socket output stream */
    private final ObjectOutputStream mOut;
    
    /** Socket input stream */
    private final ObjectInputStream mIn;
    
    /** The socket */
    private Socket mSocket;

    /** Constructor
     * 
     * @param socket        Socket to bind
     * @throws IOException  Socket errors
     */
    public ClientConnTCP(Socket socket) throws IOException{
        mOut = new ObjectOutputStream(socket.getOutputStream());
        mIn = new ObjectInputStream(socket.getInputStream());
        mSocket = socket;
    }

    /** Listen on incoming packets */
    @Override
    public void run() {
        try {
            while(mSocket.isConnected()) {
                Object obj = mIn.readObject();
                if(obj != null && mClient != null && obj instanceof NetworkPacket) {
                    resetTimeoutTimer();
                    mClient.handlePacket((NetworkPacket)obj);
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            LOG.log(Level.FINE, "Connection closed: " + e.toString(), e);
        } finally {
            mClient.handleDisconnect();
        }
    }

    /** Send a packet through this socket
     * 
     * @param pkt The packet
     */
    @Override
    public synchronized void sendPacket(NetworkPacket pkt) {
        try {
            mOut.writeObject(pkt);
            mOut.flush();
        } catch (IOException e) {
            LOG.log(Level.FINEST, e.toString(), e);
            mClient.handleDisconnect();
        }
    }

    /** Close this connection */
    @Override
    public synchronized void disconnect() {
        if(mOut != null) {
            try {
                mOut.writeObject(new NetworkPacket(GameCommand.CMD_BYE));
                mOut.flush();
            } catch(IOException e) {
                LOG.log(Level.FINER, e.toString(), e);
            }
        }
        try {
            mOut.close();
            mIn.close();
            mSocket.close();
        } catch (IOException e) {
            LOG.log(Level.FINE, "Sockets are already closed: " + e.toString(), e);
        }
        
        mSocket = null;
    }
    
    /** Check if this client is connected
     * 
     * @return True if this client is online
     */
    @Override
    public synchronized boolean isConnected() {
        if(mSocket == null)
            return false;
        return mSocket.isConnected();
    }
}
