package it.polimi.ingsw.server;


class Client {

    private final ClientConn mConn;
    private final Game mGame;
    private boolean mWaitingForName = true;
    private String mUser = null;

    public Client(ClientConn conn, Game game) {
        mConn = conn;
        mGame = game;

        /* Enable bidirectional communication Client <-> ClientConn */
        mConn.setClient(this);  
    }

    public void handleMessage(String msg) {
        if(mWaitingForName) {
            // msg = username

            if(mGame.canSetName(msg)) {
                // FIXME: Race Condition here!
                mUser = msg;
                sendMessage("OK " + mGame.getNumberOfPlayers() + " " + mGame.getRemainingTime());
                mWaitingForName = false;
            } else {
                sendMessage("FAIL");
            }
        } else {
            if(mGame.isRunning()) {

            } else {
                //nothing here
            }
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
        // TODO
        mConn.disconnect();
    }
}
