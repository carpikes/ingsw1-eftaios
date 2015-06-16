package it.polimi.ingsw.client.network;

/** Connection factory
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 10, 2015
 */
public class ConnectionFactory {
    
    /** TCP Connection id */
    public static final int CONNECTION_TCP = 0;
    /** RMI Connection id */
    public static final int CONNECTION_RMI = 1;
    
    /** Array */
    private static String[] tAssoc = {"TCP", "RMI"};
    
    /** Private constructor */
    private ConnectionFactory() {
        /** unused */
    }
    
    /** Returns a list of available connections
     * 
     * @return The list
     */
    public static final String[] getConnectionList() {
        return tAssoc;
    }

    /** Get an instance of the chosen connection
     *
     * @param type The chosen connection type
     * @return The specified connection
     */
    public static Connection getConnection(int type) {
        switch(type) {
            case CONNECTION_TCP: return new TCPConnection();
            case CONNECTION_RMI: return new RMIConnection();
            default:             return null;
        }
    }
}
