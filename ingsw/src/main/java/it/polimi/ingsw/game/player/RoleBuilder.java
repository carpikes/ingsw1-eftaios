package it.polimi.ingsw.game.player;

import it.polimi.ingsw.exception.TooFewPlayersException;
import it.polimi.ingsw.game.config.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for creating the roles for the player in a game
 * @author Michele
 * @since 3 Jun 2015
 */
public class RoleBuilder {
    
    private RoleBuilder() { }
    
    /**
     * Generate a list of roles, half humans and half aliens. 
     * @param numberOfPlayers The number of roles it has to create
     * @return The list of roles
     */
    public static List<Role> generateRoles(int numberOfPlayers, boolean randomizeRoles) {
        List<Role> roles = new ArrayList<>();
        
        if(numberOfPlayers < Config.GAME_MIN_PLAYERS)
            throw new TooFewPlayersException("Too few players (" + numberOfPlayers + ")");
        
        for(int i = 0; i < numberOfPlayers; i++)
            if(i % 2 == 0)
                roles.add(new Alien());
            else
                roles.add(new Human());
        
        if(randomizeRoles)
            Collections.shuffle(roles);
        return roles;
    }
}
