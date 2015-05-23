package it.polimi.ingsw.game.card;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.command.Command;

public class Card {
	private String name;
	private Command command;
	boolean usable;
	
    public Card(String name, Command command, boolean usable) {
        this.name = name;
        this.command = command;
        this.usable = usable;
    }
    
    public void useCard( GameState gameState ) {
        command.execute( gameState );
    }
}
