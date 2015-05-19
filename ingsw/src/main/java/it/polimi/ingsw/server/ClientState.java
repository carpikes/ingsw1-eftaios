package it.polimi.ingsw.server;

import it.polimi.ingsw.game.network.NetworkPacket;

/** Client state interface
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 15, 2015
 */
public abstract class ClientState {
    /** The player */
    protected Client mClient;
    
    /** The game he is playing */
    protected GameManager mGame;
    
    /** Constructor
     * 
     * @param c     Client instance
     * @param g     Game instance
     */
    public ClientState(Client c, GameManager g) {
        mClient = c;
        mGame = g;
    }
    
    /** Handle an incoming packet
     * 
     * @param pkt The incoming packet
     */
    public abstract void handlePacket(NetworkPacket pkt);
}
