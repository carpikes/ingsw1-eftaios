package it.polimi.ingsw.server;

import it.polimi.ingsw.game.common.CoreOpcode;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.Opcode;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Player (connection) manager
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 8, 2015
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    /** Connection to the client*/
    private final ClientConn mConn;

    /** Game he is playing */
    private final GameManager mGame;

    /** My username */
    private String mUser = null;

    /** The constructor
     * 
     * @param conn Connection to the client
     * @param game Game he is playing
     */
    public Client(ClientConn conn, GameManager game) {
        mConn = conn;
        mGame = game;

        /* Enable bidirectional communication Client <-> ClientConn */
        mConn.setClient(this);  
    }

    /** Handle a new incoming packet
     * 
     * @param pkt The packet
     */
    public synchronized void handlePacket(GameCommand pkt) {
        if(!isConnected())
            return;

        if(!mGame.isRunning()) {
            try {
                Serializable[] args = pkt.getArgs();
                if(args == null || args.length == 0)
                    return;

                /** Choosing username */
                synchronized(mGame) {
                    if(pkt.getOpcode() instanceof CoreOpcode) {
                        CoreOpcode opcode = (CoreOpcode) pkt.getOpcode();
                        switch(opcode) {
                            case CMD_CS_USERNAME:
                                String name = (String) args[0];
                                if(mGame.canSetName(name) && mUser == null) {
                                    setUsername(name);
                                    sendPacket(new GameCommand(CoreOpcode.CMD_SC_USERNAMEOK, mGame.getNumberOfClients(), mGame.getRemainingLoginTime()));
                                } else
                                    sendPacket(CoreOpcode.CMD_SC_USERNAMEFAIL);
                                break;
                            case CMD_CS_LOADMAP:
                                if(args.length == 1 && args[0] instanceof Integer && mGame.setMap(this, (Integer)args[0]))
                                    sendPacket(CoreOpcode.CMD_SC_MAPOK);
                                else
                                    sendPacket(CoreOpcode.CMD_SC_MAPFAIL);
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch( Exception e ) {
                LOG.log(Level.SEVERE, "Unknown packet: " + e.toString(), e);
            }
        } else
            mGame.handlePacket(this, pkt);
    }

    /** Send a packet through the network
     * 
     * @param pkt The packet
     */
    public void sendPacket(GameCommand pkt) {
        if(!isConnected())
            throw new RuntimeException("You can't send data to an inactive client");

        mConn.sendPacket(pkt);
    }

    /** Send a packet without arguments through the network
     * 
     * @param opcode The opcode
     */
    public void sendPacket(Opcode opcode) {
        sendPacket(new GameCommand(opcode));
    }

    /** Get the player username
     * 
     * @return the username
     */
    public String getUsername() {
        return mUser;
    }

    /** Check if the player has a username
     * 
     * @return True if the username is set
     */
    public boolean hasUsername() {
        if(mUser != null && mUser.length() > 0)
            return true;
        return false;
    }

    /** Disconnect the client */
    public synchronized void handleDisconnect() {
        if(mConn.isConnected()) {
            mConn.disconnect();
            mGame.enqueueRemoveClient(this);
        }
    }

    /** Set a new username 
     * This method could be called only once
     *
     * @param username The new username
     */
    public void setUsername(String username) {
        if(mUser != null)
            throw new RuntimeException("Username is already set");
        mUser = username;
    }

    /** Update timeouts */
    public void update() {
        if(!isConnected())
            return;

        if(mConn.isTimeoutTimerElapsed()) {
            LOG.log(Level.WARNING, "Ping timeout. Disconnecting.");
            handleDisconnect();
        }
    }

    /** Return the Connection
     *
     * @return The connection
     */
    public ClientConn getConnection() {
        return mConn;
    }

    /** Returns true if this connection is established
     *
     * @return True if online
     */
    public boolean isConnected() {
        return mConn.isConnected();
    }
}
