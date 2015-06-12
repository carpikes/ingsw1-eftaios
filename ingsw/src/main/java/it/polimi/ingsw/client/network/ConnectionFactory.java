package it.polimi.ingsw.client.network;


/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public class ConnectionFactory {
    public static final int CONNECTION_TCP = 0;
    public static final int CONNECTION_RMI = 1;
    private static String[] tAssoc = {"TCP", "RMI"};

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
     */
    public static Connection getConnection(int type) {
        switch(type) {
            case CONNECTION_TCP: return new TCPConnection();
            case CONNECTION_RMI: return new RMIConnection();
            default:             return null;
        }
    }
}
