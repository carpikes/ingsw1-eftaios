package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.exception.DrawingModeException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.Sectors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

/**
 * Panel where the map is drawn. It is used in {@link GameMainScreen} class. 
 * @author Michele Albanese <michele.albanese@mail.polimi.it>
 */
public class MapCanvasPanel extends JPanel {
    
    private static final long serialVersionUID = -5583245069814214909L;
    
    // the map being loaded
    private GameMap gameMap;

    // the array of all drawn hexagons
    private Hexagon[][] hexagons;

    // Color bindings
    private Map<Integer, Color> sectorColors;

    // Selected hex: contains indexes i and j in hexagons[][] array
    private Point currentHexCoordinates;

    // Values for every hexagon
    private double hexWidth;
    private double hexHeight;
    private double marginHeight;
    
    // canvas width and height
    private int canvasWidth;
    private int canvasHeight;
    
    /**
     * Instantiates a new map canvas panel.
     *
     * @param map the map to be drawn
     * @param canvasWidth the canvas width
     * @param canvasHeight the canvas height
     */
    public MapCanvasPanel( GameMap map, int canvasWidth, int canvasHeight ) {
        // initialization 
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        hexagons = new Hexagon[GameMap.ROWS][GameMap.COLUMNS];
        gameMap = map;
        currentHexCoordinates = null;

        // methods for detecting mouse position and clicking
        this.addMouseListener( new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) { }
            
            @Override
            public void mouseClicked(MouseEvent arg0) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }
        });
        
        this.addMouseMotionListener( new MouseMotionListener() {
            // FIXME: ignore not selectable sectors
            // get current hex by inspecting each shape area
            @Override
            public void mouseMoved(MouseEvent e) {
                for( int i = 0; i < hexagons.length; ++i )
                    for( int j = 0; j < hexagons[i].length; ++j )
                        if( hexagons[i][j] != null && hexagons[i][j].getPath().contains( e.getPoint() ) ) {
                        	Point oldHexCoordinates = currentHexCoordinates;
                            currentHexCoordinates = new Point( i, j );
                            repaint();
                            
                            return;
                        }
                
                currentHexCoordinates = null; // no selectable hexagons found
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) { }
        });

        // calc width, height and margins according to size
        calculateValuesForHexagons();

        // set colors for every sector
        createSectorColorsMap();
    }

    /**
     * Creates the sector colors map.
     */
    private void createSectorColorsMap() {
        sectorColors = new HashMap<>();   

        sectorColors.put(Sectors.ALIEN, new Color(90,0,0));
        sectorColors.put(Sectors.DANGEROUS, new Color(236,20,83));
        sectorColors.put(Sectors.NOT_DANGEROUS, new Color(222,189,218));
        sectorColors.put(Sectors.HATCH, new Color(47,53,87));
        sectorColors.put(Sectors.HUMAN, new Color(200,0,0));
        sectorColors.put(Sectors.NOT_VALID, new Color(239,236,243));
    }

    /**
     * Calculate values for hexagons.
     */
    private void calculateValuesForHexagons() {
        // Reference: http://www.redblobgames.com/grids/hexagons/
        hexWidth = canvasWidth / ( 0.75 * (GameMap.COLUMNS-1) + 1 );
        hexHeight =  hexWidth * Math.sqrt(3)/2;
        marginHeight = (canvasHeight - hexHeight * ( GameMap.ROWS + 0.5 )) / 2;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background

        Graphics2D g2d = (Graphics2D)g;
        setBackground(Color.WHITE);  // set background color for this JPanel

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        drawHexagons(g2d);
    }

    /**
     * Draw hexagons.
     *
     * @param g2d The Graphics2D object where to draw on
     */
    private void drawHexagons(Graphics2D g2d) {            
        // Draw columns first since it's easier to do
        for( int col = 0; col < GameMap.COLUMNS; ++col ) {
            for( int row = 0; row < GameMap.ROWS; ++ row ) {
            	// draw only if it is a valid sector
                if( gameMap.getSectorAt(row, col).getId() != Sectors.NOT_VALID )
                	drawHexAt(g2d, new Point(row, col), DrawingMode.NORMAL);
            }
        }
        
        if( currentHexCoordinates != null ) {
            drawHexAt(g2d, currentHexCoordinates, DrawingMode.SELECTED_HEX);
        }
    }

    /**
     * Draw hex at given coordinates.
     *
     * @param g2d The Graphics2D object where to draw on
     * @param position the position(i,j) in array of hexagons
     * @param mode {@link DrawingMode} to use
     */
    private void drawHexAt(Graphics2D g2d, Point position, DrawingMode mode) {
        Sector currentSector;
        Point2D.Double startPoint;
        
        // calculate point coordinates of upper-left corner of containing rectangle
        startPoint =  new Point2D.Double(hexWidth*3/4*position.y, marginHeight + position.x * hexHeight + ( isEvenColumn(position.y)  ? 0 : hexHeight/2 ) );
        
        // create hexagon: center of it is distant (hexWidth/2, hexHeight/2) from the starting point
        hexagons[position.x][position.y] = HexagonFactory.createHexagon(new Point2D.Double(startPoint.getX() + hexWidth/2, startPoint.getY() + hexHeight/2), hexWidth/2);

        // fill the shape according to sector type
        currentSector = gameMap.getSectorAt(position.x, position.y);
        
        // TODO: add other drawing modes
        // some tweaks on color according to drawing mode
        switch( mode ) {
        case NORMAL:
            g2d.setColor( sectorColors.get( currentSector.getId() ) );
            break;
            
        case SELECTED_HEX:
            g2d.setColor( Color.CYAN ); // FIXME color hardcoded
            break;
            
        default:
            throw new DrawingModeException("Drawing mode not supported");
        }
        
        g2d.fill(hexagons[position.x][position.y].getPath());
        
        // border
        g2d.setColor(Color.BLACK);
        g2d.draw(hexagons[position.x][position.y].getPath());
    }
    
    /**
     * Checks if column % 2 == 0.
     *
     * @param col the column index
     * @return true, if is even column
     */
    private boolean isEvenColumn( int col ) {
        return col % 2 == 0;
    }
    
    /**
     * The Enum DrawingMode. Used by {@link MapCanvasPanel#drawHexAt(Graphics2D, Point, DrawingMode)}
     */
    private enum DrawingMode {
        NORMAL,             // set color from sectorColors map
        SELECTED_HEX        // hover color
    }
}