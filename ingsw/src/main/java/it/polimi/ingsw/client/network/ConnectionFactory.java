package it.polimi.ingsw.client.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public class ConnectionFactory {
    public static final int CONNECTION_TCP = 0;
    public static final int CONNECTION_RMI = 1;
    private static String[] tAssoc = {"TCP", "RMI"};

    public static final String[] getConnectionList() {
        return tAssoc;
    }

    public static Connection getConnection(int type) {
        switch(type) {
            case CONNECTION_TCP: return new TCPConnection();
            case CONNECTION_RMI: return new RMIConnection();
        }
        return null;
    }
}
