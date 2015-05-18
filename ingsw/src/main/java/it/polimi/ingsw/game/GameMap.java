package it.polimi.ingsw.game;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.Sectors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameMap {
    private static final Logger log = Logger.getLogger( GameMap.class.getName() );
    
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

	public static GameMap createFromMapFile( File file ) {
	    Sector[][] sectors = new Sector[ROWS][COLUMNS];
	    String title = null;
	    
	    try {
	        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
	        Iterator<String> iterator = lines.iterator();
	        
	        int i = 0, j = 0;
	        title = iterator.next();
	        while ( iterator.hasNext() ) {
	            String[] currentLine = iterator.next().split(" ");
	            for( j = 0; j < currentLine.length; ++j ) {
	                sectors[i][j] = Sectors.getSectorFor(Integer.parseInt(currentLine[j]));
	            }
	            ++i;
	        }
		} catch (IOException e) { 
			log.log(Level.SEVERE, "Cannot read map file: " + e);
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException | SectorException | NumberFormatException e) {
		    log.log(Level.SEVERE, "File is not well formatted: " + e);
		    System.exit(1);
        }
		
		return new GameMap(title, sectors);
	}
	
	public static GameMap generate() {
		// TODO: advanced functionality to be implemented
		return new GameMap(null, null);
	}
	
	public Sector getSectorAt( int i, int j ) {
	    return board[i][j];
	}
}
