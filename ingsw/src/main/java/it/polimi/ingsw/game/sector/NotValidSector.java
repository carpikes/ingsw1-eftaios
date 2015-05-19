package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameLogic;
import it.polimi.ingsw.game.GamePlayer;

public class NotValidSector extends Sector {
    @Override
    public int getId() {
        return Sectors.NOT_VALID;
    }

    @Override
    public boolean doSectorAction(GameLogic logic,GamePlayer player) {
        throw new SectorException("You can't go there. Why am I running?");
    }
}
