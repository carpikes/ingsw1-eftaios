package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */

public class GUIFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = Logger.getLogger( GUIFrame.class.getName() );
    
    // Constants
    private static final int WIDTH = 1024;
    private static final int WIDTH_LEFT  = 770;
    private static final int WIDTH_RIGHT  = WIDTH - WIDTH_LEFT;
    private static final int HEIGHT = 768;
    
    private static final Dimension mDimensionLeft = new Dimension(WIDTH_LEFT, HEIGHT);
    private static final Dimension mDimensionRight = new Dimension(WIDTH_RIGHT, HEIGHT);
    
    // Drawing canvas
    private MapCanvasPanel mMapCanvas;
    private LoginCanvasPanel mLoginCanvas;
    private final GameController mController;
    
    public GUIFrame(GameController controller) {
        super();
        
        mController = controller;
        setDefaultCloseOperation(EXIT_ON_CLOSE);   
        setTitle("Escape from the Aliens in Outer Space");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        
        createRightPanel();
        
        switchToLogin();
        pack();
    }
    
    /**
     * 
     */
    private void createRightPanel() {
        
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );
        rightPanel.setPreferredSize(mDimensionRight);
        rightPanel.setLayout( new BorderLayout() );
        
        /* card panel */
        JButton btnx = new JButton("Button 1");
        rightPanel.add(btnx, BorderLayout.NORTH);
        
        /* text area */
        JTextArea textArea = new JTextArea( 5, 20 );
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea); 
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        /* Action buttons panel */
        JPanel actionButtonsPanel = new JPanel( new FlowLayout() );
        
        JButton btnAttack = new JButton("Attack");
        JButton btnDiscardCard = new JButton("Discard Card");
        JButton btnEndTurn = new JButton("End Turn");
        
        actionButtonsPanel.add(btnAttack);
        actionButtonsPanel.add(btnDiscardCard);
        
        rightPanel.add(actionButtonsPanel, BorderLayout.SOUTH);
        
        /* Add the right panel to the main frame's pane */
        this.add(rightPanel, BorderLayout.EAST);
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
        mLoginCanvas.setPreferredSize(mDimensionLeft);
        add(mLoginCanvas, BorderLayout.CENTER);
        
    }
    
    public void switchToMap(GameMap map, Point startingPoint) {
        if(mLoginCanvas == null)
            throw new RuntimeException("Map is already loaded");
        try {
            mMapCanvas = new MapCanvasPanel(mController, map, WIDTH_LEFT, HEIGHT, startingPoint);
            mMapCanvas.setPreferredSize(mDimensionLeft);
        } catch (ArrayIndexOutOfBoundsException | SectorException | NumberFormatException e) {
            LOG.log(Level.SEVERE, "File is not well formatted: " + e);
            System.exit(1);
        }
        
        remove(mLoginCanvas);
        add(mMapCanvas, BorderLayout.CENTER);
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

    public void setPlayerPosition(Point startingPoint) {
        mMapCanvas.setPlayerPosition(startingPoint);
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        GUIFrame f = new GUIFrame(null);
        GameMap map = GameMap.createFromId(1);
        f.switchToMap(map, map.getStartingPoint(true));
        f.setVisible(true);
        
        
    }
    
}
