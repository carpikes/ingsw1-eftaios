package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;

/** Client launcher
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */
public class Main {	
    /** This class is not instantiable */
    private Main() { }
    
    /** Main
     * 
     * @param args
     */
    public static void main(String[] args) {
        GameController c = new GameController();
        View v = new CLIView(c);
        
        c.setView(v);
        c.run();
    }
}
