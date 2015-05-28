package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.network.GameInfoContainer;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/** Client controller
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public class Controller implements OnReceiveListener {
    /** The view */
    private View mView;
    
    /** The connection */
    private Connection mConn;
    
    /** List of incoming packets/events */
    private LinkedBlockingQueue<NetworkPacket> mQueue;
    
    /** True if the client must be shutted down */
    private boolean mStopEvent = false;
    
    /** The constructor */
    public Controller() {
        mQueue = new LinkedBlockingQueue<NetworkPacket>();
    }
    
    /** Set the view */
    public void setView(View v) {
        mView = v;
    }
    
    /** Main loop */
    public void run() {
        String[] connList = ConnectionFactory.getConnectionList();
        
        int conn = mView.askConnectionType(connList);
        mConn = ConnectionFactory.getConnection(conn);
        mConn.setOnReceiveListener(this);
        if(mConn == null)
            return;
        
        String host = mView.askHost();
        if(host == null || host.trim().length() == 0)
            return;
        
        mConn.setHost(host.trim());
        
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
        while(!mStopEvent) {
            try {
                NetworkPacket cmd = mQueue.poll(1, TimeUnit.SECONDS);
                if(cmd == null)
                    continue;
                String msg = "";
                switch(cmd.getOpcode()) {
                    case CMD_SC_TIME:
                        mView.updateLoginTime(Integer.parseInt((String) cmd.getArgs()[0]));
                        break;
                    case CMD_SC_STAT:
                        mView.updateLoginStat(Integer.parseInt((String) cmd.getArgs()[0]));
                        break;
                    case CMD_SC_USERFAIL:
                        msg = "Another player is using your name. Choose another one.";
                    case CMD_SC_CHOOSEUSER:
                        do {
                            user = mView.askUsername(msg.equals("")?"Choose a username":msg);
                        } while(user == null || user.trim().length() == 0);
                        mConn.sendPacket(new NetworkPacket(GameCommand.CMD_CS_USERNAME, user.trim()));
                        break;
                    case CMD_SC_USEROK:
                        break;
                    case CMD_SC_MAPFAIL:
                    case CMD_SC_CHOOSEMAP:
                        Integer chosenMap;
                        do {
                        chosenMap = mView.askMap((String[])cmd.getArgs());
                        } while(chosenMap == null);
                        
                        mConn.sendPacket(new NetworkPacket(GameCommand.CMD_CS_LOADMAP, chosenMap));
                        break;
                    case CMD_SC_MAPOK:
                        break;
                    case CMD_SC_RUN:
                        mView.switchToMainScreen((GameInfoContainer)(cmd.getArgs()[0]));
                        break;
                    case CMD_BYE:
                        break;

                };
            } catch(InterruptedException e) {
                mStopEvent = true;
                break;
            }
        }
        mView.close();
    }

    /** This method handles an incoming packet */
    @Override
    public void onReceive(NetworkPacket obj) {
        synchronized(mQueue) {
            mQueue.add(obj);
        }
    }

    /** This method handles a disconnect event */
    @Override
    public void onDisconnect() {
        mStopEvent = true;
    }
}