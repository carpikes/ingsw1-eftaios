package it.polimi.ingsw.game;

import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.Sectors;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameMap {
	// including not crossable sectors
	public static final int WIDTH = 23;
	public static final int HEIGHT = 14;
	
	private Sector[][] board;
	private String name;
	
	// You can only construct a new map either from a .map file or by random generation 
	private GameMap( String _name, Sector[][] _board ) {
		name = _name;
		board = _board;
	} 
	
	public static GameMap createFromMapFile( Path file ) {
		Charset charset = Charset.forName("UTF-8");
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
			// read title
		    String mapTitle = reader.readLine();
		    
		    String line = null;
		    int row = 0;
		    
		    String[] currentSectorLine;
		    Sector[][] sectors = new Sector[WIDTH][HEIGHT];
		    
		    while ((line = reader.readLine()) != null) {
		        // check for no. of rows
		    	++row;
		        if( row > GameMap.WIDTH )
		        	throw new ArrayIndexOutOfBoundsException("Map file not valid: too many lines.");
		        
		        // get array of sectors in row rowCounter
		    	currentSectorLine = line.split(",");
		    	
		        // check for no. of columns
		    	if( currentSectorLine.length != GameMap.HEIGHT )
		    		throw new ArrayIndexOutOfBoundsException("Map file not valid: too many columns.");
		    	
		    	// create Sector according to value read 
		    	for( int col = 0; col < currentSectorLine.length; ++col ) {
		    		int idSector = Integer.parseInt(currentSectorLine[col]);
		    		sectors[row][col] = Sectors.getSectorFor(idSector);
		    	}
		    }
		    
		    if( row < GameMap.WIDTH )
		    	throw new ArrayIndexOutOfBoundsException("Map file not valid: some lines are missing.");
		    
		    return new GameMap( mapTitle, sectors );  
		} catch (Exception e) { 
			return new GameMap(null, null);
		}
	}
	
	public static GameMap generate() {
		// TODO: advanced functionality to be implemented
		return new GameMap(null, null);
	}
}
