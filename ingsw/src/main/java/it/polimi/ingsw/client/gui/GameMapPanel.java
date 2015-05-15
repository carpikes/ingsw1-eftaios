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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
	
	private Dimension screenSize; // get screen resolution
	private int marginWidth, marginHeight;
	
	private GameMap gameMap;
	private Polygon[][] hexagons;
	private Map<Integer, TexturePaint> sectorTextures;
	private Polygon currentPolygon;
	
	int hexWidth, hexRadius, hexHeight;
	
	public GameMapPanel( GameMap map ) {
		super();
		
		screenSize = new Dimension( new Dimension( (int) (GameScreen.screenSize.width*0.7), GameScreen.screenSize.height ) ); // FIXME fix resolution!
		setPreferredSize( screenSize );
		
		gameMap = map;
		hexagons = new Polygon[GameMap.ROWS][GameMap.COLUMNS];
		
		this.addMouseMotionListener( new HexListener() );
		this.addComponentListener( new ComponentListener() {
		   
	            @Override
	            public void componentResized(ComponentEvent e) {
	               screenSize = e.getComponent().getSize();
	               System.out.println(e);
	               
	               calcValuesForHexagons();
	               createHexagons();
	               repaint();
	            }

                @Override
                public void componentHidden(ComponentEvent arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void componentMoved(ComponentEvent arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void componentShown(ComponentEvent arg0) {
                    // TODO Auto-generated method stub
                    
                }
		});
		
		this.setBackground(Color.WHITE);
		
		currentPolygon = null;
		
		loadTextures();
		 
        calcValuesForHexagons();
        createHexagons();
	}
	
	private void loadTextures() {
	    sectorTextures = new HashMap<>();
    	
    	try {  		
			addTextureToMap(Sectors.ALIEN, "images/sector_alien.png");
			addTextureToMap(Sectors.DANGEROUS, "images/sector_dangerous.png");
    	    addTextureToMap(Sectors.NOT_DANGEROUS, "images/sector_not_dangerous.png");
    	    addTextureToMap(Sectors.HATCH, "images/sector_hatch.png");
    	    addTextureToMap(Sectors.HUMAN, "images/sector_human.png");
    	    addTextureToMap(Sectors.NOT_VALID, "images/sector_not_valid.png");
    	} catch (IOException e) {
    		log.log(Level.SEVERE, "Texture images for sectors not found.");
    	}
	}
	
	private void addTextureToMap( int sector, String imgName ) throws IOException {
	    // read image, create texture and clip it to an hexagon of the correct size
	    BufferedImage img = ImageIO.read(new File(imgName));
	    TexturePaint texture = new TexturePaint(img, new Rectangle(0, 0, hexWidth, hexHeight));
	    
	    sectorTextures.put(
	            sector, 
	            texture
	            );
	}

    public void paintComponent(Graphics g) {
    	super.paintComponent(g);       

    	Graphics2D g2d = (Graphics2D)g;

    	RenderingHints rh = new RenderingHints(
    			RenderingHints.KEY_INTERPOLATION,
    			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    	RenderingHints rh2 = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	g2d.setRenderingHints(rh);
    	g2d.setRenderingHints(rh2);
    	
    	drawMap( g2d );
    }  
           
    private void createHexagons() {
        for( int i = 0; i < GameMap.ROWS; ++i ) {
            for ( int j = 0; j < GameMap.COLUMNS; ++j ) {
                // calculate position of current hex's center
                // for more info on formulas used: http://www.redblobgames.com/grids/hexagons/#basics
                int centerX, centerY;
                
                centerX = (int)(marginWidth + 0.5 * hexWidth + j * 0.75 * hexWidth);
                
                if( j % 2 == 0 )
                    centerY = (int)( marginHeight + 0.5 * hexHeight + i * hexHeight );
                else
                    centerY = (int)( marginHeight + hexHeight*(i+1) );
                
                hexagons[i][j] = HexagonFactory.createHexagon( new Point(centerX, centerY), hexRadius );
            }
        }
    }
    
    /* Calculate how much space we can dedicate to each hexagon. */
	private void calcValuesForHexagons() {
	    // since we're using float calculations here, the results are not 100% precise. We apply half of the remaining pixels to center the hexagons on the panel
        double scaleWidth = ((GameMap.COLUMNS-1)*3.0/4)+1;
        hexWidth = (int)(screenSize.getWidth()/scaleWidth);
        marginWidth = (int)(screenSize.getWidth() - scaleWidth*hexWidth)/2;
		hexRadius = hexWidth / 2;

        // the same goes for the height of course
		double scaleHeight = (GameMap.ROWS+0.5);
		hexHeight = (int)(screenSize.getHeight()/scaleHeight);
		marginHeight = (int)(screenSize.getHeight() - scaleHeight*hexHeight)/2;
		
		/*double scaleHeight = Math.sqrt(3) / 2 * hexWidth;
        hexHeight = (int)( scaleHeight );
        marginHeight = (int)(screenSize.getHeight() - hexHeight*GameMap.ROWS)/2;*/
	}
	
	private void drawMap( Graphics2D g ) {
        TexturePaint currentTexture = null;
        Sector currentSector = null;
        
        /* for every sector in map, set the correct texture and paint to screen */
        for( int i = 0; i < GameMap.ROWS; ++i ) {
            for ( int j = 0; j < GameMap.COLUMNS; ++j ) {
                currentSector = gameMap.getSectorAt(i, j);
                currentTexture = sectorTextures.get(currentSector.getId());
                
                g.setPaint(currentTexture);
                g.fillPolygon( hexagons[i][j] );
            }
        }
        
       /* ------------ */ 
       if( currentPolygon != null ) {
           //TODO currentPolygon
       }
	}
	
	class HexListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent arg0) { }

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
}
