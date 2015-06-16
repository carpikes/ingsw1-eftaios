package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.exception.CommandNotValidException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.PlayerInfo;
import it.polimi.ingsw.game.common.ViewCommand;

import java.awt.Point;
import java.util.List;
import java.util.Set;

/** CLI View
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 10, 2015
 */
public class CLIView extends View {
    
    /** Game map */
    private GameMap mMap;

    /** Controller */
    private final GameController mController;

    /** Game info container */
    private GameInfo mContainer;
    
    /** The constructor
     * @param c Game Controller
     */
    public CLIView(GameController c) {
        super(c);
        mController = c;
    }
    
    /** Print a fancy border */
    private static void printBorder() {
        IO.write("*******************************************************************************");
    }
    
    /** Draw the starting banner */
    private static void banner() {
        
        for(int i=0;i<2;i++)
            printBorder();
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
        for(int i=0;i<2;i++)
            printBorder();
        IO.write("");
    }

    /** Invoked on startup
     *
     * @see it.polimi.ingsw.client.View#startup()
     */
    @Override
    public void startup() {
        CLIView.banner();
    }

    /** Method not used
     *
     * @see it.polimi.ingsw.client.View#run()
     */
    @Override
    public void run() {
        /** unused */
    }

    /** Ask for a connection type
     *
     * @see it.polimi.ingsw.client.View#askConnectionType(java.lang.String[])
     */
    @Override
    public int askConnectionType(String[] params) {
        IO.write("Which connection do you want to use?");
        return IO.askInAList(params, false);
    }

    /** Ask for a username
     * 
     * @see it.polimi.ingsw.client.View#askUsername(java.lang.String)
     */
    @Override
    public String askUsername(String message) {
        String name;

        IO.write(message);
        name = IO.readString().trim();
        return name;
    }

    /** Ask for a map
     * 
     * @see it.polimi.ingsw.client.View#askMap(java.lang.String[])
     */
    @Override
    public Integer askMap(String[] mapList) {
        IO.write("Choose a map:");
        return IO.askInAList(mapList, false);
    }

    /** Ask for a host
     *
     * @see it.polimi.ingsw.client.View#askHost()
     */
    @Override
    public String askHost() {
        IO.write("Type the hostname");
        return IO.readString();
    }

    /** Ask for a view
     *
     * @see it.polimi.ingsw.client.View#askView(java.lang.String[])
     */
    @Override
    public Integer askView( String[] viewList ) {
        IO.write("Which view do you want to use?");
        return IO.askInAList(viewList, false);
    }

    /** Display an error
     *
     * @see it.polimi.ingsw.client.View#showError(java.lang.String)
     */
    @Override
    public void showError(String string) {
        IO.write("ERROR: " + string);
    }

    /** Update Login time on startup
     *
     * @see it.polimi.ingsw.client.View#updateLoginTime(int)
     */
    @Override
    public void updateLoginTime(int i) {
        IO.write("Remaining time: " + i);
    }

    /** Update login statistics about players on startup
     *
     * @see it.polimi.ingsw.client.View#updateLoginStat(int)
     */
    @Override
    public void updateLoginStat(int i) {
        IO.write("Players online: " + i);
    }

    /** Display map after login phase
     *
     * @see it.polimi.ingsw.client.View#switchToMainScreen(it.polimi.ingsw.game.common.GameInfo)
     */
    @Override
    public void switchToMainScreen(GameInfo container) {
        mContainer = container;
        mMap = container.getMap();

        CLIMapRenderer.renderMap(mMap, null, null);
        IO.write("Game is started! Good luck!");
        IO.write("Your role is: " + (container.isHuman()?"HUMAN":"ALIEN"));
        IO.write(String.format("%d players in game:", container.getPlayersList().length));
        for(PlayerInfo e : container.getPlayersList())
            IO.write("-> " + e.getUsername());
    }

