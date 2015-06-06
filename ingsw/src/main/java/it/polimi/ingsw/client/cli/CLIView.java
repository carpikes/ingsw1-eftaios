package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.GameViewCommand;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public class CLIView extends View {
    private static final Logger LOG = Logger.getLogger(CLIView.class.getName());

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

	private GameMap mMap;
	private final GameController mController;
	private GameStartInfo mContainer;

    public CLIView(GameController c) {
        super(c);
        mController = c;
        CLIView.banner();
    }

    @Override
    public void run() {
    }

    @Override
    public int askConnectionType(String[] params) {
        IO.write("Which connection do you want to use?");
        return IO.askInAList(params);
    }

    @Override
    public String askUsername(String message) {
        String name;
        
        IO.write(message);
        name = IO.readString().trim();
        return name;
    }

    @Override
    public Integer askMap(String[] mapList) {
        IO.write("Choose a map:");
        return IO.askInAList(mapList);
    }

    @Override
    public String askHost() {
        IO.write("Type the hostname");
        return IO.readString();
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#showError(java.lang.String)
     */
    @Override
    public void showError(String string) {
        IO.write("ERROR: " + string);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#updateLoginTime(int)
     */
    @Override
    public void updateLoginTime(int i) {
        IO.write("Remaining time: " + i);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#updateLoginStat(int)
     */
    @Override
    public void updateLoginStat(int i) {
        IO.write("Players online: " + i);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#switchToMainScreen(it.polimi.ingsw.game.network.GameStartInfo)
     */
    @Override
    public void switchToMainScreen(GameStartInfo container) {
    	mContainer = container;
    	mMap = container.getMap();
    	
    	CLIMapRenderer.renderMap(mMap, null, null);
    	IO.write("Game is started! Good luck!");
    	IO.write("Your role is: " + (container.isHuman()?"HUMAN":"ALIEN"));
    	IO.write(String.format("%d players in game:", container.getPlayersList().length));
    	for(EnemyInfo e : container.getPlayersList())
    		IO.write("-> " + e.getUsername());
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void handleCommand(ArrayList<GameViewCommand> cmd) {
    	GameViewCommand c;
    	if(cmd.size() > 1) {
    		IO.write("What do you want to do now?");
            int choice = IO.askInAList(cmd);
            c = cmd.get(choice);
    	} else if(cmd.size() == 1) {
    		c = cmd.get(0);
    	} else
    		return;
    	
        switch(c.getOpcode()) {
	        case CMD_ENABLEMAPVIEW:
	        	Point newPos = null;
	            Point curPos = (Point) c.getArgs()[0];
	            int maxMoves = (int) c.getArgs()[1];
	            Set<Point> enabledCells = mController.getMap().getCellsWithMaxDistance(curPos, maxMoves);
	            
	            CLIMapRenderer.renderMap(mMap, curPos, enabledCells);
	    		IO.write("Choose a position on the map");
	            do {
	            	newPos = IO.askMapPos();
	            	if( (enabledCells == null && mMap.isWithinBounds(newPos)) || (enabledCells != null && enabledCells.contains(newPos)))
	            		break;
	            	IO.write("Invalid position");
	            } while(true);
	            mController.onMapPositionChosen(newPos);
	            break;
	        case CMD_CHOOSEOBJECTCARD:
	            break;
			case CMD_ATTACK:
				break;
			case CMD_DISCARDOBJECTCARD:
				break;
			case CMD_DRAWDANGEROUSCARD:
				mController.drawDangerousCard();
				break;
			case CMD_ENDTURN:
				mController.endTurn();
				break;
        }
    }

	@Override
	public void showInfo(String user, String message) {
		if(user != null)
			IO.write("["+ user + "] " + message);
		else
			IO.write("--INFO-- " + message);
	}

	@Override
	public void showNoiseInSector(String user, Point p) {
		showInfo(user, "NOISE IN SECTOR " + mMap.pointToString(p));
	}
}
