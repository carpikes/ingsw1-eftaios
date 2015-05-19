package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameLogic;
import it.polimi.ingsw.game.GamePlayer;

public class AlienSector extends Sector {

    @Override
    public int getId() {
        return Sectors.ALIEN;
    }

    @Override
    public boolean doSectorAction(GameLogic logic,GamePlayer player) {
        throw new SectorException("You can't go there. Why am I running?");
    }

}
