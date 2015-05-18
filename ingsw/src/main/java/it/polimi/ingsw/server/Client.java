package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.NetworkPacket;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 8, 2015
 */

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    private final ClientConn mConn;
    private final Game mGame;
    private ClientState mCurState;
    private String mUser = null;

    public Client(ClientConn conn, Game game) {
        mConn = conn;
        mGame = game;
        mCurState = new ClientStateConnecting(this, game);

        /* Enable bidirectional communication Client <-> ClientConn */
        mConn.setClient(this);  
    }

    public synchronized void handlePacket(NetworkPacket pkt) {
        synchronized(mCurState) {
            if(mCurState != null)
                mCurState.handlePacket(pkt);
        }
    }
    
    public void sendPacket(NetworkPacket pkt) {
        mConn.sendPacket(pkt);
    }
    
    public void sendPacket(int opcode) {
        sendPacket(new NetworkPacket(opcode));
    }

    public String getUsername() {
        return mUser;
    }

    public boolean hasUsername() {
        if(mUser != null && mUser.length() > 0)
            return true;
        return false;
    }

    public void handleDisconnect() {
        mConn.disconnect();
        mGame.removeClient(this);
    }

    public void setUsername(String msg) {
        if(mUser != null)
            throw new RuntimeException("Username is already set");
        mUser = msg;
    }

    public synchronized void setGameReady() {
        if(mCurState instanceof ClientStateInGame)
            throw new RuntimeException("This player is already in game");
        if(mUser == null)
            throw new RuntimeException("This player has no name");
        
        mCurState = new ClientStateInGame(this, mGame);
    }
    
    public void update() {
        if(mConn.isTimeoutTimerElapsed()) {
            LOG.log(Level.WARNING, "Ping timeout. Disconnecting.");
            handleDisconnect();
        }
    }
}
