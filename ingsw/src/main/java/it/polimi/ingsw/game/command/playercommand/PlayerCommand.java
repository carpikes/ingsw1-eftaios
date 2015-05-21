/**
 * 
 */
package it.polimi.ingsw.game.command.playercommand;

import it.polimi.ingsw.game.GamePlayer;
import it.polimi.ingsw.game.command.Command;

/**
 * @author Michele
 * @since 21 May 2015
 */
public interface PlayerCommand extends Command {
    void execute( GamePlayer player );
}
