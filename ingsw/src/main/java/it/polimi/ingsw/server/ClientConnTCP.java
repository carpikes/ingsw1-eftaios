package it.polimi.ingsw.server;

import it.polimi.ingsw.common.CoreOpcode;
import it.polimi.ingsw.common.GameCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** TCP connection Handler
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 8, 2015
 */
public class ClientConnTCP extends ClientConn {
    /** Logger */
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

    /** Run the reader thread
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            while(mSocket.isConnected()) {
                Object obj = mIn.readObject();
                if(obj != null && mClient != null && obj instanceof GameCommand) {
                    resetTimeoutTimer();

                    if(((GameCommand)obj).getOpcode() == CoreOpcode.CMD_PING)
                        continue;
                    mClient.handlePacket((GameCommand)obj);
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            LOG.log(Level.FINE, "Connection closed: " + e.toString(), e);
        } finally {
            mClient.handleDisconnect();
        }
    }

    /** Send a GameCommand to this client
     * @see it.polimi.ingsw.server.ClientConn#sendPacket(it.polimi.ingsw.common.GameCommand)
     */
    @Override
    public synchronized void sendPacket(GameCommand pkt) {
        try {
            mOut.writeObject(pkt);
            mOut.flush();
        } catch (IOException e) {
            LOG.log(Level.FINEST, e.toString(), e);
            mClient.handleDisconnect();
        }
    }

    /** Disconnect 
     * @see it.polimi.ingsw.server.ClientConn#disconnect()
     */
    @Override
    public synchronized void disconnect() {
        if(mOut != null) {
            try {
                mOut.writeObject(new GameCommand(CoreOpcode.CMD_BYE));
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

    /** Return true if the socket is connected
     *
     * @see it.polimi.ingsw.server.ClientConn#isConnected()
     * @return True if this connection is established
     */
    @Override
    public synchronized boolean isConnected() {
        if(mSocket == null)
            return false;
        return mSocket.isConnected();
    }
}
