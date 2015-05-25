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
            case GameCommands.CMD_CS_ATTACK:            return null;
            default:                                    throw new NetworkPacketNotValidException("Network packet not valid. Discarded.");                  
        }
    }
    
}
