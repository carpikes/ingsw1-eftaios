/**
 * 
 */
package it.polimi.ingsw.game.command;

import it.polimi.ingsw.game.GameState;
import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.game.player.GamePlayer;
import it.polimi.ingsw.game.player.Human;
import it.polimi.ingsw.game.player.PlayerState;
import it.polimi.ingsw.game.player.Role;

/**
 * @author Michele
 * @since 23 May 2015
 */
public class DrawDangerousCardCommand implements Command {
    
    ObjectCard objectCard;
    
    public DrawDangerousCardCommand(ObjectCard objectCard) {
        this.objectCard = objectCard;
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#isValid(it.polimi.ingsw.game.GameState)
     */
    @Override
    public boolean isValid(GameState gameState) {
        GamePlayer player = gameState.getCurrentPlayer();
        PlayerState playerState = player.getCurrentState();
        
        return ( playerState == PlayerState.USING_OBJECT_CARD && player.isHuman() ); 
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.game.command.Command#execute(it.polimi.ingsw.game.GameState)
     */
    @Override
    public void execute(GameState gameState) {
        objectCard.useCard( gameState );
    }

}
