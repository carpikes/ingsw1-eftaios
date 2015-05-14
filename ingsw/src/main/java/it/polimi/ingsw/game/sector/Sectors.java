package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.exception.SectorException;

public class Sectors {
	// Parameterized factory method
	public static Sector getSectorFor(int id) throws SectorException  {
		switch(id) {
			case ALIEN: 			return new AlienSector();
			case DANGEROUS: 		return new DangerousSector();
			case NOT_DANGEROUS:		return new NotDangerousSector();
			case HATCH:				return new HatchSector();
			case HUMAN:				return new HumanSector();
			case NOT_VALID:			return new NotValidSector();
			default:				throw new SectorException("Illegal sector code.");
		}
	}
	
	public static final int NOT_VALID = 0;
	public static final int HATCH = 2;
	public static final int NOT_DANGEROUS = 1;
	public static final int DANGEROUS = 3;
	public static final int ALIEN = 8;
	public static final int HUMAN = 9;

}
