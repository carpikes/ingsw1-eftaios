package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.network.GameViewCommand;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    
    
    public static int askInAList(String[] list) {
        for(int i = 0; i<list.length;i++)
            IO.write((i+1) + ") " + list[i]);
        return readRangeInt(1, list.length) - 1;
    }
    
    public static int askInAList(ArrayList<GameViewCommand> list) {
        for(int i = 0; i<list.size();i++)
            IO.write((i+1) + ") " + list.get(i).getOpcode().toString());
        return readRangeInt(1, list.size()) - 1;
    }

	public static Point askMapPos() {
		do {
			String s = IO.readString();
			if(s.length() == 3 || (s.length() == 4  && s.charAt(1) == '0')) {
			
				int x = (int)(Character.toLowerCase(s.charAt(0)) - 'a');
				int y = Integer.parseInt(s.substring(s.length()-2))-1; 
				if(x >= 0 && x <= GameMap.COLUMNS && y >=0 && y <= GameMap.ROWS)
					return new Point(x,y);
			}
			
			IO.write("Invalid position");
		} while(true);
	}
}
