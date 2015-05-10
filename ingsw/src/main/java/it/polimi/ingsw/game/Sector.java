package it.polimi.ingsw.game;


public abstract class Sector {
	// image component stuff
	
	private boolean crossable;
	
	// Parameterized factory method
	public static Sector getSectorFor(int id) throws Exception  {
		switch(id) {
		case SectorData.ALIEN: 			return new AlienSector();
		case SectorData.DANGEROUS: 		return new DangerousSector();
		case SectorData.NOT_DANGEROUS:	return new NotDangerousSector();
		case SectorData.HATCH:			return new HatchSector();
		case SectorData.HUMAN:			return new HumanSector();
		case SectorData.NOT_VALID:		return new NotValidSector();
		default:						throw new Exception("Illegal sector code.");
		}
	}
	
	
}
