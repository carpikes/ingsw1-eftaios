package it.polimi.ingsw.client.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory {
    public static final int CONNECTION_TCP = 1;
    public static final int CONNECTION_RMI = 2;

    private static final Map<Integer, String> mAssoc;

    static {
        Map<Integer, String> tAssoc = new HashMap<Integer, String>();
        tAssoc.put(CONNECTION_TCP, "TCP");
        tAssoc.put(CONNECTION_RMI, "RMI");
        mAssoc = Collections.unmodifiableMap(tAssoc); 
    }

	public static final Map<Integer,String> getConnectionList() {
	    return mAssoc;
	}

	public static Connection getConnection(int type) {
	    switch(type) {
	        case CONNECTION_TCP: return new TCPConnection();
            case CONNECTION_RMI: return new RMIConnection();
	    }
	    return null;
	}
}
