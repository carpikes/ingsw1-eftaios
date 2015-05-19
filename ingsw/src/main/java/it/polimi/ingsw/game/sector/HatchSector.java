package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.game.GameLogic;
import it.polimi.ingsw.game.GamePlayer;

public class HatchSector extends Sector {
    
    @Override
    public int getId() {
        return Sectors.HATCH;
    }

    @Override
    public boolean doSectorAction(GameLogic logic,GamePlayer player) {
        // TODO Auto-generated method stub
        // TODO take the hatch
        return true;
    }
}
