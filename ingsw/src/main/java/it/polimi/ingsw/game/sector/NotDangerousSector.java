package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.game.GameLogic;
import it.polimi.ingsw.game.GamePlayer;

public class NotDangerousSector extends Sector {
    @Override
    public int getId() {
        return Sectors.NOT_DANGEROUS;
    }

    @Override
    public boolean doSectorAction(GameLogic logic,GamePlayer player) {
        // Nothing here
        return true;
    }
}
