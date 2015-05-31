package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */

public class GUIFrame extends JFrame {
    private static final Logger LOG = Logger.getLogger( GUIFrame.class.getName() );
    
    // Constants
    private static final int WIDTH  = 1024;
    private static final int HEIGHT = 768;
    private static final Dimension mDimension = new Dimension(WIDTH, HEIGHT);

    // Drawing canvas
    private MapCanvasPanel mMapCanvas;
    private static final long serialVersionUID = 1L;
    private LoginCanvasPanel mLoginCanvas;
    
    public GUIFrame() {
        super();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);   
        setTitle("Escape from the Aliens in Outer Space");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        
        switchToLogin();
    }
    
    public void setRemainingTime(int remainingTime) {
        if(mLoginCanvas != null)
            mLoginCanvas.setTime(remainingTime);
    }
    public void setPlayers(int newPlayers) {
        if(mLoginCanvas != null)
            mLoginCanvas.setPlayers(newPlayers);
    }
    
    private void switchToLogin() {
        mLoginCanvas = new LoginCanvasPanel();
        mLoginCanvas.setPreferredSize(mDimension);
        add(mLoginCanvas);
        pack();
    }
    
    public void switchToMap(GameMap map, Point startingPoint) {
        if(mLoginCanvas == null)
            throw new RuntimeException("Map is already loaded");
        try {
            mMapCanvas = new MapCanvasPanel( map, WIDTH, HEIGHT, startingPoint);
            mMapCanvas.setPreferredSize(mDimension);
        } catch (ArrayIndexOutOfBoundsException | SectorException | NumberFormatException e) {
            LOG.log(Level.SEVERE, "File is not well formatted: " + e);
            System.exit(1);
        }
        
        remove(mLoginCanvas);
        add(mMapCanvas);
        mLoginCanvas = null;
        pack();
    }

    public Point getChosenMapCell() {
        return mMapCanvas.getChosenMapCell();
    }

    public void enableMapCells(Set<Point> pnt) {
        mMapCanvas.setEnabledCells(pnt);
    }

    public void resetChosenMapCell() {
        mMapCanvas.setEnabledCells(null);
    }
    
    public static void main(String[] args) throws IOException {
        GUIFrame f = new GUIFrame();
        GameMap map = GameMap.createFromId(1);
        f.switchToMap(map, map.getStartingPoint(true));
        f.setVisible(true);
        Set<Point> pnt = map.getCellsWithMaxDistance(map.getStartingPoint(true), 2); 
        f.enableMapCells(pnt);
    }

    public void setPlayerPosition(Point startingPoint) {
        mMapCanvas.setPlayerPosition(startingPoint);
    }
    
}
