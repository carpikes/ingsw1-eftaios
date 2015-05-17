package it.polimi.ingsw.client.cli;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

class CLIGame implements OnReceiveListener {
    private Connection mConn;
    private LinkedBlockingQueue<String> mQueue;
    private boolean mMustClose = false;

    private static void banner() {
        System.out.println("*******************************************************************************");
        System.out.println("*******************************************************************************");
        System.out.println("***  ______        _____       _____                    _____       ______  ***");  
        System.out.println("*** |  ____|      / ____|     / ____|        /\\        |  __ \\     |  ____| ***");  
        System.out.println("*** | |__        | (___      | |            /  \\       | |__) |    | |__    ***");  
        System.out.println("*** |  __|        \\___ \\     | |           / /\\ \\      |  ___/     |  __|   ***");  
        System.out.println("*** | |____       ____) |    | |____      / ____ \\     | |         | |____  ***");  
        System.out.println("*** |______|     |_____/      \\_____|    /_/    \\_\\    |_|         |______| ***");  
        System.out.println("***  ___ ___  ___  __  __    _____ _  _ ___      _   _    ___ ___ _  _ ___  ***");
        System.out.println("*** | __| _ \\/ _ \\|  \\/  |  |_   _| || | __|    /_\\ | |  |_ _| __| \\| / __| ***");
        System.out.println("*** | _||   / (_) | |\\/| |    | | | __ | _|    / _ \\| |__ | || _|| .` \\__ \\ ***");
        System.out.println("*** |_| |_|_\\\\___/|_|  |_|    |_| |_||_|___|  /_/ \\_\\____|___|___|_|\\_|___/ ***");
        System.out.println("***  ___  _  _    ___   _   _  _____  ___  ___    ___  ___   _    ___  ___  ***");
        System.out.println("*** |_ _|| \\| |  / _ \\ | | | ||_   _|| __|| _ \\  / __|| _ \\ /_\\  / __|| __| ***");
        System.out.println("***  | | | .` | | (_) || |_| |  | |  | _| |   /  \\__ \\|  _// _ \\| (__ | _|  ***");
        System.out.println("*** |___||_|\\_|  \\___/  \\___/   |_|  |___||_|_\\  |___/|_| /_/ \\_\\\\___||___| ***");
        System.out.println("***                                                                         ***");
        System.out.println("*** Command line client                by Michele Albanese & Alain Carlucci ***");
        System.out.println("*******************************************************************************");
        System.out.println("*******************************************************************************");
        System.out.println("");
    }

    public CLIGame() {
        mQueue = new LinkedBlockingQueue<String>();
        CLIGame.banner();
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
            }
            paramsConfig.put(param.getKey(), value);
        }
        mConn.setConfiguration(paramsConfig);
        mConn.setOnReceiveListener(this);
    }

    public void run() {
        String name = "";
        
        try {
            mConn.connect();
        } catch (IOException e) {
            System.out.println("Cannot connect: " + e.toString());
            return;
        }
        IO.write("Connected to server.");
        IO.write("Hello! What's your name?");
        try {
            // Choose username
            boolean nickOk = false;
            do {
                name = IO.readString().trim();
                mConn.sendMessage(name);
                while(!mMustClose) {
                    String cmd = mQueue.poll(1, TimeUnit.SECONDS);
                    if(cmd == null)
                        continue;
                    
                    if(cmd.startsWith("USEROK ")) {
                        nickOk = true;
                        break;
                    }
                    if(cmd.equals("USERFAIL"))
                        break;
                }
                if(!nickOk && !mMustClose)
                    IO.write("Another player is using your name. Choose another one.");
            }while(!nickOk && !mMustClose);
            
            if(!mMustClose)
                IO.write("Welcome, " + name);

        } catch (InterruptedException e) {
            return;
        }

        // Main Loop
        try {
            IO.write("Waiting for other players");
            String cmd = null;
            while((cmd == null || !cmd.equals("RUN")) && !mMustClose) {
                cmd = mQueue.poll(1, TimeUnit.SECONDS);
            }
            
            if(!mMustClose)
                IO.write("Game started. Good luck!");
            
            while(mConn.isOnline() && !mMustClose) {
                Thread.sleep(1000);
            }
        }catch(Exception e) {
            return;
        }
    }

    @Override
    public void onReceive(String msg) {
        try {
            mQueue.put(msg);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect() {
        mMustClose = true;
    }
}
