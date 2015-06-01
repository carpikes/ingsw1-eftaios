package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.network.GameInfoContainer;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/** Client controller
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public class GameController implements OnReceiveListener {
    /** The view */
    private View mView;
    
    /** The connection */
    private Connection mConn;
    
    /** List of incoming packets/events */
    private LinkedBlockingQueue<NetworkPacket> mQueue;
    
    /** True if the client must be shutted down */
    private boolean mStopEvent = false;
    
    private GameInfoContainer mGameInfo = null;
    
    /** The constructor */
    public GameController() {
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
        if(mConn == null)
            return;
        
        mConn.setOnReceiveListener(this);
        
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
                        mGameInfo = (GameInfoContainer) cmd.getArgs()[0];
                        mView.switchToMainScreen(mGameInfo);
                        break;
                    case CMD_BYE:
                        mStopEvent = true;
                        break;
                        
                    /* TODO These commmands below are sent while game is running
                     * and the map is loaded. Add a check into each state if the
                     * used variables are initialized. Or, instead, a giant try-catch
                     * might be useful.
                     */
                    case CMD_SC_ADRENALINE_WRONG_STATE:
                        break;
                    case CMD_SC_CANNOT_USE_OBJ_CARD:
                        break;
                    case CMD_SC_DANGEROUS_CARD_DRAWN:
                        break;
                    case CMD_SC_DISCARD_OBJECT_CARD:
                        break;
                    case CMD_SC_END_OF_TURN:
                        break;
                    case CMD_SC_FULL:
                        break;
                    case CMD_SC_LOSE:
                        break;
                    case CMD_SC_MOVE_DONE:
                        
                        break;
                    case CMD_SC_OBJECT_CARD_OBTAINED:
                        break;
                    case CMD_SC_MOVE_INVALID:
                    case CMD_SC_START_TURN:
                        Point curPos = (Point) cmd.getArgs()[0];
                        int maxMoves = (int) cmd.getArgs()[1];
                        Set<Point> enabledCells = mGameInfo.getMap().getCellsWithMaxDistance(curPos, maxMoves);
                        Point pos = mView.askMapPosition(enabledCells);
                        mConn.sendPacket(new NetworkPacket(GameCommand.CMD_CS_MOVE, pos));
                        break;
                    case CMD_SC_UPDATE_LOCAL_INFO:
                        break;
                    case CMD_SC_WIN:
                        break;
                        
                    case INFO_END_GAME:
                        break;
                    case INFO_GOT_A_NEW_OBJ_CARD:
                        break;
                    case INFO_HAS_MOVED:
                        break;
                    case INFO_KILLED_PEOPLE:
                        break;
                    case INFO_LOSER:
                        break;
                    case INFO_NOISE:
                        break;
                    case INFO_OBJ_CARD_USED:
                        break;
                    case INFO_SILENCE:
                        break;
                    case INFO_SPOTLIGHT:
                        break;
                    case INFO_START_TURN:
                        break;
                    case INFO_USED_HATCH:
                        break;
                    case INFO_WINNER:
                        break;
                    default:
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
