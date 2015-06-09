package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.SectorException;
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
    
    private Sector[][] mBoard;
    private final Point mHumanStartingPoint, mAlienStartingPoint;
    private int mNumberOfHatches = 0;
    
    /** Private constructor
     *  
     * @param name Map name
     * @param board Board
     * @param human Human starting point
     * @param alien Alien starting point
     * @param numberOfHatches Number of hatches in this map
     */
    private GameMap( String name, Sector[][] board, Point human, Point alien, int numberOfHatches) {
        mBoard = board;
        mHumanStartingPoint = human;
        mAlienStartingPoint = alien;
        mNumberOfHatches = numberOfHatches;
    } 

    /** Load a map from file
     * 
     * @param file Filename
     * @return The map
     * @throws IOException File not found
     */
    public static GameMap createFromMapFile( File file ) throws IOException {
        Sector[][] sectors = new Sector[ROWS][COLUMNS];
        String title = null;
        Point human=null, alien=null;
        
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        Iterator<String> iterator = lines.iterator();
        
        int i = 0, j = 0;
        int numHatches = 0;
        title = iterator.next();
        while ( iterator.hasNext() ) {
            String[] currentLine = iterator.next().split(" ");
            for( j = 0; j < currentLine.length; ++j ) {
                sectors[i][j] = SectorBuilder.getSectorFor(Integer.parseInt(currentLine[j]));
                switch(sectors[i][j].getId()) {
                case SectorBuilder.ALIEN:
                    if(alien != null)
                        throw new SectorException("Aliens must have only one starting point");
                    alien = new Point(j,i);
                    break;
                case SectorBuilder.HUMAN:
                    if(human != null)
                        throw new SectorException("Humans must have only one starting point");
                    human = new Point(j,i);
                    break;
                case SectorBuilder.HATCH:
                    numHatches++;
                    break;
                }
            }
            ++i;
        }
        
        if(human == null || alien == null)
            throw new SectorException("Missing starting points");
        
        if( i != ROWS || j != COLUMNS )
            throw new SectorException("Missing sector");
        
        return new GameMap(title, sectors, human, alien, numHatches);
    }

    /** Return the sector type at the specified point
     * 
     * @param x X coords
     * @param y Y coords
     * @return The sector type
     */
    public Sector getSectorAt( int x, int y ) {
        return mBoard[y][x];
    }
    
    /** Return the sector type at the specified point
     * 
     * @param point The point
     * @return The sector type
     */
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

    /** Get the player starting point
     * 
     * @param isHuman True if the player is human
     * @return The starting point
     */
    public Point getStartingPoint(boolean isHuman) {
        if(isHuman)
            return mHumanStartingPoint;
        return mAlienStartingPoint;
    }

    /** Get the list of maps
     * 
     * @return The list of maps
     */
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

    /** Load a map using the specified ID
     * 
     * @param mapId The id
     * @return The map
     * @throws IOException File not found
     */
    public static GameMap createFromId(int mapId) throws IOException {
        if(!isValidMap(mapId))
            throw new RuntimeException("Invalid map id");
        
        return GameMap.createFromMapFile(new File(mapFiles[mapId]));
    }

    /** Return the cells within the specified range
     * @param currentPosition Starting position
     * @param maxMoves Range
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
    
    /** Return a string representing this coordinates
     * 
     * @param p Coordinate
     * @return The string
     */
    public String pointToString(Point p) {
        return pointToString(p.x,p.y);
    }
    
    /** Return a string representing this coordinates
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @return The string
     */
    public String pointToString(int x, int y) {
        return String.format("%c%03d", x + 'A', y + 1);
    }
    
    /** Return the remaining hatches
     * 
     * @return Remaining hatches
     */
    public int getRemainingHatches() {
        return mNumberOfHatches;
    }
    
    /** Use a hatch
     * 
     * @param pos Hatch coordinates
     */
    public void useHatch(Point pos) {
        if(!isWithinBounds(pos) || mNumberOfHatches <= 0 || mBoard[pos.y][pos.x].getId() != SectorBuilder.HATCH)
            throw new SectorException("Cannot use an hatch that does not exist.");
        
        mBoard[pos.y][pos.x] = SectorBuilder.getSectorFor(SectorBuilder.USED_HATCH);
        mNumberOfHatches--;
    }
}
