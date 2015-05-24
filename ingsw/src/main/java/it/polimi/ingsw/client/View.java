package it.polimi.ingsw.client;

import it.polimi.ingsw.game.network.GameInfoContainer;

import java.util.Map;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */
public interface View {
    public int askConnectionType(String[] params);
    public String askHost();
    public String askUsername(String message);
    public int askMap(String[] mapList);
    
    public void run();

    public void showError(String string);
    public void updateLoginTime(int i);
    public void updateLoginStat(int i);
    public void switchToMainScreen(GameInfoContainer container);
}
