package it.polimi.ingsw.client.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 10, 2015
 */

class IO {
    private static final Logger LOG = Logger.getLogger(IO.class.getName());
    private static BufferedReader mReader = new BufferedReader(new InputStreamReader(System.in));

    public static void write(String line) {
        System.out.println(line);
    }

    public static int readInt() {
        int i = 0;
        while(true) {
            try {
                System.out.print("> ");
                System.out.flush();
                String m = mReader.readLine();
                i = Integer.valueOf(m);
                break;
            } catch(Exception e) {
                LOG.log(Level.FINEST, e.toString(), e);
                System.out.println("Invalid choice");
            }
        }
        return i;
    }

    public static String readString() {
        try {
            System.out.print("> ");
            System.out.flush();
            return mReader.readLine().trim();
        } catch (Exception e) {
            LOG.log(Level.FINEST, e.toString(), e);
        }
        return "";
    }

    public static int readRangeInt(int min, int max) {
        int i;
        while(true) {
            i = readInt();
            if(i >= min && i <= max)
                break;
            write("Out of range");
        }
        return i;
    }
}
