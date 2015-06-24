package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.common.ViewCommand;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Helper class for IO operations on CLI
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since  May 10, 2015
 */
class IO {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(IO.class.getName());

    /** Input Reader */
    private static BufferedReader mReader = new BufferedReader(new InputStreamReader(System.in));

    /** Private constructor */
    private IO() {
        /** Unused */
    }

    /** Write a message in the console
     *
     * @param line The message to be printed
     */
    public static void write(String line) {
        System.out.println(line);
    }

    /** Write a char in the console
     *
     * @param c The character to be printed
     */
    public static void write(char c) {
        System.out.print(c);
    }

    /** Read a integer value
     *
     * @param minusAvailable True if you can go back in this menu
     * @return The vlaue read
     */
    public static Integer readInt(boolean minusAvailable) {
        int i = 0;
        while(true) {
            try {
                System.out.print("> ");
                System.out.flush();
                String m = mReader.readLine();

                /** console shut down */
                if(m == null)
                    System.exit(0); 

                if(minusAvailable && "-".equals(m))
                    return null;

                i = Integer.valueOf(m);
                break;
            } catch(Exception e) {
                LOG.log(Level.FINEST, e.toString(), e);
                System.out.println("Invalid choice");
            }
        }
        return i;
    }

    /** Read a string from the console
     *
     * @return The string
     */
    public static String readString() {
        try {
            System.out.print("> ");
            System.out.flush();
            String s = mReader.readLine();
            if(s == null)
                System.exit(0); // console shut down
            return s.trim();
        } catch (Exception e) {
            LOG.log(Level.FINEST, e.toString(), e);
        }
        return "";
    }

    /** Read an integer in a range
     *
     * @param min The minimum value in the range
     * @param max The maximum value in the range
     * @param minusAvailable True if you can go back in this menu
     * @return The number read
     */
    public static Integer readRangeInt(int min, int max, boolean minusAvailable) {
        Integer i;
        while(true) {
            i = readInt(minusAvailable);
            if(i == null) {
                if(minusAvailable)
                    return null;
            } else if(i >= min && i <= max)
                break;
            write("Out of range");
        }
        return i;
    }


    /** Ask for a value in a list of strings
     *
     * @param list The list of strings
     * @param minusAvailable True if you can go back in this menu
     * @return The index of the element you chose
     */
    public static Integer askInAList(String[] list, boolean minusAvailable) {
        for(int i = 0; i<list.length;i++)
            IO.write((i+1) + ") " + list[i]);
        do {
            Integer c = readRangeInt(1, list.length, minusAvailable);
            if(c == null) {
                if(minusAvailable)
                    return null;
            } else if(c >= 1)
                return c-1;
        } while(true);
    }

    /** Ask for a value in a list of View Commands
     *
     * @param list The list of commands
     * @param minusAvailable True if you can go back in this menu
     * @return The index of the element you chose
     */
    public static Integer askInAList(List<ViewCommand> list, boolean minusAvailable) {
        for(int i = 0; i<list.size();i++)
            IO.write((i+1) + ") " + list.get(i).getOpcode().toString());
        do {
            Integer c = readRangeInt(1, list.size(), minusAvailable);
            if(c == null) {
                if(minusAvailable)
                    return null;
            } else if(c >= 1)
                return c-1;
        } while(true);
    }

    /** Ask for a position in the map
     *
     * @param minusAvailable True if you can go back in this menu
     * @return The chosen position
     */
    public static Point askMapPos(boolean minusAvailable) {
        do {
            try {
                String s = IO.readString();

                if("-".equals(s) && minusAvailable)
                    return null;

                /** parse string */
                if(s.length() == 3 || (s.length() == 4  && s.charAt(1) == '0')) {
                    int x = Character.toLowerCase(s.charAt(0)) - 'a';
                    int y = Integer.parseInt(s.substring(s.length()-2))-1; 
                    if(x >= 0 && x <= GameMap.COLUMNS && y >=0 && y <= GameMap.ROWS)
                        return new Point(x,y);
                }

                IO.write("Invalid position");
            } catch(Exception e) {
                IO.write("Invalid position");
                LOG.log(Level.FINEST, "", e);
            }
        } while(true);
    }
}
