package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.NetworkPacket;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Player manager
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 8, 2015
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    
    /** Connection to the client*/
    private final ClientConn mConn;
    
    /** Game he is playing */
    private final Game mGame;
    
    /** Client current state (Choosing username, playing */
    private ClientState mCurState;
    
    /** Username */
    private String mUser = null;

    /** The constructor
     * 
     * @param conn Connection to the client
     * @param game Game he is playing
     */
    public Client(ClientConn conn, Game game) {
        mConn = conn;
        mGame = game;
        mCurState = new ClientStateConnecting(this, game);

        /* Enable bidirectional communication Client <-> ClientConn */
        mConn.setClient(this);  
    }

    /** Handle a new incoming packet
     * 
     * @param pkt The packet
     */
    public synchronized void handlePacket(NetworkPacket pkt) {
        synchronized(mCurState) {
            if(mCurState != null)
                mCurState.handlePacket(pkt);
        }
    }
    
    /** Send a packet through the network
     * 
     * @param pkt The packet
     */
    public void sendPacket(NetworkPacket pkt) {
        mConn.sendPacket(pkt);
    }
    
    /** Send a packet without arguments through the network
     * 
     * @param opcode The opcode
     */
    public void sendPacket(int opcode) {
        sendPacket(new NetworkPacket(opcode));
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
            mGame.removeClient(this);
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

    /** Change the state to "Game ready" */
    public synchronized void setGameReady() {
        if(mCurState instanceof ClientStateInGame)
            throw new RuntimeException("This player is already in game");
        if(mUser == null)
            throw new RuntimeException("This player has no name");
        
        mCurState = new ClientStateInGame(this, mGame);
    }
    
    /** Check if the state is GameReady */
    public synchronized boolean isGameReady() {
        return (mCurState instanceof ClientStateInGame && mGame.isRunning());
    }

    
    /** Update timeouts */
    public void update() {
        if(mConn.isTimeoutTimerElapsed()) {
            LOG.log(Level.WARNING, "Ping timeout. Disconnecting.");
            handleDisconnect();
        }
    }
}
