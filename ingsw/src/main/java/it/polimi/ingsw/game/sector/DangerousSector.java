package it.polimi.ingsw.game.sector;

import it.polimi.ingsw.game.GameLogic;
import it.polimi.ingsw.game.GamePlayer;
import it.polimi.ingsw.game.card.SectorCard;

public class DangerousSector extends Sector {
    @Override
    public int getId() {
        return Sectors.DANGEROUS;
    }

    @Override
    public boolean doSectorAction(GameLogic logic,GamePlayer player) {
        if(!player.hasJustAttacked()) {
            SectorCard s = new SectorCard();
            s.doAction(logic, player);
            player.giveCard(s);
        }
        return false;
    }
}
