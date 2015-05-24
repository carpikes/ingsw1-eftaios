package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */

public class Controller implements OnReceiveListener {
    private View mView;
    private Connection mConn;
    private LinkedBlockingQueue<NetworkPacket> mQueue;
    private boolean mMustClose = false;
    
    public Controller() {
        mQueue = new LinkedBlockingQueue<NetworkPacket>();
    }
    
    public void setView(View v) {
        mView = v;
    }
    
    public void run() {
        String[] connList = ConnectionFactory.getConnectionList();
        
        int conn = mView.askConnectionType(connList);
        mConn = ConnectionFactory.getConnection(conn);
        mConn.setOnReceiveListener(this);
        if(mConn == null)
            return;
        
        String host = mView.askHost().trim();
        if(host.length() == 0)
            return;
        
        mConn.setHost(host);
        
        try {
            mConn.connect();
        } catch (IOException e) {
            mView.showError("Cannot connect to " + host + ": " + e.toString());
            return;
        }
        
        while(!mConn.isOnline()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        mView.run();
        
        String user = "";
        while(!mMustClose) {
            try {
                NetworkPacket cmd = mQueue.poll(1, TimeUnit.SECONDS);
                if(cmd == null)
                    continue;
                
                switch(cmd.getOpcode()) {
                    case GameCommands.CMD_SC_TIME:
                        mView.updateLoginTime(Integer.parseInt((String) cmd.getArgs()[0]));
                        break;
                    case GameCommands.CMD_SC_STAT:
                        mView.updateLoginStat(Integer.parseInt((String) cmd.getArgs()[0]));
                        break;
                    case GameCommands.CMD_SC_USERFAIL:
                        user = mView.askUsername("Another player is using your name. Choose another one.");
                        mConn.sendPacket(new NetworkPacket(GameCommands.CMD_CS_USERNAME, user));
                        break;
                    case GameCommands.CMD_SC_CHOOSEUSER:
                        user = mView.askUsername("Choose a username");
                        mConn.sendPacket(new NetworkPacket(GameCommands.CMD_CS_USERNAME, user));
                        break;
                    case GameCommands.CMD_SC_USEROK:
                        break;
                    case GameCommands.CMD_SC_CHOOSEMAP:
                        break;
                    case GameCommands.CMD_SC_MAPOK:
                        break;
                    case GameCommands.CMD_SC_MAPFAIL:
                        break;
                    case GameCommands.CMD_SC_RUN:
                        break;
                    case GameCommands.CMD_BYE:
                        break;

                };
            } catch(InterruptedException e) {
                mMustClose = true;
                break;
            }
        }

        mView.run();
        
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.network.OnReceiveListener#onReceive(it.polimi.ingsw.game.network.NetworkPacket)
     */
    @Override
    public void onReceive(NetworkPacket obj) {
        synchronized(mQueue) {
            mQueue.add(obj);
        }
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.network.OnReceiveListener#onDisconnect()
     */
    @Override
    public void onDisconnect() {
        // TODO Auto-generated method stub
        
    }
}
