package it.polimi.ingsw.server;

public abstract class ClientState {
    protected Client mClient;
    protected Game mGame;
    public ClientState(Client c, Game g) {
        mClient = c;
        mGame = g;
    }
    
    public abstract void handleMessage(String msg);
}
