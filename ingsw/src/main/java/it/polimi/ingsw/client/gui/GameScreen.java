package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameScreen {
	
	// TODO: a config file could be useful here
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	// FIXME: change to non-static once you rip off main method
	private static GameMap currentMap;
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        JFrame f = new JFrame("Escape from the aliens in outer space");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        
        GameMapPanel gameMapPanel = new GameMapPanel( currentMap );
        f.add(gameMapPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(WIDTH*3/8,HEIGHT));
        f.add(rightPanel, BorderLayout.EAST);
        
        f.pack();
        f.setLocationRelativeTo(null); // center window
        f.setVisible(true);
    }
}
