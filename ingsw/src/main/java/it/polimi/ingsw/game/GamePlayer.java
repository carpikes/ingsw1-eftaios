package it.polimi.ingsw.game;

import it.polimi.ingsw.game.card.SectorCard;
import it.polimi.ingsw.game.sector.Sector;


/**
 *
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 19, 2015
 */
public class GamePlayer {
    public static final int PLAYER_HUMAN = 0;
    public static final int PLAYER_ALIEN = 1;

    private Sector mCurrentSector;
    private GameLogic mLogic;
    private int mType;
    private boolean mHasJustAttacked;
    private boolean mWantToAttack;
    
    public GamePlayer(GameLogic logic, int type) {
        mLogic = logic;
        mType = type;
    }
    
    /** Get current sector
     * @return
     */
    public Sector getSector() {
        return mCurrentSector;
    }

    /** Get last chosen sector
     * @return
     */
    public Sector getLastChosenSector() {
        // TODO Auto-generated method stub
        return null;
    }

    /** Ask the player where he want to go
     * 
     * @param string
     */
    public void askSector(String string) {
        // TODO Auto-generated method stub
        // TODO if player could attack and onlySector == false, ask also if he wants to attack
        if(getType() == PLAYER_ALIEN) {
            // TODO Ask also if he wants to attack
        }
    }

    /**
     * 
     */
    public void resetLastChosenSector() {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param sectorCard
     */
    public void giveCard(SectorCard sectorCard) {
        // TODO: send the card to the player
        sectorCard.doAction(mLogic, this);
    }

    /** Has player attacked in this turn?
     * @return
     */
    public boolean hasJustAttacked() {
        // TODO Auto-generated method stub
        return false;
    }

    /** Do player want attack?
     * @return
     */
    public boolean wantAttack() {
        return mWantToAttack;
    }

    /** Human or alien?
     * @return
     */
    public int getType() {
        return mType;
    }

    /** Attack in your current sector
     * 
     */
    public void attack() {
        mHasJustAttacked = true;
        // TODO: code the attack
    }

    /**
     * 
     */
    public void resetAttackState() {
        mHasJustAttacked = false;
    }

}
