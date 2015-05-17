package it.polimi.ingsw.client.network;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

public abstract class Connection {
    public class ParametersType {
        public static final int TYPE_INTEGER = 0;
        public static final int TYPE_STRING = 1;
    }

    protected OnReceiveListener mListener = null;
    protected Map<String, Integer> mConfigParams;

    Connection() {
        mConfigParams = new TreeMap<String, Integer>();
    }
    
    public void setOnReceiveListener(OnReceiveListener listener) {
        mListener = listener;
    }

    public Map<String, Integer> getConfigurationParameters() {
        return mConfigParams;
    }

    public abstract void setConfiguration(Map<String, Object> obj);
    public abstract void connect() throws IOException;
    public abstract void disconnect();
    public abstract void sendMessage(String msg);
    public abstract boolean isOnline();
}
