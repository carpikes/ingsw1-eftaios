package it.polimi.ingsw.client.gui;

import java.util.Map;

import javax.swing.JOptionPane;

import it.polimi.ingsw.client.View;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */
public class GUIView implements View {
    MainFrame mMainFrame;
    @Override
    public int askConnectionType(String[] params) {
        Object ret = JOptionPane.showInputDialog(null, "Choose a connection", "Connection type", 
                     JOptionPane.QUESTION_MESSAGE, null, params, params[0]);
        
        for(int i = 0; i<params.length;i++)
            if(params[i].equals(ret))
                return i;
        return -1;
    }

    @Override
    public String askHost() {
        return JOptionPane.showInputDialog("Type the host");
    }

    @Override
    public String askUsername(String message) {
        return JOptionPane.showInputDialog(message.length() == 0 ? "Type a username" : message);
    }

    @Override
    public int askMap(String[] mapList) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void run() {
        mMainFrame = new MainFrame();
    }

    @Override
    public void showError(String string) {
        JOptionPane.showMessageDialog(null, string, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#updateLoginTime(int)
     */
    @Override
    public void updateLoginTime(int i) {
        mMainFrame.setRemainingTime(i);
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#updateLoginStat(int)
     */
    @Override
    public void updateLoginStat(int i) {
        mMainFrame.setPlayers(i);
    }

}
