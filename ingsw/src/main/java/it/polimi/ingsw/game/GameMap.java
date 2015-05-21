package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class GameMap {
    private static final Logger LOG = Logger.getLogger( GameMap.class.getName() );
    
	// including not crossable sectors
	public static final int ROWS = 14;
	public static final int COLUMNS = 23;
	
	private Sector[][] board;
	private String name;
	
	// You can only construct a new map either from a .map file or by random generation 
	private GameMap( String name, Sector[][] board ) {
	    this.name = name;
		this.board = board;
	} 

	public static GameMap createFromMapFile( File file ) throws IOException {
	    Sector[][] sectors = new Sector[ROWS][COLUMNS];
	    String title = null;
	    
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        Iterator<String> iterator = lines.iterator();
        
        int i = 0, j = 0;
        title = iterator.next();
        while ( iterator.hasNext() ) {
            String[] currentLine = iterator.next().split(" ");
            for( j = 0; j < currentLine.length; ++j ) {
                sectors[i][j] = SectorBuilder.getSectorFor(Integer.parseInt(currentLine[j]));
            }
            ++i;
        }
        
        if( i != ROWS || j != COLUMNS )
            throw new SectorException("Missing sector");
		
		return new GameMap(title, sectors);
	}
	
	public static GameMap generate() {
		// TODO: advanced functionality to be implemented
		return new GameMap(null, null);
	}
	
	public Sector getSectorAt( int i, int j ) {
	    return board[i][j];
	}
	
	public Sector getSectorAt( Point point ) {
	    return getSectorAt( point.x, point.y );
	}

    /** Check if given point is inside the map
     * @param destination
     * @return
     */
    public boolean isWithinBounds(Point p) {
        return ( p.x >= 0 && p.y >= 0 && p.x < ROWS && p.y < COLUMNS ); 
    }

    /** Calculate how many hexagons are there between A and B
     * @param source Where you start 
     * @param destination Where you end in
     * @return 
     */
    public int distance(Point currentPosition, Point destination) {
        // TODO: IMPLEMENTAMI!
        return 1;
    }
}
