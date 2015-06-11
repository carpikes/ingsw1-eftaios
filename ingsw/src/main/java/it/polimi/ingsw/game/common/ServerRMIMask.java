package it.polimi.ingsw.game.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 16, 2015
 */
public interface ServerRMIMask extends Remote {
    public String registerAndGetId() throws RemoteException;
    public void onRMICommand(String clientId, GameCommand pkt) throws RemoteException;
    public GameCommand[] readCommands(String clientId) throws RemoteException;
}
