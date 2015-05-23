package it.polimi.ingsw.game.player;

import it.polimi.ingsw.game.config.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoleFactory {
    public static List<Role> generateRoles(int numberOfPlayers) {
        List<Role> roles = new ArrayList<>();
        
        if(numberOfPlayers < Config.GAME_MIN_PLAYERS)
            throw new RuntimeException("Too few players (" + numberOfPlayers + ")");
        
        for(int i = 0; i < numberOfPlayers; i++)
            if(i < numberOfPlayers/2)
                roles.add(new Alien());
            else
                roles.add(new Human());
        
        Collections.shuffle(roles);
        return roles;
    }
}
