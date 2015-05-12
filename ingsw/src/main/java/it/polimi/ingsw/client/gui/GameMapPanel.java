package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GameMapPanel extends JPanel {
	private GameMap gameMap;
	
	public GameMapPanel( GameMap map ) {
		super();
		
		gameMap = map;
		this.setBackground(Color.WHITE);
	}
	
	public Dimension getPreferredSize() {
	        return new Dimension(GameScreen.WIDTH*5/8, GameScreen.HEIGHT);
	}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        drawMap();
    }  
    
    private void drawMap() {
    	float maxSectorWidth = GameScreen.WIDTH / GameMap.WIDTH; 
    	float maxSectorHeight = GameScreen.HEIGHT / GameMap.HEIGHT;
    	
    	
    }
}
