package it.polimi.ingsw.client.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class Connection {
    public class ParametersType {
        public static final int TYPE_INTEGER = 0;
        public static final int TYPE_STRING = 1;
    }

    protected Map<String, Integer> mConfigParams;

    Connection() {
        mConfigParams = new TreeMap<String, Integer>();
    }

    public Map<String, Integer> getConfigurationParameters() {
        return mConfigParams;
    }

    public abstract void setConfiguration(Map<String, Object> obj);
    public abstract void connect() throws IOException;
    public abstract void sendMessage(String msg);
    public abstract void setOnReceiveListener(OnReceiveListener listener);
    public abstract boolean isOnline();
}