    /** Handle a map view
     *
     * @param c ViewCommand
     * @param canGoBack True if user can go back
     * @return True if the command is handled successfully
     */
    private boolean handleEnableMapView(ViewCommand c, boolean canGoBack) {
        Point newPos = null;
        Point curPos = null;
        int maxMoves = 0;
        Set<Point> enabledCells = null;
        
        if(c.getArgs().length > 0) {
            if(c.getArgs()[0] instanceof Point)
                curPos = (Point) c.getArgs()[0];
            if(c.getArgs().length == 2)
                maxMoves = (int) c.getArgs()[1];
        }

        if(maxMoves != 0) 
            enabledCells = mController.getMap().getCellsWithMaxDistance(curPos, maxMoves, mContainer.isHuman());

        CLIMapRenderer.renderMap(mMap, curPos, enabledCells);
        IO.write("Choose a position on the map" + (canGoBack?" (or type - to go back)":""));
        do {
            newPos = IO.askMapPos(canGoBack);

            /** up in the menu */
            if(newPos == null)
                return false;

            if( (enabledCells == null && mMap.isWithinBounds(newPos)) || 
                (enabledCells != null && enabledCells.contains(newPos)))
                break;
            IO.write("Invalid position");
        } while(true);
        mController.onMapPositionChosen(newPos);
        return true;
    }

    /** Handle a ChooseObjectCard command
     *
     * @param c ViewCommand
     * @param canGoBack True if user can go back
     * @return True if the command is handled successfully
     */
    private boolean handleChooseObjectCard(ViewCommand c, boolean canGoBack) {
        if(c.getArgs().length > 0 && c.getArgs() instanceof String[]) {
            String[] objs = (String[]) c.getArgs();
            IO.write("Which card do you want to use?" + (canGoBack?" (or type - to go back)":""));
            Integer choice = IO.askInAList(objs, canGoBack);
            if(choice == null)
                return false;
            mController.sendChosenObjectCard(choice);
            return true;
        } else
            showError(""+c.getArgs().length);
        return false;
    }

    /** Handle a DiscardObjectCard Command
     *
     * @param c ViewCommand
     * @param canGoBack True if user can go back
     * @return True if the command is handled successfully
     */
    private boolean handleDiscardObjectCard(ViewCommand c, boolean canGoBack) {
        if(c.getArgs().length > 0 && c.getArgs() instanceof String[]) {
            String[] objs = (String[]) c.getArgs();
            IO.write("Which card do you want to discard?" + (canGoBack?" (or type - to go back)":""));
            Integer i = IO.askInAList(objs, canGoBack);

            if(i == null)
                return false;
            mController.sendDiscardObjectCard(i);
            return true;
        } else {
            showError(""+c.getArgs().length);
        }
        return false;
    }

    /** Handle a view command received from the game controller
     *
     * @see it.polimi.ingsw.client.View#handleCommand(java.util.List)
     */
    @Override
    protected void handleCommand(List<ViewCommand> cmd) {
        ViewCommand c;
        boolean loopMenu = true;

        while(loopMenu) {

            if(cmd.size() > 1) {
                IO.write("What do you want to do now?");
                int choice = IO.askInAList(cmd, false);
                c = cmd.get(choice);
            } else if(cmd.size() == 1) {
                loopMenu = false;
                c = cmd.get(0);
            } else
                return;

            switch(c.getOpcode()) {
                case CMD_ENABLEMAPVIEW:
                    /** loopMenu == false if this is the only choice */
                    if(handleEnableMapView(c, loopMenu)) 
                        loopMenu = false;
                    break;
                case CMD_CHOOSEOBJECTCARD:
                    if(handleChooseObjectCard(c, loopMenu))
                        loopMenu = false;
                    break;
                case CMD_ATTACK:
                    mController.attack();
                    loopMenu = false;
                    break;
                case CMD_DISCARDOBJECTCARD:
                    if(handleDiscardObjectCard(c, loopMenu))
                        loopMenu = false;
                    break;
                case CMD_DRAWDANGEROUSCARD:
                    mController.drawDangerousCard();
                    loopMenu = false;
                    break;
                case CMD_ENDTURN:
                    mController.endTurn();
                    loopMenu = false;
                    break;
                default:
                    throw new CommandNotValidException("Command not valid!");
            }
        }
    }

