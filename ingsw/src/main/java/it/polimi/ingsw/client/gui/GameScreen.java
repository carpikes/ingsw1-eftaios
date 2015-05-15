package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameScreen {
	
	// TODO: a config file could be useful here
	public static Dimension screenSize;
	
	// FIXME: change to non-static once you rip off main method
	// private GameMap currentMap;
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        JFrame f = new JFrame("Escape from the aliens in outer space");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setUndecorated(true);
        f.setResizable(false);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        GameMapPanel gameMapPanel = new GameMapPanel( GameMap.createFromMapFile( new File("maps/galilei.txt")) );
        f.add(gameMapPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize( new Dimension( (int) (screenSize.width*0.3), screenSize.height ) );
        f.add(rightPanel, BorderLayout.EAST);
        
        f.pack();
        f.setLocationRelativeTo(null); // center window
        f.setVisible(true);
    }
}
