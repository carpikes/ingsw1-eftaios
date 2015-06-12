package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;
import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.common.CoreOpcode;
import it.polimi.ingsw.game.common.PlayerInfo;
import it.polimi.ingsw.game.common.GameCommand;
import it.polimi.ingsw.game.common.GameOpcode;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.InfoOpcode;
import it.polimi.ingsw.game.common.Opcode;
import it.polimi.ingsw.game.common.ViewCommand;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Client controller
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public class GameController implements OnReceiveListener {
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    /** The view */
    private final View mView;

    /** The connection */
    private Connection mConn;

    /** List of incoming packets/events */
    private final LinkedBlockingQueue<GameCommand> mCommandQueue;
    private final LinkedBlockingQueue<List<ViewCommand>> mViewQueue;

    /** True if the client must be shut down */
    private boolean mStopEvent = false;

    private GameInfo mGameInfo = null;

    private Integer mMyTurn = null;
    private String mMyUsername = null;

    private int mCurPlayerId = 0;
    private boolean hostDone = false;

    /** The constructor */
    public GameController(String[] args) {

        mView = determineView(args);
        mCommandQueue = new LinkedBlockingQueue<>();
        mViewQueue = new LinkedBlockingQueue<>();

        createViewCommandQueueThread();
    }

    /**
     * 
     */
    private void createViewCommandQueueThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(isRunning()) {
                        List<ViewCommand> cmd = mViewQueue.poll(100, TimeUnit.MILLISECONDS);
                        if(cmd != null) 
                            mView.handleCommand(cmd);
                    }
                } catch(InterruptedException e) {
                    LOG.log(Level.FINEST, e.toString(), e);
                }


            }
        }).start();
    }


    /**
     * @param args
     * @param tempView
     * @return
     */
    private View determineView(String[] args) {
        View tempView = null;

        String[] viewList = ViewFactory.getViewList();
        if(args.length == 1) {
            for(int i = 0; i < viewList.length; i++) {
                String v = viewList[i];
                if(v.equalsIgnoreCase(args[0])) {
                    tempView = ViewFactory.getView( this, i );
                    break;
                }
            }
        }

        if(tempView == null) {
            tempView = new CLIView(this);

            int viewCode = tempView.askView( viewList );
            tempView = ViewFactory.getView(this, viewCode);
        }
        return tempView;
    }


    protected void enqueueViewCommand(List<ViewCommand> arrayList) {
        try {
            mViewQueue.put(arrayList);
        } catch(InterruptedException e) {
            LOG.log(Level.FINEST, e.toString(), e);
        }
    }

    /** Main loop */
    public void run() {
        if(mView == null)
            return;

        mView.startup();
        
        if( !setupConnection() )
            mView.close();
        
        do {
            try {
                setupHost();
                
                while(!mConn.isOnline()) 
                    Thread.sleep(100);
                
                hostDone = true;
            } catch (IOException e) {
                mView.showError("Unable to connecto to the specified host");
                LOG.log(Level.FINER, e.toString(), e);
            } catch (InterruptedException e) {
                LOG.log(Level.FINER, e.toString(), e);
            }    
        } while( !hostDone  );

        mView.run();

        try {
            while(!mStopEvent) {
                GameCommand cmd = mCommandQueue.poll(1, TimeUnit.SECONDS);
                if(cmd == null)
                    continue;

                processCommand(cmd);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        stop();
        mView.close();
        return;
    }
    
    private void setupHost() throws IOException {
        String host;
        do {
            host = mView.askHost();
        } while( host == null || host.trim().length() == 0);

        mConn.setHost(host.trim());
        mConn.connect();
    }

    private boolean setupConnection() {
        String[] connList = ConnectionFactory.getConnectionList();
        int conn = mView.askConnectionType(connList);
        mConn = ConnectionFactory.getConnection(conn);
        if(mConn == null)
            return false;

        mConn.setOnReceiveListener(this);
        
        return true;
    }

    private void processCommand(GameCommand cmd) {
        String curUser = null;

        if(mGameInfo != null && mGameInfo.getPlayersList() != null)
            curUser = mGameInfo.getPlayersList()[mCurPlayerId].getUsername();

        Opcode op = cmd.getOpcode();

        if(op instanceof CoreOpcode) {
            parseCoreCmd(cmd);
        } else if(op instanceof GameOpcode) {
            parseGameCmd(cmd);
        } else if(op instanceof InfoOpcode) {
            parseInfoCmd(cmd, curUser);
        }
    }


    /* These commmands below are sent while game is running
     * and the map is loaded. 
     * 
     * @param cmd The command
     * @param curUser Current username
     */
    @SuppressWarnings("unchecked")
    private void parseGameCmd(GameCommand cmd) {
        Object obj = null;
        GameOpcode gop = (GameOpcode) cmd.getOpcode();

        if(cmd.getArgs().length > 0)
            obj = cmd.getArgs()[0];

        switch(gop) {
            case CMD_SC_AVAILABLE_COMMANDS:
                if(obj != null && obj instanceof ArrayList<?>) {
                    List<?> tmp = (List<?>) obj;
                    if(!tmp.isEmpty() && tmp.get(0) instanceof ViewCommand)
                        enqueueViewCommand((List<ViewCommand>) tmp);
                }
                break;
            case CMD_SC_OBJECT_CARD_OBTAINED:
                if(obj != null && obj instanceof Integer) {
                    List<Integer> listOfCards = mGameInfo.getListOfCards();
                    listOfCards.add( (Integer)obj );
                    mView.notifyObjectCardListChange( listOfCards );
                }
                break;
            case CMD_SC_MOVE_INVALID:
                mView.showError("Invalid move");
                break;
                
            case CMD_SC_DROP_CARD:
                if(obj != null && obj instanceof Integer) {
                    try {
                        int index = (Integer)obj;
                        mGameInfo.getListOfCards().remove( index );
                        mView.notifyObjectCardListChange( mGameInfo.getListOfCards() );
                    } catch( IndexOutOfBoundsException e ) {
                        LOG.warning( "Trying to dropping a non-existent card" );
                    }
                }
                break;
            case CMD_SC_LOSE:
                mView.showInfo(null, "YOU'VE JUST LOST THE GAME. <3");
                break;
            case CMD_SC_WIN:
                mView.showInfo(null, "You won! Congrats!");
                break;
            default:
                break;
        }
    }

    /** Parse an InfoCommand
     * 
     * @param cmd The command
     * @param curUser Current username
     */
    @SuppressWarnings("unchecked")
    private void parseInfoCmd(GameCommand cmd, String curUser) {
        InfoOpcode op = (InfoOpcode) cmd.getOpcode();

        switch(op) {
            case INFO_END_GAME:
                if(cmd.getArgs().length == 2) {
                    Object obj = cmd.getArgs()[0];
                    Object obj2 = cmd.getArgs()[1];
                    if(cmd.getArgs()[0] != null && cmd.getArgs()[0] instanceof ArrayList<?> && obj2 != null && obj2 instanceof ArrayList<?>) {
                        List<Integer> winnerList = (List<Integer>) obj;
                        List<Integer> loserList = (List<Integer>) obj2;
                        mView.showEnding(winnerList, loserList);
                        stop();
                    }
                }
                break;
            case INFO_CHANGED_NUMBER_OF_CARDS:
                PlayerInfo playerInfo = mGameInfo.getPlayersList()[mCurPlayerId]; 

                if(cmd.getArgs()[0] != null && cmd.getArgs()[0] instanceof Integer) {
                    int count = (int)cmd.getArgs()[0];
                    // update count of cards for current player
                    playerInfo.setNumberOfCards( count );

                    // update info of current status for all players and show a message
                    mView.updatePlayerInfoDisplay( mCurPlayerId );

                    mView.showInfo(curUser, "has " + count + " cards");
                }

                break;
            case INFO_DISCARDED_OBJ_CARD:
                if(cmd.getArgs().length == 2 && cmd.getArgs()[1] instanceof String) {
                    String name = (String) cmd.getArgs()[1];
                    PlayerInfo e = mGameInfo.getPlayersList()[mCurPlayerId];
                    e.setNumberOfCards(e.getNumberOfCards()-1);
                    mView.showInfo(curUser, "Discarded " + name + " card");
                }
                break;
            case INFO_HAS_MOVED:
                mView.showInfo(curUser, "Player has moved");
                break;
            case INFO_PLAYER_ATTACKED:
                if(cmd.getArgs().length == 3 && cmd.getArgs()[0] instanceof Point && cmd.getArgs()[1] instanceof ArrayList<?>
                        && cmd.getArgs()[2] instanceof ArrayList<?>) {
                    Point p = (Point) cmd.getArgs()[0];

                    mView.showInfo(curUser, "Player just attacked in sector " + mGameInfo.getMap().pointToString(p));

                    List<Integer> killedList = (List<Integer>) cmd.getArgs()[1];
                    if(killedList == null || killedList.isEmpty())
                        mView.showInfo(curUser, "Nobody has been killed");
                    else
                        for(Integer i : killedList)
                            mView.showInfo(curUser, mGameInfo.getPlayersList()[i].getUsername() + " has been killed");

                    List<Integer> defendedList = (List<Integer>) cmd.getArgs()[2];
                    if(defendedList != null && !defendedList.isEmpty())
                        for(Integer i : defendedList)
                            mView.showInfo(curUser, mGameInfo.getPlayersList()[i].getUsername() + " has been attacked, but survived");
                        }
                break;
            case INFO_LOSER:
                mView.showInfo(curUser, "This player lost the game");
                break;
            case INFO_NOISE:
                if(cmd.getArgs().length == 1) {
                    Object obj = cmd.getArgs()[0];
                    if(obj != null && obj instanceof Point) {
                        Point p = (Point) obj;

                        mView.showNoiseInSector(curUser, p);
                    }
                }
                break;
            case INFO_OBJ_CARD_USED:
                if(cmd.getArgs().length == 2 && cmd.getArgs()[1] instanceof String) {
                    String name = (String) cmd.getArgs()[1];
                    mView.showInfo(curUser, "Used the object card '" + name + "'");
                }
                break;
            case INFO_SILENCE:
                mView.showInfo(curUser, "Extracted dangerous card: Silence.");
                break;
            case INFO_SPOTLIGHT:
                if(cmd.getArgs().length == 2 && cmd.getArgs()[0] instanceof Point && cmd.getArgs()[1] instanceof Point[]) {
                    Point chosenPoint = (Point) cmd.getArgs()[0];
                    Point[] playersFound = (Point []) cmd.getArgs()[1];
                    
                    mView.showInfo(curUser, "Used spotlight.");
                    mView.handleSpotlightResult(chosenPoint, playersFound);
                } else
                    mView.showInfo(curUser, "Used spotlight but I got an invalid packet");
                break;
            case INFO_START_TURN:
                if(cmd.getArgs().length == 1 && cmd.getArgs()[0] != null && cmd.getArgs()[0] instanceof Integer) {
                    mCurPlayerId = (Integer) cmd.getArgs()[0];
                    if(mCurPlayerId == mMyTurn)
                        mView.onMyTurn();
                    else
                        mView.onOtherTurn(mGameInfo.getPlayersList()[mCurPlayerId].getUsername());
                }
                break;
            case INFO_USED_HATCH:
                if(cmd.getArgs().length == 1 && cmd.getArgs()[0] != null && cmd.getArgs()[0] instanceof Point) {
                    mGameInfo.getMap().useHatch((Point) cmd.getArgs()[0]);
                    mView.showInfo(curUser, "Used a hatch");
                }
                break;
            case INFO_WINNER:
                mView.showInfo(curUser, "This player won the game!");
                break;
            case INFO_ALIEN_FULL:
                mView.showInfo(curUser, "This alien is full");
                break;
            default:
                break;
        }
    }

    /** Parse a CoreCommand
     * 
     * @param cmd The command
     */
    private void parseCoreCmd(GameCommand cmd) {
        String msg = "";
        CoreOpcode lop = (CoreOpcode) cmd.getOpcode();
        Object obj = null;

        if(cmd.getArgs().length > 0)
            obj = cmd.getArgs()[0];

        switch(lop) {
            case CMD_SC_TIME:
                if(obj != null && obj instanceof Integer)
                    mView.updateLoginTime((Integer) obj);
                break;
            case CMD_SC_STAT:
                if(obj != null && obj instanceof Integer)
                    mView.updateLoginStat((Integer) obj);
                break;
            case CMD_SC_USERNAMEFAIL:
                msg = "Invalid name or another player is using your name. Choose another one.";
            case CMD_SC_CHOOSEUSER:
                do {
                    mMyUsername = mView.askUsername("".equals(msg)?"Choose a username":msg);
                    if(mMyUsername != null)
                        mMyUsername = mMyUsername.trim();
                } while(mMyUsername == null || mMyUsername.length() == 0);
                mConn.sendPacket(new GameCommand(CoreOpcode.CMD_CS_USERNAME, mMyUsername));
                break;
            case CMD_SC_USERNAMEOK:
                mView.showInfo(null, "Username accepted. Waiting for other players...");
                break;
            case CMD_SC_MAPFAIL:
            case CMD_SC_CHOOSEMAP:
                Integer chosenMap;
                do {
                    chosenMap = mView.askMap((String[])cmd.getArgs());
                } while(chosenMap == null);

                mConn.sendPacket(new GameCommand(CoreOpcode.CMD_CS_LOADMAP, chosenMap));
                break;
            case CMD_SC_MAPOK:
                mView.showInfo(null, "Map successfully loaded");
                break;
            case CMD_SC_RUN:
                runGame(obj);
                break;
            case CMD_BYE:
                stop();
                break;
            case CMD_SC_FULL:
                mView.showError("Server is full. Try again later");
                stop();
                break;
            default:
                break;
        }
    }

    /** This method handles an incoming packet */
    @Override
    public void onReceive(GameCommand obj) {
        synchronized(mCommandQueue) {
            mCommandQueue.add(obj);
        }
    }

    /** This method handles a disconnect event */
    @Override
    public void onDisconnect() {
        mStopEvent = true;
    }

    public boolean isRunning() {
        return !mStopEvent;
    }

    public void stop() {
        mStopEvent = true;
        mConn.disconnect();
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

    private void runGame(Object obj) {
        if (obj != null && obj instanceof GameInfo) {
            mGameInfo = (GameInfo) obj;
            for(int i = 0; i < mGameInfo.getPlayersList().length; i++) {
                if(mGameInfo.getPlayersList()[i].getUsername().equals(mMyUsername)) {
                    mMyTurn = i;
                    break;
                }
            }
            mView.switchToMainScreen(mGameInfo);
        } else
            throw new RuntimeException("Can't get game infos!");
    }
}
