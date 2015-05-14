package it.polimi.ingsw.server;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 8, 2015
 */

abstract class ClientConn implements Runnable{
    protected Client mClient = null;

    public void setClient(Client client) {
        mClient = client;
    }

    public abstract void sendCommand(String cmd);
    public abstract void disconnect();
}
