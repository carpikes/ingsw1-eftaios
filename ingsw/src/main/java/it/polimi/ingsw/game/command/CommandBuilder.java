/**
 * 
 */
package it.polimi.ingsw.game.command;

import it.polimi.ingsw.exception.NetworkPacketNotValidException;
import it.polimi.ingsw.game.card.ObjectCard;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

import java.awt.Point;

/**
 * @author Michele
 * @since 23 May 2015
 */
public class CommandBuilder {

    /**
     * @param opcode
     * @return
     */
    public static Command getCommandFromNetworkPacket(NetworkPacket packet) throws ClassCastException {
        switch( packet.getOpcode() ) {
            case GameCommands.CMD_CS_ATTACK:            return new AttackCommand( (Point) packet.getArgs()[0] );
            case GameCommands.CMD_CS_DISCARD_OBJ_CARD:  return new DiscardObjectCard( (int) packet.getArgs()[0] );
            case GameCommands.CMD_CS_USE_OBJ_CARD:      return new UseObjectCardCommand( (ObjectCard) packet.getArgs()[0] );
            case GameCommands.CMD_CS_SET_POSITION:      return new AttackCommand( (Point) packet.getArgs()[0] );
            default:                                    throw new NetworkPacketNotValidException("Network packet not valid. Discarded.");                  
        }
    }
    
}
