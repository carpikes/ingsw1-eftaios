package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.network.GameStartInfo;
import it.polimi.ingsw.game.network.GameViewCommand;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */
public class GUIView extends View {
    GUIFrame mMainFrame;
    
    public GUIView(GameController controller) {
        super(controller);
        mMainFrame = new GUIFrame(controller);
    }
    
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
    public Integer askView( String[] viewList ) {
        Object ret = JOptionPane.showInputDialog(null, "Choose a view", "", 
                JOptionPane.QUESTION_MESSAGE, null, viewList, viewList[0]);
   
        if(ret == null || !(ret instanceof String))
            return null;
        
        for(int i = 0; i<viewList.length;i++)
            if(viewList[i].equals(ret))
                return i;
        
        return null; 
    }

    @Override
    public Integer askMap(String[] mapList) {

        Object ret = JOptionPane.showInputDialog(null, "Choose a map", "", 
                JOptionPane.QUESTION_MESSAGE, null, mapList, mapList[0]);
   
        if(ret == null || !(ret instanceof String))
            return null;
        
        for(int i = 0; i<mapList.length;i++)
            if(mapList[i].equals(ret))
                return i;
        
        return null; 
    }

    @Override
    public void run() {
        mMainFrame.setVisible(true);
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

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#switchToMainScreen(it.polimi.ingsw.game.network.GameInfoContainer)
     */
    @Override
    public void switchToMainScreen(GameStartInfo container) {
        GameMap map = container.getMap();
        mMainFrame.setStartInfo( container );
        mMainFrame.switchToMap(map,map.getStartingPoint(container.isHuman()));
        
        mMainFrame.validate();
        mMainFrame.repaint();
    }

    @Override
    public void close() {
        if(mMainFrame != null) {
            mMainFrame.setVisible(false);
        }
    }

    @Override
    protected void handleCommand(ArrayList<GameViewCommand> cmd) {
        for(GameViewCommand c : cmd) {
            switch(c.getOpcode()) {
            case CMD_CHOOSEOBJECTCARD:
                break;
            case CMD_ENABLEMAPVIEW:
                Point curPos = (Point) c.getArgs()[0];
                int maxMoves = (int) c.getArgs()[1];
                Set<Point> enabledCells = mController.getMap().getCellsWithMaxDistance(curPos, maxMoves);
                mMainFrame.enableMapCells(enabledCells);
                //Point pos = askMapPosition(enabledCells);
                //sendPacket(new GameCommand(GameOpcode.CMD_CS_CHOSEN_MAP_POSITION, pos));

                break;
            case CMD_ATTACK:
                
                break;
            case CMD_DISCARDOBJECTCARD:
                
                break;
            case CMD_DRAWDANGEROUSCARD:
                
                break;
            case CMD_ENDTURN:
                
                break;

            default:
                break;
            
            }
        }
    }

    /** WARNING user can be null!!! */
	@Override
	public void showInfo(String user, String message) {
		mMainFrame.showInfo( user, message );
	}

	/** WARNING user can be null!!! */
	@Override
	public void showNoiseInSector(String user, Point p) {
		// TODO Auto-generated method stub
		
	}

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#onMyTurn()
     */
    @Override
    public void onMyTurn() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see it.polimi.ingsw.client.View#onOtherTurn(java.lang.String)
     */
    @Override
    public void onOtherTurn(String username) {
        // TODO Auto-generated method stub
        
    }
}
