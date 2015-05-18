package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 10, 2015
 */

public class Main {	
    
    public static void main(String[] args) {
        Controller c = new Controller();
        View v = new CLIView();
        c.setView(v);
        c.run();
    }

}
