package it.polimi.ingsw.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Choose between client or server
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class PackageMain {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(PackageMain.class.getName());

    /** Private constructor */
    private PackageMain() {
        /** Unused */
    }
    
    /** Main
     * 
     * @param args args
     */
    public static void main(String[] args) {
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What would you like to launch?\n1)Client\n2)Server\n");
        do {
            try {
                String line = buf.readLine();
                if(line == null)
                    return;
            
                Integer i = Integer.parseInt(line);
                if(i == 1) {
                    it.polimi.ingsw.client.Main.main(args);
                    return;
                } else if(i == 2) {
                    it.polimi.ingsw.server.Main.main(args);
                    return;
                }
            } catch(Exception e) {
                LOG.log(Level.FINEST, e.toString(), e);
            }
            System.out.println("Invalid choice");
        } while(true);
    }
}