    /** Display an info on console
     *
     * @see it.polimi.ingsw.client.View#showInfo(java.lang.String, java.lang.String)
     */
    @Override
    public void showInfo(String user, String message) {
        if(user != null)
            IO.write("["+ user + "] " + message);
        else
            IO.write("--INFO-- " + message);
    }

    /** Display noise info
     *
     * @see it.polimi.ingsw.client.View#showNoiseInSector(java.lang.String, java.awt.Point)
     */
    @Override
    public void showNoiseInSector(String user, Point p) {
        showInfo(user, "NOISE IN SECTOR " + mMap.pointToString(p));
    }

    /** Display message on my turn start
     *
     * @see it.polimi.ingsw.client.View#onMyTurn()
     */
    @Override
    public void onMyTurn() {
        showInfo(null, "It's your turn!");
    }

    /** Display message when someone else's turn starts
     *
     * @see it.polimi.ingsw.client.View#onOtherTurn(java.lang.String)
     */
    @Override
    public void onOtherTurn(String username) {
        showInfo(null, "It's " + username + "'s turn!");
    }

    /** Show ending banner
     *
     * @see it.polimi.ingsw.client.View#showEnding(java.util.List, java.util.List)
     */
    @Override
    public void showEnding(List<Integer> winnerList, List<Integer> loserList) {
        IO.write("*******************");
        IO.write("**    THE END    **");
        IO.write("*******************\n");

        if(winnerList.isEmpty()) 
            IO.write("Nobody won this game.");
        else {
            IO.write("====  Winners  ====");
            for(Integer i : winnerList) {
                IO.write(" -> " + mContainer.getPlayersList()[i].getUsername());
            }
        }

        if(loserList.isEmpty()) {
            IO.write("Nobody lost this game.");
        } else {
            IO.write("====  Losers   ====");
            for(Integer i : loserList) {
                IO.write(" -> " + mContainer.getPlayersList()[i].getUsername());
            }
        }
        
        mController.stop();
        System.exit(0);
    }

    /** Display your object cards
     *
     * @see it.polimi.ingsw.client.View#notifyObjectCardListChange(java.util.List)
     */
    @Override
    public void notifyObjectCardListChange(List<Integer> listOfCards) {
        StringBuilder cards = new StringBuilder();

        cards.append("Your object cards: ");
        for(int i = 0;i < listOfCards.size(); i++) {
            cards.append( i == 0 ? "[ " : "| " );
            cards.append(ObjectCardBuilder.idToString(listOfCards.get(i)));
            cards.append(" ");
        }

        if(listOfCards.isEmpty())
            cards.append("[ YOU HAVE NO OBJECT CARDS ]");
        else 
            cards.append("]");

        IO.write(cards.toString());
    }

    /** Unused
     *
     * @see it.polimi.ingsw.client.View#updatePlayerInfoDisplay(int)
     */
    @Override
    public void updatePlayerInfoDisplay( int idPlayer ) {
        /** unused */
    }

    /** Display info about spotlight action
     *
     * @see it.polimi.ingsw.client.View#handleSpotlightResult(java.awt.Point, java.awt.Point[])
     */
    @Override
    public void handleSpotlightResult(Point chosenPoint, Point[] playersFound) {
        boolean spottedAtLeastOne = false;
        
        IO.write("***** SPOTLIGHT RESULTS (" + mContainer.getMap().pointToString(chosenPoint) + ") *****\n");
        for(int i = 0; i < playersFound.length; i++) {
            Point p = playersFound[i];
            if(p != null) {
                IO.write(" -> " + mContainer.getPlayersList()[i].getUsername() + " spotted in sector " + mContainer.getMap().pointToString(p));
                spottedAtLeastOne = true;
            }
        }
        
        if(!spottedAtLeastOne)
            IO.write("No players are in those sectors\n");
        IO.write("\n************************************\n");
    }

    /** Display info about attacks in a sector
     *
     * @see it.polimi.ingsw.client.View#handleAttack(java.lang.String, java.awt.Point)
     */
    @Override
    public void handleAttack(String user, Point p) {
        showInfo(user, "Player just attacked in sector " + mContainer.getMap().pointToString(p));
    }
}
