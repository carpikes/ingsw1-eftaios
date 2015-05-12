package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sectors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameMapPanel extends JPanel {
	private GameMap gameMap;
	
	// Sector ID <-> image
	private Map<Integer, BufferedImage> sectorImages;
	
	public GameMapPanel( GameMap map ) {
		super();
		
		gameMap = map;
		readImages();
		this.setBackground(Color.WHITE);
	}
	
	public Dimension getPreferredSize() {
	        return new Dimension(GameScreen.WIDTH*5/8, GameScreen.HEIGHT);
	}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);       

        drawMap(g);
    }  
    
    private void drawMap(Graphics g) {
    	float maxSectorWidth = GameScreen.WIDTH / GameMap.WIDTH; 
    	float maxSectorHeight = GameScreen.HEIGHT / GameMap.HEIGHT;
    	
    	
    	
    }
    
    private void readImages() {
    	/* FIXME: IMAGE HANDLER */
    	sectorImages = new HashMap<>();
    	
    	try {
    	    sectorImages.put(Sectors.ALIEN, ImageIO.read(new File("sector_alien.png")));
    	    sectorImages.put(Sectors.DANGEROUS, ImageIO.read(new File("sector_dangerous.png")));
    	    sectorImages.put(Sectors.NOT_DANGEROUS, ImageIO.read(new File("sector_not_dangerous.png")));
    	    sectorImages.put(Sectors.HATCH, ImageIO.read(new File("sector_hatch.png")));
    	    sectorImages.put(Sectors.HUMAN, ImageIO.read(new File("sector_human.png")));
    	    sectorImages.put(Sectors.NOT_VALID, ImageIO.read(new File("sector_not_valid.png")));
    	} catch (IOException e) {
    		
    	}
    }
}
