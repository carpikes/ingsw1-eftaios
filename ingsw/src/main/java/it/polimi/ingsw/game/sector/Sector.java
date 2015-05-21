package it.polimi.ingsw.game.sector;


public class Sector {
	
    private int id;
    
    // other properties: maybe we can refactor and create a SectorProperties class
    private int crossable;
    //...
    
    public Sector( int id ) {
        this.id = id;
    }
	
	public int getId() { return id; }
}
