package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sectors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameMapPanel extends JPanel {
	
	private static final Logger log = Logger.getLogger( GameMapPanel.class.getName() );
	
	private GameMap gameMap;
	
	/* ----- to be removed soon -------- */
	private BufferedImage img, img2; 
	private Polygon p1, p2;
	/* ----- to be removed soon -------- */
	
	private Map<Integer, BufferedImage> sectorImages;
	private TexturePaint texture;
	
	private Polygon currentPolygon;
	
	private final int width = 800;
	private final int height = 600;
	private final int widthExternalSpace = 9; // externalspace + width - 2 * externalspace + externalspace
	private final int heightExternalSpace = 6; // same for height
	
	int hexWidth, hexSize, hexHeight;
	
	public GameMapPanel( GameMap map ) {
		super();
		
		gameMap = map;
				
		this.addMouseMotionListener( new HexListener() );
		this.setBackground(Color.WHITE);
		
		currentPolygon = null;
		
		loadTextures();
		calcValuesForHexagons();
		
		setInitialState();
	}
	
	public Dimension getPreferredSize() {
	        return new Dimension(width,height);
	}
	
	private void loadTextures() {
    	sectorImages = new HashMap<>();
    	
    	try {  
    		/* ----- to be removed soon -------- */
    		img = ImageIO.read(new File("images/texture.jpg"));
			img2 = ImageIO.read(new File("images/hex.png"));
			/* ----- to be removed soon -------- */
			
    	    sectorImages.put(Sectors.ALIEN, ImageIO.read(new File("images/sector_alien.png")));
    	    sectorImages.put(Sectors.DANGEROUS, ImageIO.read(new File("images/sector_dangerous.png")));
    	    sectorImages.put(Sectors.NOT_DANGEROUS, ImageIO.read(new File("images/sector_not_dangerous.png")));
    	    sectorImages.put(Sectors.HATCH, ImageIO.read(new File("images/sector_hatch.png")));
    	    sectorImages.put(Sectors.HUMAN, ImageIO.read(new File("images/sector_human.png")));
    	    sectorImages.put(Sectors.NOT_VALID, ImageIO.read(new File("images/sector_not_valid.png")));
    	} catch (IOException e) {
    		log.log(Level.WARNING, "Texture images for sectors not found.");
    	}
	}

    public void paintComponent(Graphics g) {
    	super.paintComponent(g);       

    	Graphics2D g2d = (Graphics2D)g;

    	RenderingHints rh = new RenderingHints(
    			RenderingHints.KEY_ANTIALIASING,
    			RenderingHints.VALUE_ANTIALIAS_ON);
    	g2d.setRenderingHints(rh);

    	TexturePaint slatetp = new TexturePaint(img, new Rectangle(0, 0, 90, 60));
    	g2d.setPaint(slatetp);
    	
    	g2d.fillPolygon( p1 );    
    	g2d.fillPolygon( p2 );  
    	
    	if( currentPolygon != null ) {
    		TexturePaint slatetp1 = new TexturePaint(img2, new Rectangle(0, 0, 90, 60));
        	g2d.setPaint(slatetp1);
    		g2d.fillPolygon( currentPolygon );
    	}
    }  
    
    class HexListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent arg0) {	}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// FIXME: Polygon p = getPolygonFromPoint();
			if( p1.contains(arg0.getPoint()) ) {
				currentPolygon = p1;
			} else {
				currentPolygon = null;
			}
			
			repaint();
		}
    }
    
    private int calcHexWidth() {
    	return (width - 2 * widthExternalSpace)/23; // FIXME: cambia quel 23 con la costante dalla classe Sectors!
    }
    
    private void setInitialState() {
    	p1 = HexagonFactory.createHexagon( new Point(200,200), hexSize );
		p2 = HexagonFactory.createHexagon( new Point(200,200+hexHeight), hexSize );
    }
    
    /* Calculate how much space we can dedicate to each hexagon. */
	private void calcValuesForHexagons() {
		
		hexWidth = calcHexWidth();
		hexSize = hexWidth / 2;
		hexHeight = (int)( Math.sqrt(3) * 2 * hexSize );
	}
}
