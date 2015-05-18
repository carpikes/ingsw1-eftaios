package it.polimi.ingsw.client.cli;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public class CLIView implements OnReceiveListener, View {
    private static final Logger LOG = Logger.getLogger(CLIView.class.getName());
    private Connection mConn;
    private LinkedBlockingQueue<NetworkPacket> mQueue;
    private boolean mMustClose = false;

    private static void banner() {
        IO.write("*******************************************************************************");
        IO.write("*******************************************************************************");
        IO.write("***  ______        _____       _____                    _____       ______  ***");  
        IO.write("*** |  ____|      / ____|     / ____|        /\\        |  __ \\     |  ____| ***");  
        IO.write("*** | |__        | (___      | |            /  \\       | |__) |    | |__    ***");  
        IO.write("*** |  __|        \\___ \\     | |           / /\\ \\      |  ___/     |  __|   ***");  
        IO.write("*** | |____       ____) |    | |____      / ____ \\     | |         | |____  ***");  
        IO.write("*** |______|     |_____/      \\_____|    /_/    \\_\\    |_|         |______| ***");  
        IO.write("***  ___ ___  ___  __  __    _____ _  _ ___      _   _    ___ ___ _  _ ___  ***");
        IO.write("*** | __| _ \\/ _ \\|  \\/  |  |_   _| || | __|    /_\\ | |  |_ _| __| \\| / __| ***");
        IO.write("*** | _||   / (_) | |\\/| |    | | | __ | _|    / _ \\| |__ | || _|| .` \\__ \\ ***");
        IO.write("*** |_| |_|_\\\\___/|_|  |_|    |_| |_||_|___|  /_/ \\_\\____|___|___|_|\\_|___/ ***");
        IO.write("***  ___  _  _    ___   _   _  _____  ___  ___    ___  ___   _    ___  ___  ***");
        IO.write("*** |_ _|| \\| |  / _ \\ | | | ||_   _|| __|| _ \\  / __|| _ \\ /_\\  / __|| __| ***");
        IO.write("***  | | | .` | | (_) || |_| |  | |  | _| |   /  \\__ \\|  _// _ \\| (__ | _|  ***");
        IO.write("*** |___||_|\\_|  \\___/  \\___/   |_|  |___||_|_\\  |___/|_| /_/ \\_\\\\___||___| ***");
        IO.write("***                                                                         ***");
        IO.write("*** Command line client                by Michele Albanese & Alain Carlucci ***");
        IO.write("*******************************************************************************");
        IO.write("*******************************************************************************");
        IO.write("");
    }

    public CLIView() {
        mQueue = new LinkedBlockingQueue<NetworkPacket>();
        CLIView.banner();
        IO.write("Which connection do you want to use?");
        Map<Integer,String> mConnectionList = ConnectionFactory.getConnectionList();

        Integer min = null, max = null;
        for(Map.Entry<Integer, String> m : mConnectionList.entrySet()) {
            if(min == null) min = m.getKey();
            if(max == null) max = m.getKey();
            min = Math.min(m.getKey(), min);
            max = Math.max(m.getKey(), max);
            IO.write(m.getKey() + ") " + m.getValue());
        }

        int connType = IO.readRangeInt(min, max);

        mConn = ConnectionFactory.getConnection(connType);

        Map<String, Integer> paramsList = mConn.getConfigurationParameters();
        Map<String, Object> paramsConfig = new TreeMap<String, Object>();
        for(Map.Entry<String, Integer> param : paramsList.entrySet()) {
            Object value = null;
            IO.write("Type the " + param.getKey());
            switch(param.getValue()) {
                case Connection.ParametersType.TYPE_INTEGER:
                    value = IO.readInt();
                    break;
                case Connection.ParametersType.TYPE_STRING:
                    value = IO.readString();
                    break;
                default:
                    throw new RuntimeException("Unsupported type");
            }
            paramsConfig.put(param.getKey(), value);
        }
        mConn.setConfiguration(paramsConfig);
        mConn.setOnReceiveListener(this);
    }

    @Override
    public void run() {
        try {
            mConn.connect();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot connect: " + e.toString(), e);
            return;
        }
        IO.write("Connected to server.");
        IO.write("Hello! What's your name?");
        askUsername();
        mainLoop();
        IO.write("Goodbye!");
    }

    private void askUsername() {
        String name;
        try {
            // Choose username
            boolean nickOk = false;
            do {
                name = IO.readString().trim();
                mConn.sendPacket(new NetworkPacket(GameCommands.CMD_CS_USERNAME, name));
                while(!mMustClose) {
                    NetworkPacket cmd = mQueue.poll(1, TimeUnit.SECONDS);
                    if(cmd == null)
                        continue;
                    
                    if(cmd.getOpcode() == GameCommands.CMD_SC_USEROK) {
                        nickOk = true;
                        break;
                    }
                    if(cmd.getOpcode() == GameCommands.CMD_SC_USERFAIL)
                        break;
                }
                if(!nickOk && !mMustClose)
                    IO.write("Another player is using your name. Choose another one.");
            }while(!nickOk && !mMustClose);
            
            if(!mMustClose)
                IO.write("Welcome, " + name);

        } catch (InterruptedException e) {
            LOG.log(Level.FINER, e.toString(), e);
            return;
        }
    }

    private void mainLoop() {
        try {
            IO.write("Waiting for other players");
            NetworkPacket cmd = null;
            while((cmd == null || cmd.getOpcode() != GameCommands.CMD_SC_RUN) && !mMustClose)
                cmd = mQueue.poll(1, TimeUnit.SECONDS);
            
            if(!mMustClose)
                IO.write("Game started. Good luck!");
            
            while(mConn.isOnline() && !mMustClose) {
                Thread.sleep(1000);
            }
        }catch(Exception e) {
            LOG.log(Level.FINER, e.toString(), e);
        }
    }
    
    @Override
    public void onReceive(NetworkPacket pkt) {
        try {
            mQueue.put(pkt);
        } catch (InterruptedException e) {
            LOG.log(Level.FINER, e.toString(), e);
        }
    }

    @Override
    public void onDisconnect() {
        mMustClose = true;
    }
}
