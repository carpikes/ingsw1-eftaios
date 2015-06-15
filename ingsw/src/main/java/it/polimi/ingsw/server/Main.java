package it.polimi.ingsw.server;

/** Server Main
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 8, 2015
 */
public class Main {

    /** Private constructor */
    private Main() { }

    /** Server entry point */
    public static void main(String[] args) {
        Server.getInstance().runServer(false);
    }

}
