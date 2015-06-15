package it.polimi.ingsw.client;

/** Client launcher
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 10, 2015
 */
public class Main {	
    /** This class is not instantiable */
    private Main() { }

    /** Main
     * 
     * @param args Args
     */
    public static void main(String[] args) {
        new GameController(args).run();
    }
}
