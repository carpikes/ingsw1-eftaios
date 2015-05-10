package it.polimi.ingsw.game.sector;

/*
 * TODO Find a cleaner solution (?)
 * Even though a enum would apparently have been a cleaner solution,
 * it would have been very problematic to get every ID at compile time
 * (needed for the parameterized factory method in Sector).
 */

public class SectorData {
	public static final int NOT_VALID = 0;
	public static final int NOT_DANGEROUS = 1;
	public static final int DANGEROUS = 2;
	public static final int ALIEN = 3;
	public static final int HUMAN = 4;
	public static final int HATCH = 5;
}
