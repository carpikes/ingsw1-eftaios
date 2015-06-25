package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;
import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.OnReceiveListener;
import it.polimi.ingsw.common.CoreOpcode;
import it.polimi.ingsw.common.GameCommand;
import it.polimi.ingsw.common.GameInfo;
import it.polimi.ingsw.common.GameOpcode;
import it.polimi.ingsw.common.InfoOpcode;
import it.polimi.ingsw.common.Opcode;
import it.polimi.ingsw.common.PlayerInfo;
import it.polimi.ingsw.common.ViewCommand;
import it.polimi.ingsw.exception.InvalidGameInfoException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.config.Config;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Client game controller. 
 * It is the supervisor of whatever happens on the client side.
 * It is the proxy for any communication between client and server. 
 * Whenever a server sends a message to the client, this class is responsible for
 * analyzing the command and delegate the effects to the underlying view.
 * Whenever a clients sends a message, it delegates the actual sending to the underlying connection
 * responsible for it.
 * 
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public class GameController implements OnReceiveListener {
    /** Logger */
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
    private boolean mHostDone = false;
    
    private String mArgsView, mArgsHost, mArgsConn;

    /** The constructor 
     *
     * @param args Main args
     */
    public GameController(String[] args) {
        loadParams(args);
        mView = determineView();
        mCommandQueue = new LinkedBlockingQueue<>();
        mViewQueue = new LinkedBlockingQueue<>();

        createViewCommandQueueThread();
    }

    /** Load parameters from cli
     * 
     * @param args Main args
     */
    private void loadParams(String[] args) {
        if(args.length > 3)
            return;
        if(args.length >= 1)
            mArgsView = args[0];
        if(args.length >= 2)
            mArgsConn = args[1];
        if(args.length >= 3)
            mArgsHost = args[2];
    }

    /** Create the thread responsible for getting new View Commands */
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

    /** Determine initial view. 
     * @param args The view list
     * @return The temporary, initial view
     */
    private View determineView() {
        View tempView = null;

        String[] viewList = ViewFactory.getViewList();
        if(mArgsView != null && mArgsView.length() > 0) {
            for(int i = 0; i < viewList.length; i++) {
                String v = viewList[i];
                if(v.equalsIgnoreCase(mArgsView)) {
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


    /** Add a list of view commands to be then processed by the view.
     * 
     * @param arrayList A list of Viewcommands to be added
     */
    protected void enqueueViewCommands(List<ViewCommand> arrayList) {
        try {
            mViewQueue.put(arrayList);
        } catch(InterruptedException e) {
            LOG.log(Level.FINEST, e.toString(), e);
        }
    }

    /** Main loop of the game */
    public void run() {
        if(mView == null)
            return;

        /** show statup with current view */
        mView.startup();
        
        /** handle connection */
        if(!setupConnection()) {
            System.exit(0);
            return;
        }
        
        /** handle host */
        do {
            try {
                setupHost();
                
                while(mConn != null && !mConn.isOnline()) 
                    Thread.sleep(100);
                
                mHostDone = true;
            } catch (IOException e) {
                mView.showError("Unable to connecto to the specified host");
                LOG.log(Level.FINER, e.toString(), e);
            } catch (InterruptedException e) {
                LOG.log(Level.FINER, e.toString(), e);
            }    
        } while( !mHostDone  );

        if(mStopEvent)
            return;
        
        /** starts the view */
        mView.run();

        /** keep getting commands till the end of the game */
        try {
            while(!mStopEvent) {
                GameCommand cmd = mCommandQueue.poll(1, TimeUnit.SECONDS);
                if(cmd == null)
                    continue;

                processCommand(cmd);
            }
        } catch (InterruptedException e) {
            LOG.log(Level.FINEST, e.toString(), e);
        }

        /** stops the game */
        stop();
    }
    
    /** Ask for a host and connect to it
     *
     * @throws IOException
     */
    private void setupHost() throws IOException {
        if(mArgsHost != null && mArgsHost.length() > 0) {
            mConn.setHost(mArgsHost);
            mArgsHost = null;
            mConn.connect();
        } else {
            String host;
            do {
                host = mView.askHost();
            } while( host != null && host.trim().length() == 0);
            
            if(host != null) {
                mConn.setHost(host.trim());
                mConn.connect();
            } else {
                System.exit(0);
            }
        }
    }

    /** Ask for a connection type
     *
     * @return Whether this choice was successful or not
     */
    private boolean setupConnection() {
        
        mConn = null;
        String[] connList = ConnectionFactory.getConnectionList();
        if(mArgsConn != null && mArgsConn.length() > 0) {
            for(int i = 0; i < connList.length; i++)
                if(connList[i].equalsIgnoreCase(mArgsConn)) {
                    mConn = ConnectionFactory.getConnection(i);
                    mArgsConn = null;
                    break;
                }
        }
        
        if(mConn == null) {
            int conn = mView.askConnectionType(connList);
            mConn = ConnectionFactory.getConnection(conn);
            if(mConn == null)
                return false;
        }
        
        mConn.setOnReceiveListener(this);
        
        return true;
    }

    /** Process a command by delegating the work to the core/game/info command parser
     *
     * @param cmd The game command to be parsed
     */
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


    /** Handle a game command (a command that is sent while the game is running)
     *
     * @param cmd The command to be processed
     */
    private void parseGameCmd(GameCommand cmd) {
        Object obj = null;
        GameOpcode gop = (GameOpcode) cmd.getOpcode();

        if(cmd.getArgs().length > 0)
            obj = cmd.getArgs()[0];

        switch(gop) {
            case CMD_SC_AVAILABLE_COMMANDS:
                handleAvailableCommands(obj);
                break;
            case CMD_SC_OBJECT_CARD_OBTAINED:
                handleObjectCardObtained(obj);
                break;
            case CMD_SC_MOVE_INVALID:
                mView.showError("Invalid move");
                break;
            case CMD_SC_UPDATE_POSITION:
                handleUpdatePosition(obj);
                break;
            case CMD_SC_DROP_CARD:
                handleDropCard(obj);
                break;
            case CMD_SC_LOSE:
                mView.showInfo(null, "You've just lost the game. Better luck next time!");
                break;
            case CMD_SC_WIN:
                mView.showInfo(null, "You won! Congrats!");
                break;
            default:
                break;
        }
    }

    /** Handle update position
     * 
     * @param obj The Point
     */
    private void handleUpdatePosition(Object obj) {
        if(obj != null && obj instanceof Point) {
            mView.setPosition( (Point) obj );
        }
    }

    /** Handle a drop card command
     *
     * @param obj Command parameter
     */
    private void handleDropCard(Object obj) {
        if(obj != null && obj instanceof Integer) {
            try {
                int index = (Integer)obj;
                mGameInfo.getListOfCards().remove( index );
                mView.notifyObjectCardListChange( mGameInfo.getListOfCards() );
            } catch( IndexOutOfBoundsException e ) {
                LOG.warning( "Trying to dropping a non-existent card" );
                LOG.log(Level.FINEST, e.toString(), e);
            }
        }
    }

    /** Handle an object card obtained command
     *
     * @param obj Command parameter
     */
    private void handleObjectCardObtained(Object obj) {
        if(obj != null && obj instanceof Integer) {
            List<Integer> listOfCards = mGameInfo.getListOfCards();
            listOfCards.add( (Integer)obj );
            mView.notifyObjectCardListChange( listOfCards );
        }
    }

    /** Called when you get all available commands during your current state
     *
     * @param obj Command parameter
     */
    @SuppressWarnings("unchecked")
    private void handleAvailableCommands(Object obj) {
        if(obj != null && obj instanceof ArrayList<?>) {
            List<?> tmp = (List<?>) obj;
            if(!tmp.isEmpty() && tmp.get(0) instanceof ViewCommand)
                enqueueViewCommands((List<ViewCommand>) tmp);
        }
    }

    /** Handle an info command (which is sent to all players)
     *
     * @param cmd The command to be processed
     * @param curUser The player that sent this message
     */
    private void parseInfoCmd(GameCommand cmd, String curUser) {
        InfoOpcode op = (InfoOpcode) cmd.getOpcode();

        switch(op) {
            case INFO_END_GAME:
                handleInfoEndGame(cmd);
                break;
            case INFO_CHANGED_NUMBER_OF_CARDS:
                handleInfoChangedNumberOfCards(cmd, curUser);
                break;
            case INFO_DISCARDED_OBJ_CARD:
                handleInfoDiscardedObjectCard(cmd, curUser);
                break;
            case INFO_HAS_MOVED:
                mView.showInfo(curUser, "Player has moved");
                break;
            case INFO_PLAYER_ATTACKED:
                handleInfoPlayerAttacked(cmd, curUser);
                break;
            case INFO_LOSER:
                mView.showInfo(curUser, "This player lost the game");
                break;
            case INFO_NOISE:
                handleInfoNoise(cmd, curUser);
                break;
            case INFO_OBJ_CARD_USED:
                handleInfoObjectCardUsed(cmd, curUser);
                break;
            case INFO_SILENCE:
                mView.showInfo(curUser, "Extracted dangerous card: Silence.");
                break;
            case INFO_SPOTLIGHT:
                handleInfoSpotlight(cmd, curUser);
                break;
            case INFO_START_TURN:
                handleInfoStartTurn(cmd);
                break;
            case INFO_USED_HATCH:
                handleInfoUsedHatch(cmd);
                break;
            case INFO_RED_HATCH:
                mView.showInfo(curUser, "Trying to use a broken hatch. Sorry for that.");
                break;
                
            case INFO_GREEN_HATCH:
                mView.showInfo(curUser, "Got a good hatch finally!");
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

    /** Handle a used hatch info
     *
     * @param cmd The command to be processed
     */
    private void handleInfoUsedHatch(GameCommand cmd) {
        if(cmd.getArgs().length == 1 && cmd.getArgs()[0] != null && cmd.getArgs()[0] instanceof Point) {
            mGameInfo.getMap().useHatch((Point) cmd.getArgs()[0]);
            mView.changeSectorToUsedHatch( (Point) cmd.getArgs()[0] );
        }
    }

    /** Handle an object card used info
     *
     * @param cmd The command to be processed
     * @param curUser The user that sent this message
     */
    private void handleInfoObjectCardUsed(GameCommand cmd, String curUser) {
        if(cmd.getArgs().length == 2 && cmd.getArgs()[1] instanceof String) {
            String name = (String) cmd.getArgs()[1];
            mView.showInfo(curUser, "Used the object card '" + name + "'");
        }
    }

    /** Handle a start turn info
     *
     * @param cmd The command to be processed
     */
    private void handleInfoStartTurn(GameCommand cmd) {
        if(cmd.getArgs().length == 2 && cmd.getArgs()[0] != null && cmd.getArgs()[0] instanceof Integer
                && cmd.getArgs()[1] != null && cmd.getArgs()[1] instanceof Integer) {
            mCurPlayerId = (Integer) cmd.getArgs()[0];
            int moveCounter = (Integer) cmd.getArgs()[1];
            if(mCurPlayerId == mMyTurn)
                mView.onMyTurn( moveCounter );
            else
                mView.onOtherTurn(mGameInfo.getPlayersList()[mCurPlayerId].getUsername());
        }
    }

    /** Handle a spotlight info
     *
     * @param cmd The command to be processed
     * @param curUser The user that sent this message
     */
    private void handleInfoSpotlight(GameCommand cmd, String curUser) {
        if(cmd.getArgs().length == 2 && cmd.getArgs()[0] instanceof Point && cmd.getArgs()[1] instanceof Point[]) {
            Point chosenPoint = (Point) cmd.getArgs()[0];
            Point[] playersFound = (Point []) cmd.getArgs()[1];
            
            mView.showInfo(curUser, "Used spotlight.");
            mView.handleSpotlightResult(chosenPoint, playersFound);
        } else
            mView.showInfo(curUser, "Used spotlight but I got an invalid packet");
    }

    /** Handle a noise info
     *
     * @param cmd The command to be processed
     * @param curUser The user that sent this message
     */
    private void handleInfoNoise(GameCommand cmd, String curUser) {
        if(cmd.getArgs().length == 1) {
            Object obj = cmd.getArgs()[0];
            if(obj != null && obj instanceof Point) {
                Point p = (Point) obj;

                mView.showNoiseInSector(curUser, p);
            }
        }
    }

    /** Handle a player attacked info
     *
     * @param cmd The command to be processed
     * @param curUser The user that sent this message
     */
    @SuppressWarnings("unchecked")
    private void handleInfoPlayerAttacked(GameCommand cmd, String curUser) {
        if(cmd.getArgs().length == 3 && cmd.getArgs()[0] instanceof Point && cmd.getArgs()[1] instanceof ArrayList<?>
                && cmd.getArgs()[2] instanceof ArrayList<?>) {
            Point p = (Point) cmd.getArgs()[0];

            mView.handleAttack(curUser, p);
            
            /** create killed people list */
            List<Integer> killedList = (List<Integer>) cmd.getArgs()[1];
            if(killedList == null || killedList.isEmpty())
                mView.showInfo(curUser, "Nobody has been killed");
            else
                for(Integer i : killedList)
                    mView.showInfo(curUser, mGameInfo.getPlayersList()[i].getUsername() + " has been killed");

            /** create defended people list */
            List<Integer> defendedList = (List<Integer>) cmd.getArgs()[2];
            if(defendedList != null && !defendedList.isEmpty())
                for(Integer i : defendedList)
                    mView.showInfo(curUser, mGameInfo.getPlayersList()[i].getUsername() + " has been attacked, but survived");
                }
    }

    /** Handle a discarded object card info
     *
     * @param cmd The command to be processed
     * @param curUser The user that sent this message
     */
    private void handleInfoDiscardedObjectCard(GameCommand cmd, String curUser) {
        if(cmd.getArgs().length == 2 && cmd.getArgs()[1] instanceof String) {
            String name = (String) cmd.getArgs()[1];
            PlayerInfo e = mGameInfo.getPlayersList()[mCurPlayerId];
            e.setNumberOfCards(e.getNumberOfCards()-1);
            mView.showInfo(curUser, "Discarded " + name + " card");
        }
    }

    /** Handle a changed number of cards info
     *
     * @param cmd The command to be processed
     * @param curUser The user that sent this message
     */
    private void handleInfoChangedNumberOfCards(GameCommand cmd, String curUser) {
        if(cmd.getArgs().length == 2 && cmd.getArgs()[0] != null && cmd.getArgs()[1] != null && cmd.getArgs()[0] instanceof Integer && cmd.getArgs()[1] instanceof Integer) {
            int id = (int)cmd.getArgs()[0];
            int count = (int)cmd.getArgs()[1];
            
            PlayerInfo playerInfo = mGameInfo.getPlayersList()[ id ]; 
            
            /** update count of cards for current player */
            playerInfo.setNumberOfCards( count );

            /** update info of current status for all players and show a message */
            mView.updatePlayerInfoDisplay( id );

            mView.showInfo(curUser, "has " + count + " cards");
        }
    }

    /** Handle an end game info
     *
     * @param cmd The command to be processed
     */
    @SuppressWarnings("unchecked")
    private void handleInfoEndGame(GameCommand cmd) {
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
    }

    /** Process a core command (the ones that are not directly related to the game)
     *
     * @param cmd The command to be processed
     */
    private void parseCoreCmd(GameCommand cmd) {
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
                handleChooseUser("Invalid name or another player is using your name. Choose another one.");
                break;
            case CMD_SC_CHOOSEUSER:
                handleChooseUser("");
                break;
            case CMD_SC_USERNAMEOK:
                mView.showInfo(null, "Username accepted. Waiting for other players...");
                break;
            case CMD_SC_MAPFAIL:
                handleChooseMap(cmd);
                break;
            case CMD_SC_CHOOSEMAP:
                handleChooseMap(cmd);
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

    /** Send to the server the chosen map
     *
     * @param cmd The command with all the maps available on the server
     */
    private void handleChooseMap(GameCommand cmd) {
        Integer chosenMap;
        do {
            chosenMap = mView.askMap((String[])cmd.getArgs());
        } while(chosenMap == null);

        mConn.sendPacket(new GameCommand(CoreOpcode.CMD_CS_LOADMAP, chosenMap));
    }

    /** Send to the server a username
     *
     * @param msg 
     */
    private void handleChooseUser(String msg) {
        do {
            mMyUsername = mView.askUsername("".equals(msg)?"Choose a username (Max " + Config.MAX_USERNAME_LENGTH + " chars)":msg);
            if(mMyUsername != null)
                mMyUsername = mMyUsername.trim();
        } while(mMyUsername == null || mMyUsername.length() == 0);
        mConn.sendPacket(new GameCommand(CoreOpcode.CMD_CS_USERNAME, mMyUsername));
    }

    /** This method handles an incoming packet
     *
     * @see it.polimi.ingsw.client.network.OnReceiveListener#onReceive(it.polimi.ingsw.common.GameCommand)
     */
    @Override
    public void onReceive(GameCommand obj) {
        synchronized(mCommandQueue) {
            mCommandQueue.add(obj);
        }
    }

    /** This method handles a disconnect event
     *
     * @see it.polimi.ingsw.client.network.OnReceiveListener#onDisconnect()
     */
    @Override
    public void onDisconnect() {
        if(!mStopEvent)
            stop();
    }

    /** Check if the game is still running
     *
     * @return True if still running
     */
    public boolean isRunning() {
        return !mStopEvent;
    }

    /** Stop the game */
    public void stop() {
        mStopEvent = true;
        if(mConn != null)
            mConn.disconnect();
        if(mView != null)
            mView.showEnding("Connection closed");
    }

    /** Return current loaded map
     *
     * @return The map
     */
    public GameMap getMap() {
        return mGameInfo.getMap();
    }

    /** Send the chosen map position
     *
     * @param mCurHexCoords The point on the map
     */
    public void onMapPositionChosen(Point mCurHexCoords) {
        mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, mCurHexCoords));
    }

    /** Send a draw dangerous card command */
    public void drawDangerousCard() {
        mConn.sendPacket(GameOpcode.CMD_CS_DRAW_DANGEROUS_CARD);
    }

    /** Send an end turn command */
    public void endTurn() {
        mConn.sendPacket(GameOpcode.CMD_CS_END_TURN);
    }

    /** Send an attack command */
    public void attack() {
        mConn.sendPacket(GameOpcode.CMD_CS_ATTACK);
    }

    /** Send a chosen card command
     *
     * @param choice The card you chose
     */
    public void sendChosenObjectCard(int choice) {
        mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_OBJECT_CARD, choice));
    }

    /** Send a discard object command
     *
     * @param choice The card to be discarded
     */
    public void sendDiscardObjectCard(int choice) {
        mConn.sendPacket(new GameCommand(GameOpcode.CMD_CS_DISCARD_OBJECT_CARD, choice));
    }

    /** Start the game
     *
     * @param obj Command with a gameInfo
     */
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
            throw new InvalidGameInfoException("Can't get game infos!");
    }
}
