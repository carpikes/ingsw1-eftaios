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
import it.polimi.ingsw.game.network.GameInfoContainer;
import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public class CLIView implements View {
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

    public CLIView() {
        CLIView.banner();
    }

    @Override
    public void run() {
    }

    @Override
    public int askConnectionType(String[] params) {
        IO.write("Which connection do you want to use?");
        
        for(int i=0;i<params.length;i++)
            IO.write((i+1) + ") " + params[i]);
        
        return IO.readRangeInt(1, params.length)-1;
    }

    @Override
    public String askUsername(String message) {
        String name;
        
        IO.write(message);
        name = IO.readString().trim();
        return name;
    }

    @Override
    public int askMap(String[] mapList) {
        IO.write("Choose a map:");
        for(int i=0;i<mapList.length;i++)
            IO.write(String.format("%d) %s", i+1, mapList[i]));
        
        int choice = IO.readRangeInt(1, mapList.length)-1;
        return choice;
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
     * @see it.polimi.ingsw.client.View#switchToMainScreen(it.polimi.ingsw.game.network.GameInfoContainer)
     */
    @Override
    public void switchToMainScreen(GameInfoContainer container) {
        // TODO Auto-generated method stub
        
    }
}
