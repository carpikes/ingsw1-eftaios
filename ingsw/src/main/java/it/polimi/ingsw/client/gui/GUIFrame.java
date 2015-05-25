package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.config.Config;

import java.awt.Dimension;
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
        mLoginCanvas.setTime(remainingTime);
    }
    public void setPlayers(int newPlayers) {
        mLoginCanvas.setPlayers(newPlayers);
    }
    
    private void switchToLogin() {
        mLoginCanvas = new LoginCanvasPanel();
        mLoginCanvas.setPreferredSize(mDimension);
        add(mLoginCanvas);
        pack();
    }
    
    public void switchToMap(GameMap map) {
        if(mLoginCanvas == null)
            throw new RuntimeException("Map is already loaded");
        try {
            mMapCanvas = new MapCanvasPanel( map, Config.GAME_MAP_WIDTH, Config.GAME_MAP_HEIGHT);
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
}
