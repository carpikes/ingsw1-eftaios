/**
 * 
 */
package it.polimi.ingsw.game.command;

import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;

/**
 * @author Michele
 * @since 23 May 2015
 */
public class CommandBuilder {

    /**
     * @param opcode
     * @return
     */
    public static Command getCommandFromNetworkPacket(NetworkPacket packet) {
        switch( packet.getOpcode() ) {
        case GameCommands.CMD_CS_ATTACK:    
            
            
        }
        
        return null;
    }
    
}
