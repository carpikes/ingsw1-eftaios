package it.polimi.ingsw.game.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** RMI Mask
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 16, 2015
 */
public interface ServerRMIMask extends Remote {
    
    /** Call this guy to register your client and get an unique token
     * 
     * @return An unique token
     * @throws RemoteException If connection is broken
     */
    public String registerAndGetId() throws RemoteException;
    
    /** Call this guy to send a command (from client) TO server
     * 
     * @param clientId Your token
     * @param pkt The command
     * @throws RemoteException If connection is broken
     */
    public void onRMICommand(String clientId, GameCommand pkt) throws RemoteException;
    
    /** Call this method to read your pending packets
     * E.G. Packet from Server to Client
     * (This is because RMI is NOT bidirectional)
     * 
     * @param clientId Your token
     * @return A list of pending packets
     * @throws RemoteException If connection is broken
     */
    public GameCommand[] readCommands(String clientId) throws RemoteException;
}
