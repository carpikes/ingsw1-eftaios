package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.common.ViewCommand;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
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

    public static void write(char c) {
        System.out.print(c);
    }
    public static Integer readInt(boolean minusAvailable) {
        int i = 0;
        while(true) {
            try {
                System.out.print("> ");
                System.out.flush();
                String m = mReader.readLine();
                if(minusAvailable && m.equals("-"))
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

    public static Point askMapPos(boolean minusAvailable) {
        do {
            try {
                String s = IO.readString();

                if(s.equals("-") && minusAvailable)
                    return null;

                if(s.length() == 3 || (s.length() == 4  && s.charAt(1) == '0')) {

                    int x = (int)(Character.toLowerCase(s.charAt(0)) - 'a');
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
