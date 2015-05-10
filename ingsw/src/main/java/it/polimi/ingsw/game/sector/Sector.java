package it.polimi.ingsw.game.sector;


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
	
	class SectorData {
		public static final int NOT_VALID = 0;
		public static final int HATCH = 2;
		public static final int NOT_DANGEROUS = 1;
		public static final int DANGEROUS = 3;
		public static final int ALIEN = 8;
		public static final int HUMAN = 9;

	}
	
}
