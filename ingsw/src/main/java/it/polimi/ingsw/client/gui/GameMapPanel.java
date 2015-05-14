package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;
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
	private Polygon[][] hexagons;
	/* ----- to be removed soon -------- */
	
	private Map<Integer, TexturePaint> sectorTextures;
	private TexturePaint texture;
	
	private Polygon currentPolygon;
	
	private final int width = 800;
	private final int height = 600;
	
	int hexWidth, hexRadius, hexHeight;
	
	public GameMapPanel( GameMap map ) {
		super();
		
		gameMap = map;
		hexagons = new Polygon[GameMap.ROWS][GameMap.COLUMNS];
		
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
	    sectorTextures = new HashMap<>();
    	
    	try {  
    		/* ----- to be removed soon -------- */
    		img = ImageIO.read(new File("images/texture.jpg"));
			img2 = ImageIO.read(new File("images/hex.png"));
			/* ----- to be removed soon -------- */
			
			addTextureToMap(Sectors.ALIEN, "images/sector_alien.png");
			addTextureToMap(Sectors.DANGEROUS, "images/sector_dangerous.png");
    	    addTextureToMap(Sectors.NOT_DANGEROUS, "images/sector_not_dangerous.png");
    	    addTextureToMap(Sectors.HATCH, "images/sector_hatch.png");
    	    addTextureToMap(Sectors.HUMAN, "images/sector_human.png");
    	    addTextureToMap(Sectors.NOT_VALID, "images/sector_not_valid.png");
    	} catch (IOException e) {
    		log.log(Level.WARNING, "Texture images for sectors not found.");
    	}
	}
	
	private void addTextureToMap( int sector, String imgName ) throws IOException {
	    sectorTextures.put(Sectors.ALIEN, new TexturePaint(ImageIO.read(new File(imgName)), new Rectangle(0, 0, hexWidth, hexHeight)));
	}

    public void paintComponent(Graphics g) {
    	super.paintComponent(g);       

    	Graphics2D g2d = (Graphics2D)g;

    	RenderingHints rh = new RenderingHints(
    			RenderingHints.KEY_ANTIALIASING,
    			RenderingHints.VALUE_ANTIALIAS_ON);
    	g2d.setRenderingHints(rh);
    	
    	// TODO: set texture according to value read from game map
    	drawMap( g2d );
    }  
    
    class HexListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent arg0) {	}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// FIXME: Polygon p = getPolygonFromPoint();
			/*if( p1.contains(arg0.getPoint()) ) {
				currentPolygon = p1;
			} else {
				currentPolygon = null;
			}*/
			
			repaint();
		}
    }
       
    private void setInitialState() {
        for( int i = 0; i < GameMap.ROWS; ++i ) {
            for ( int j = 0; j < GameMap.COLUMNS; ++j ) {
                // calculate position of current hex's center
                // for more info on formulas used: http://www.redblobgames.com/grids/hexagons/#basics
                int centerX = (int)(hexWidth * (0.5 + 0.75*j));
                int centerY;
                
                if( i % 2 == 0 ) {
                    centerY = (int)( (i+0.5)*hexHeight );
                } else {
                    centerY = (int)((i+1.5)*hexHeight );
                }
                
                hexagons[i][j] = HexagonFactory.createHexagon( new Point(centerX, centerY), hexRadius );
            }
        }
    }
    
    /* Calculate how much space we can dedicate to each hexagon. */
	private void calcValuesForHexagons() {
		hexWidth = width/GameMap.ROWS;
		hexRadius = hexWidth / 2;
		hexHeight = height/GameMap.COLUMNS; //(int)( Math.sqrt(3) * 2 * hexSize );
	}
	
	public Map<Integer, TexturePaint> getSectorTextures() {
		return sectorTextures;
	}
	
	private void drawMap( Graphics2D g ) {
	    TexturePaint currentTexture = null;
	    Sector currentSector = null;
	    
	    /* for every sector in map, set the correct texture and paint to screen */
	    for( int i = 0; i < GameMap.ROWS; ++i ) {
            for ( int j = 0; j < GameMap.COLUMNS; ++j ) {
                currentSector = gameMap.getSectorAt(i, j);
                currentTexture = sectorTextures.get(currentSector);
                
                g.setPaint(currentTexture);
                g.fillPolygon( hexagons[i][j] );
            }
        }
	    
	   /* ------------ */ 
       if( currentPolygon != null ) {
            TexturePaint slatetp1 = new TexturePaint(img2, new Rectangle(0, 0, hexWidth, hexHeight));
            g.setPaint(slatetp1);
            g.fillPolygon( currentPolygon );
        }
	}
}
