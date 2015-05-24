package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;
import it.polimi.ingsw.client.gui.GUIFrame;
import it.polimi.ingsw.client.gui.GUIView;
import it.polimi.ingsw.client.gui.MainFrame;
import it.polimi.ingsw.client.network.ConnectionFactory;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

public class Main {	
    private Main() { }
    
    public static void main(String[] args) {
        Controller c = new Controller();
        View v = new GUIView();
        c.setView(v);
        c.run();
    }
}
