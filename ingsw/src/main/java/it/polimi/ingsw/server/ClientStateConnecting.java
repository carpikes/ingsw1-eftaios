package it.polimi.ingsw.server;

public class ClientStateConnecting extends ClientState{

    public ClientStateConnecting(Client c, Game g) {
        super(c,g);
    }

    /**
     * Handle an incoming network message 
     * @param msg In this state, the only allowed msg is the player username.
     */
    @Override
    public void handleMessage(String msg) {
        synchronized(mGame) {
            synchronized(mClient) {
                if(mGame.canSetName(msg)) {
                    mClient.setUsername(msg);
                    mClient.sendMessage("USEROK " + mGame.getNumberOfPlayers() + " " + mGame.getRemainingTime());
                    mClient.setGameReady();
                } else {
                    mClient.sendMessage("USERFAIL");
                }
            }
        }
    }

}
