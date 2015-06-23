package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.ViewCommand;
import it.polimi.ingsw.game.common.ViewOpcode;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

/** Gui View: last layer between the controller and the view
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 15 Jun 2015
 */
public class GUIView extends View {
    /** Main frame */
    private GUIFrame mMainFrame;
    
    /** Game info */
    private GameInfo gameInfo = null;

    /** Create view and initialize main frame
     *
     * @param controller The local game controller
     */
    public GUIView(GameController controller) {
        super(controller);
        mMainFrame = new GUIFrame(controller);
    }

    /** Ask for a connection in a dialog
     *
     * @see it.polimi.ingsw.client.View#askConnectionType(java.lang.String[])
     */
    @Override
    public int askConnectionType(String[] params) {
        Object ret = JOptionPane.showInputDialog(null, "Choose a connection", "Connection type", 
                JOptionPane.QUESTION_MESSAGE, null, params, params[0]);

        for(int i = 0; i<params.length;i++)
            if(params[i].equals(ret))
                return i;
        
        return -1;
    }

    /** Ask for a host in a dialog
     *
     * @see it.polimi.ingsw.client.View#askHost()
     */
    @Override
    public String askHost() {
        return JOptionPane.showInputDialog("Type the host");
    }

    /** Ask for a username in a dialog
     *
     * @see it.polimi.ingsw.client.View#askUsername(java.lang.String)
     */
    @Override
    public String askUsername(String message) {
        return JOptionPane.showInputDialog(message.length() == 0 ? "Type a username" : message);
    }

    /** Ask for a view in a dialog
     *
     * @see it.polimi.ingsw.client.View#askView(java.lang.String[])
     */
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

    /** Ask for a map in a dialog
     *
     * @see it.polimi.ingsw.client.View#askMap(java.lang.String[])
     */
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

    /** Make this frame visible
     *
     * @see it.polimi.ingsw.client.View#run()
     */
    @Override
    public void run() {
        mMainFrame.setVisible(true);
    }

