package it.polimi.ingsw.server;

class Client {
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

    public synchronized void handleMessage(String msg) {
        synchronized(mCurState) {
            if(mCurState != null)
                mCurState.handleMessage(msg);
        }
    }
    
    public void sendMessage(String msg) {
        mConn.sendCommand(msg);
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
        synchronized(mUser) {
            if(mUser == null)
                throw new RuntimeException("Username is already set");
            
            mUser = msg;
        }
    }

    public synchronized void setGameReady() {
        if(mCurState instanceof ClientStateInGame)
            throw new RuntimeException("This player is already in game");
        if(mUser == null)
            throw new RuntimeException("This player has no name");
        
        mCurState = new ClientStateInGame(this, mGame);
    }
}
