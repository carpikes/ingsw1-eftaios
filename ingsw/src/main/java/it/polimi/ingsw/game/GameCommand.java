package it.polimi.ingsw.game;

/**
 * A list of all commands that can be sent all over the network. There are three types of command:
 * - CMD_SC_* : from server to one client
 * - CMD_CS_* : from one client to server
 * - CMD_INFO : from server to all clients
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 17, 2015
 */
public enum GameCommand {
    /** LOGIN COMMANDS */
    CMD_INVALID,
    CMD_PING,
    CMD_BYE,
    CMD_SC_FULL,
    CMD_SC_STAT,
    CMD_SC_TIME,
    CMD_SC_CHOOSEUSER,
    CMD_CS_USERNAME,
    CMD_SC_USEROK,
    CMD_SC_USERFAIL,
    CMD_SC_RUN,
    CMD_SC_CHOOSEMAP,
    CMD_CS_LOADMAP,
    CMD_SC_MAPOK,
    CMD_SC_MAPFAIL,
   
    /** GAME COMMANDS TO SERVER */
    CMD_CS_ATTACK,
    CMD_CS_DISCARD_OBJ_CARD,
    CMD_CS_USE_OBJ_CARD,
    CMD_CS_SET_POSITION,
    CMD_CS_MOVE,
    CMD_CS_DRAW_DANGEROUS_CARD,
    CMD_CS_NOISE_IN_ANY_SECTOR_POSITION,
    CMD_CS_NOT_MY_TURN,
    CMD_CS_AWAKE,
    
    /** GAME COMMANDS TO *A SINGLE* CLIENT */
    CMD_SC_UPDATE_LOCAL_INFO,
    CMD_SC_START_TURN,
    CMD_SC_MOVE_DONE,
    CMD_SC_DANGEROUS_CARD_DRAWN,
    CMD_SC_OBJECT_CARD_OBTAINED,
    CMD_SC_DISCARD_OBJECT_CARD,
    CMD_SC_ADRENALINE_WRONG_STATE,
    CMD_SC_CANNOT_USE_OBJ_CARD,
    CMD_SC_MOVE_INVALID,
    CMD_SC_END_OF_TURN,
    CMD_SC_LOSE,
    CMD_SC_WIN,
    
    /** INFO TO *ALL* PLAYERS */
    INFO_HAS_MOVED,
    INFO_USED_HATCH,
    INFO_WINNER,
    INFO_LOSER, 
    INFO_KILLED_PEOPLE, 
    INFO_START_TURN, 
    INFO_OBJ_CARD_USED,
    INFO_NOISE,
    INFO_SILENCE, 
    INFO_GOT_A_NEW_OBJ_CARD,
    INFO_END_GAME,
    INFO_SPOTLIGHT
}
