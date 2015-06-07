package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameCommand;
import it.polimi.ingsw.game.network.GameOpcode;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.GameViewCommand;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
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
    private LinkedBlockingQueue<GameCommand> mQueue;
    
    /** True if the client must be shut down */
    private boolean mStopEvent = false;
    
    private GameStartInfo mGameInfo = null;
    
    private Integer mMyTurn = null;
    private String mMyUsername = null;
    
    private int mCurTurn = 0;
    
    /** The constructor */
    public GameController() {
        mQueue = new LinkedBlockingQueue<GameCommand>();
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
        
        while(!mStopEvent) {
            try {
                GameCommand cmd = mQueue.poll(1, TimeUnit.SECONDS);
                if(cmd == null)
                    continue;
                
                processCommand(cmd);
            } catch(InterruptedException e) {
                mStopEvent = true;
                break;
            }
        }
        mView.close();
    }

	private void processCommand(GameCommand cmd) {
        String msg = "";
        Object obj = null;
        String curUser = null;
        
        if(mGameInfo != null && mGameInfo.getPlayersList() != null)
        	curUser = mGameInfo.getPlayersList()[mCurTurn].getUsername();
        
        if(cmd.getArgs().length > 0)
        	obj = cmd.getArgs()[0];
        
        switch(cmd.getOpcode()) {
            case CMD_SC_TIME:
        		if(obj != null && obj instanceof Integer)
        			mView.updateLoginTime((Integer) obj);
                break;
            case CMD_SC_STAT:
            	if(obj != null && obj instanceof Integer)
            		mView.updateLoginStat((Integer) obj);
                break;
            case CMD_SC_USERFAIL:
                msg = "Another player is using your name. Choose another one.";
            case CMD_SC_CHOOSEUSER:
                do {
                    mMyUsername = mView.askUsername(msg.equals("")?"Choose a username":msg);
                    if(mMyUsername != null)
                        mMyUsername = mMyUsername.trim();
                } while(mMyUsername == null || mMyUsername.length() == 0);
                mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_USERNAME, mMyUsername));
                break;
            case CMD_SC_USEROK:
            	mView.showInfo(null, "Username accepted. Waiting for other players...");
                break;
            case CMD_SC_MAPFAIL:
            case CMD_SC_CHOOSEMAP:
                Integer chosenMap;
                do {
                    chosenMap = mView.askMap((String[])cmd.getArgs());
                } while(chosenMap == null);
                
                mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_LOADMAP, chosenMap));
                break;
            case CMD_SC_MAPOK:
                break;
            case CMD_SC_RUN:
            	if (obj != null && obj instanceof GameStartInfo) {
            		mGameInfo = (GameStartInfo) cmd.getArgs()[0];
            		for(int i = 0; i < mGameInfo.getPlayersList().length; i++) {
            		    if(mGameInfo.getPlayersList()[i].getUsername().equals(mMyUsername)) {
            		        mMyTurn = i;
            		        break;
            		    }
            		}
            		mView.switchToMainScreen(mGameInfo);
            	} else
            		throw new RuntimeException("Can't get game infos!");
                break;
            case CMD_BYE:
                mStopEvent = true;
                break;
            case CMD_SC_AVAILABLE_COMMANDS:
            	if(obj != null && obj instanceof ArrayList<?>) {
            		ArrayList<?> tmp = (ArrayList<?>) obj;
            		if(tmp.size() > 0 && tmp.get(0) instanceof GameViewCommand)
            			mView.enqueueCommand((ArrayList<GameViewCommand>) tmp);
            	}
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

                break;
            case CMD_SC_UPDATE_LOCAL_INFO:
                break;
            case CMD_SC_WIN:
                break;
                
            case INFO_END_GAME:
            	mView.showInfo(null, "Game is over. Good bye!");
            	stop();
                break;
            case INFO_GOT_A_NEW_OBJ_CARD:
            	mView.showInfo(curUser, "Draw new object card!");
                break;
            case INFO_HAS_MOVED:
            	mView.showInfo(curUser, "Player has moved");
                break;
            case INFO_PLAYER_ATTACKED:
            	if(cmd.getArgs().length == 2 && obj instanceof Point && cmd.getArgs()[1] instanceof ArrayList<?>) {
            		Point p = (Point) obj;
            		ArrayList<Integer> killedList = (ArrayList<Integer>) cmd.getArgs()[1];
            		mView.showInfo(curUser, "Player just attacked in sector " + mGameInfo.getMap().pointToString(p));
            		
            		if(killedList == null || killedList.size() == 0)
            			mView.showInfo(curUser, "Nobody has been killed");
            		else
            			for(Integer i : killedList)
            				mView.showInfo(curUser, mGameInfo.getPlayersList()[i].getUsername() + " has been killed");
            	}
                break;
            case INFO_LOSER:
            	mView.showInfo(curUser, "Game over.");
                break;
            case INFO_NOISE:
            	if(obj != null && obj instanceof Point) {
            		Point p = (Point) obj;
            		
            		mView.showNoiseInSector(curUser, p);
            	}
                break;
            case INFO_OBJ_CARD_USED:
                if(cmd.getArgs().length == 2 && cmd.getArgs()[1] instanceof String) {
                    String name = (String) cmd.getArgs()[1];
                    mView.showInfo(curUser, "Used the object card '" + name + "'");
                } else
                    mView.showInfo(curUser, "Used a weird object card without a valid name");
                
                break;
            case INFO_SILENCE:
                mView.showInfo(curUser, "Silence. Not implemented yet"); // TODO here
                break;
            case INFO_SPOTLIGHT:
                mView.showInfo(curUser, "Spotlight. Not implemented yet"); // TODO here
                break;
            case INFO_START_TURN:
                if(obj != null && obj instanceof Integer) {
                    mCurTurn = (Integer) obj;
                    if(mCurTurn == mMyTurn)
                        mView.onMyTurn();
                    else
                        mView.onOtherTurn(mGameInfo.getPlayersList()[mCurTurn].getUsername());
                }
                break;
            case INFO_USED_HATCH:
                break;
            case INFO_WINNER:
                break;
            default:
                break;
        }
    }

    /** This method handles an incoming packet */
    @Override
    public void onReceive(GameCommand obj) {
        synchronized(mQueue) {
            mQueue.add(obj);
        }
    }

    /** This method handles a disconnect event */
    @Override
    public void onDisconnect() {
        stop();
    }
    
    public boolean isRunning() {
        return !mStopEvent;
    }

    public void stop() {
        mStopEvent = true;
    }

    public GameMap getMap() {
        return mGameInfo.getMap();
    }

    public void onMapPositionChosen(Point mCurHexCoords) {
        mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, mCurHexCoords));
    }

	public void drawDangerousCard() {
		mConn.sendPacket(GameOpcode.CMD_CS_DRAW_DANGEROUS_CARD);
	}

	public void endTurn() {
		mConn.sendPacket(GameOpcode.CMD_CS_END_TURN);
	}

	public void attack() {
		mConn.sendPacket(GameOpcode.CMD_CS_ATTACK);
	}

    /**
     * @param choice
     */
    public void sendChosenObjectCard(int choice) {
        mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD, choice));
    }
    
    public void sendDiscardObjectCard(int choice) {
        mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_DISCARD_OBJECT_CARD, choice));
    }
}
