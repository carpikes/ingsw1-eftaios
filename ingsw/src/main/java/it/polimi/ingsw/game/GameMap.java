package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.player.Role;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String[] mapFiles = {
        "maps/fermi.txt",
        "maps/galilei.txt",
        "maps/galvani.txt",
        "maps/debug.txt"
    };
    
	// including not crossable sectors
	public static final int ROWS = 14;
	public static final int COLUMNS = 23;
	
	private Sector[][] board;
	private final Point humanStartingPoint, alienStartingPoint;
	
	// You can only construct a new map either from a .map file or by random generation 
	private GameMap( String name, Sector[][] board, Point human, Point alien) {
		this.board = board;
		this.humanStartingPoint = human;
		this.alienStartingPoint = alien;
	} 

	public static GameMap createFromMapFile( File file ) throws IOException {
	    Sector[][] sectors = new Sector[ROWS][COLUMNS];
	    String title = null;
	    Point human=null, alien=null;
	    
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        Iterator<String> iterator = lines.iterator();
        
        int i = 0, j = 0;
        title = iterator.next();
        while ( iterator.hasNext() ) {
            String[] currentLine = iterator.next().split(" ");
            for( j = 0; j < currentLine.length; ++j ) {
                sectors[i][j] = SectorBuilder.getSectorFor(Integer.parseInt(currentLine[j]));
                if(sectors[i][j].getId() == SectorBuilder.ALIEN)
                    alien = new Point(j,i);
                else if(sectors[i][j].getId() == SectorBuilder.HUMAN)
                    human = new Point(j,i);
            }
            ++i;
        }
        
        if(human == null || alien == null)
            throw new SectorException("Missing starting points");
        
        if( i != ROWS || j != COLUMNS )
            throw new SectorException("Missing sector");
		
		return new GameMap(title, sectors, human, alien);
	}
	
	public Sector getSectorAt( int x, int y ) {
	    return board[y][x];
	}
	
	public Sector getSectorAt( Point point ) {
	    return getSectorAt( point.x, point.y );
	}

    /** Check if given point is inside the map
     * @param destination
     * @return
     */
    public boolean isWithinBounds(Point p) {
        return ( p.x >= 0 && p.y >= 0 && p.x < COLUMNS && p.y < ROWS ); 
    }

    public Point getStartingPoint(boolean isHuman) {
        if(isHuman)
            return humanStartingPoint;
        return alienStartingPoint;
    }

    public static String[] getListOfMaps() {
        return mapFiles;
    }

    /** Check if the given mapId is valid
     * 
     * @param mapId Chosen map id
     * @return True if is valid
     */
    public static boolean isValidMap(Integer mapId) {
        // Generate map
        if(mapId == -1)
            return true;
        
        if(mapId >= 0 && mapId < mapFiles.length)
            return true;
        return false;
    }

    public static GameMap createFromId(int mapId) throws IOException {
        if(!isValidMap(mapId))
            throw new RuntimeException("Invalid map id");
        
        return GameMap.createFromMapFile(new File(mapFiles[mapId]));
    }

    /**
     * @param point 
     * @param usedHatch
     */
    public void setType(Point point, int type) {
        if( isWithinBounds(point) ) {
            board[point.y][point.x] = SectorBuilder.getSectorFor(type);
        }
    }

    /**
     * @param currentPosition
     * @param maxMoves
     * @return
     */
    public Set<Point> getCellsWithMaxDistance(Point currentPosition,
            int maxMoves, boolean isHuman) {
        Set<Point> sectors = new HashSet< >();
        Queue<Point> frontier = new LinkedList<Point>();
        
        Point currentPoint;
        Point delimiter = null;
        
        frontier.add( currentPosition );
        frontier.add( delimiter );
        
        while( maxMoves > 0 ) {
        	
        	do {
        		currentPoint = frontier.poll();
        		
        		if( currentPoint != null ) {
        		    ArrayList<Point> neighbors = getNeighbourAccessibleSectors( currentPoint, isHuman );
        			
        			frontier.addAll( neighbors );
        			frontier.add( delimiter );
        			
        			sectors.add( currentPoint );
        			sectors.addAll(neighbors);
        		}
        	} while( currentPoint != null );
        	
        	--maxMoves;
        }
        
        sectors.remove( currentPosition );
        return sectors;
    }

    /**
     * Get neighbour cells starting from the given position. Note that only dangerous, not dangerous and hatch sectors
     * are given. Remember that in a mxn matrix, these (x) are the sectors accessible from O is the column is even:
     * - x -
     * x O x
     * x x x
     * otherwise, if the column is odd:
     * x x x
     * x O x
     * - x -
     * @param currentPosition The starting sector
     * @return A list of all neighbours
     */
    public ArrayList<Point> getNeighbourAccessibleSectors( Point currentPosition, boolean isHuman ) {
    	// get x and y for simplicity's sake
    	int x = currentPosition.x;
    	int y = currentPosition.y;
    	
    	ArrayList<Point> sectors = new ArrayList< >();

    	for( int i = -1; i <= 1; ++i ) {
    		for( int j = -1; j <= 1; ++j ) {
    		    
                // exclude the - sectors
    		    boolean isValid = (i == 0 && j != 0);
    		    if(currentPosition.x % 2 == 0) 
    		        isValid |= (i == 1 && j == 0) || (i == -1);
    		    else
    		        isValid |= (i == -1 && j == 0) || (i == 1);
    		    
    			if( isValid ) {
    				Point p = new Point(x+j, y+i);
    				
    				if( this.isWithinBounds( p ) ){
    				    Sector currentSector = this.getSectorAt(p);
    				    if( currentSector.isCrossable() && !(currentSector.getId() == SectorBuilder.HATCH && !isHuman) )
    				        sectors.add(p);
    				}
    			}
    		}
    	}
    	
    	return sectors;
    }
    
    public String pointToString(Point p) {
    	return pointToString(p.x,p.y);
    }
    
    public String pointToString(int x, int y) {
    	return String.format("%c%03d", x + 'A', y + 1);
    }
}