    /** Show an error in a dialog
     *
     * @see it.polimi.ingsw.client.View#showError(java.lang.String)
     */
    @Override
    public void showError(String string) {
        JOptionPane.showMessageDialog(null, string, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Update login time in the login canvas
     *
     * @see it.polimi.ingsw.client.View#updateLoginTime(int)
     */
    @Override
    public void updateLoginTime(int i) {
        mMainFrame.setRemainingTime(i);
    }

    /** Update login statistics about players
     *
     * @see it.polimi.ingsw.client.View#updateLoginStat(int)
     */
    @Override
    public void updateLoginStat(int i) {
        mMainFrame.setPlayers(i);
    }


    /** Move to the actual game
     *
     * @see it.polimi.ingsw.client.View#switchToMainScreen(it.polimi.ingsw.game.common.GameInfo)
     */
    @Override
    public void switchToMainScreen(GameInfo container) {
        gameInfo = container;
        GameMap map = container.getMap();
        mMainFrame.setGameInfo( container );
        mMainFrame.switchToMap(map,map.getStartingPoint(container.isHuman()));

        mMainFrame.validate();
        mMainFrame.repaint();
    }

    /** Handle view commands. 
     * Enable several graphical elements according to the view commands received from the server.
     *
     * @see it.polimi.ingsw.client.View#handleCommand(java.util.List)
     */
    @Override
    protected void handleCommand(List<ViewCommand> cmd) {
        /** reset GUI  */
        resetViewStatus();

        // end turn automatically if the only option possible is End Turn
        if( cmd.size() == 1 && cmd.get(0).getOpcode() == ViewOpcode.CMD_ENDTURN )
                mMainFrame.clickOnEndTurn();
        // draw a dangerous card automatically if this is the only option left
        else if( cmd.size() == 1 && cmd.get(0).getOpcode() == ViewOpcode.CMD_DRAWDANGEROUSCARD ) 
                mMainFrame.clickOnDrawDangerousCard();
        else {
            // enable a gui element if requested by a command
            for(ViewCommand c : cmd) {
                switch(c.getOpcode()) {
                case CMD_CHOOSEOBJECTCARD:
                    setCanSelectObjCard( true );
                    break;

                case CMD_ENABLEMAPVIEW:
                    setEnableMap(c);
                    break;

                case CMD_ATTACK:
                    setAttackEnabled( true );
                    break;

                case CMD_DISCARDOBJECTCARD:
                    setDiscardObjCardEnabled( true );
                    showInfo(null, "Discard a card by right clicking on it (or use it as usual if possible)");
                    break;

                case CMD_DRAWDANGEROUSCARD:
                    setCanDrawDangerousCard( true );
                    break;
                case CMD_ENDTURN:
                    setEndTurnEnabled( true );
                    break;

                default:
                    break;
                }
            }
        }
    }

    /** Enable some (or all) the sectors in the main map
     *
     * @param c The main command received from the server
     */
    private void setEnableMap(ViewCommand c) {
        if( c.getArgs().length > 0 && c.getArgs()[0] instanceof Point && c.getArgs()[1] instanceof Integer) {
            /** start from a point and choose a position within x moves */
            Set<Point> enabledCells = mController.getMap().getCellsWithMaxDistance( (Point)c.getArgs()[0], (int)c.getArgs()[1], gameInfo.isHuman());
            mMainFrame.enableMapCells(enabledCells);
        } else 
            /** choose any position */
            mMainFrame.enableMapCells( null );
    }

    /** Enable end turn button
     *
     * @param value The value to set
     */
    private void setEndTurnEnabled( boolean value ) {
        mMainFrame.setEndTurnEnabled( value );
    }

    /** Enable draw dangerous card button
     *
     * @param value
     */
    private void setCanDrawDangerousCard( boolean value ) {
        mMainFrame.setCanDrawDangerousCard( value );
    }

    /** Enable discard card action
     *
     * @param value
     */
    private void setDiscardObjCardEnabled(boolean value) {
        mMainFrame.setDiscardObjCardEnabled( value );
    }

    /** Enable attack button
     *
     * @param value
     */
    private void setAttackEnabled(boolean value) {
        mMainFrame.setAttackEnabled( value );
    }

    /** Enable use object card action
     *
     * @param value
     */
    private void setCanSelectObjCard(boolean value) {
        mMainFrame.setCanSelectObjCard( value );
    }

    /** Reset all graphical elements (buttons & map) */
    private void resetViewStatus() {
        mMainFrame.resetViewStatus();
    }

    /** Display info in the right area
     *
     * @see it.polimi.ingsw.client.View#showInfo(java.lang.String, java.lang.String)
     */
    @Override
    public void showInfo(String user, String message) {
        mMainFrame.showInfo( user, message );
    }    


    /** Show noise action on map
     *
     * @see it.polimi.ingsw.client.View#showNoiseInSector(java.lang.String, java.awt.Point)
     */
    @Override
    public void showNoiseInSector(String user, Point p) {
        mMainFrame.showNoiseInSector(user, p);
    }

    /** Called at the start of my turn
     *
     * @see it.polimi.ingsw.client.View#onMyTurn()
     */
    @Override
    public void onMyTurn( int moveCounter ) {
        mMainFrame.resetBlinkingElements();
        showInfo(null, "It's your turn!");
        showInfo(null, "Move number: " + moveCounter);
    }

    /** Called at the start of someone else's turn
     *
     * @see it.polimi.ingsw.client.View#onOtherTurn(java.lang.String)
     */
    @Override
    public void onOtherTurn(String username) {
        mMainFrame.resetBlinkingElements();
        mMainFrame.resetViewStatus();
        mMainFrame.showInfo( null, "-- It's " + username + "'s turn! --" );
    }

    /** Not implemented.
     *
     * @see it.polimi.ingsw.client.View#startup()
     */
    @Override
    public void startup() {
        return;
    }

    /** Show ending canvas (with winners and losers stats)
     *
     * @see it.polimi.ingsw.client.View#showEnding(java.util.List, java.util.List)
     */
    @Override
    public void showEnding(List<Integer> winnerList, List<Integer> loserList) {
        mMainFrame.showEnding( winnerList, loserList );
    }

    /** Update the cards panel on every change 
     *
     * @see it.polimi.ingsw.client.View#notifyObjectCardListChange(java.util.List)
     */
    @Override
    public void notifyObjectCardListChange(List<Integer> listOfCards) {
        mMainFrame.notifyObjectCardListChange(listOfCards);
    }

    /** Update players' info in the bottom panel
     *
     * @see it.polimi.ingsw.client.View#updatePlayerInfoDisplay(int)
     */
    @Override
    public void updatePlayerInfoDisplay( int idPlayer ) {
        mMainFrame.updatePlayerInfoDisplay( idPlayer );
    }

    /** Create map of sector - players to be then passed to the main frame.
     *
     * @see it.polimi.ingsw.client.View#handleSpotlightResult(java.awt.Point, java.awt.Point[])
     */
    @Override
    public void handleSpotlightResult(Point chosenPoint, Point[] playersFound) {
        Map<String, Point> data = new HashMap<String, Point>();
        
        for(int i = 0; i < playersFound.length; i++) {
            Point p = playersFound[i];
            if(p != null) {
                data.put( gameInfo.getPlayersList()[ i ].getUsername(), p);
            }
        }
        
        mMainFrame.setSpotlightData( data );
    }

    /** Handle attack animation
     *
     * @see it.polimi.ingsw.client.View#handleAttack(java.lang.String, java.awt.Point)
     */
    @Override
    public void handleAttack(String user, Point p) {
        mMainFrame.handleAttack( p );
    }

    /** Show ending
     * @see it.polimi.ingsw.client.View#showEnding(java.lang.String)
     * @param string The string
     */
    @Override
    public void showEnding(String string) {
        mMainFrame.showEnding(string);
    }

    /** Change the color for this sector
     * @see it.polimi.ingsw.client.View#changeSectorToUsedHatch(java.awt.Point)
     */
    @Override
    public void changeSectorToUsedHatch(Point point) {
        mMainFrame.changeSectorToUsedHatch(point);
        
    }
  
}
