package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.exception.DrawingModeException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

/** Panel where the map is drawn. It is used in {@link GUIFrame} class. 
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
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

    private Set<Point> mEnabledCells = null;

    private boolean mClickedOnCell = false;
    
    private final Object renderLoopMutex = new Object();

    private Point mPlayerPosition;
    
    /**
     * Instantiates a new map canvas panel.
     *
     * @param map the map to be drawn
     * @param canvasWidth the canvas width
     * @param canvasHeight the canvas height
     */
    public MapCanvasPanel( GameMap map, int canvasWidth, int canvasHeight , Point playerPosition) {
        // initialization 
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        hexagons = new Hexagon[GameMap.ROWS][GameMap.COLUMNS];
        gameMap = map;
        currentHexCoordinates = null;
        mPlayerPosition = playerPosition;

        
        // add listeners
        addMouseListeners();

        // calc width, height and margins according to size
        calculateValuesForHexagons();

        // set colors for every sector
        createSectorColorsMap();
        
        new Timer(15, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MapCanvasPanel m = MapCanvasPanel.this;
                if(m != null)
                    m.repaint();
            }
            
        }).start();
    }

    /** Methods for detecting mouse position and clicking */
    private void addMouseListeners() {
        addMouseListener( new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) { }
            
            @Override
            public void mouseClicked(MouseEvent arg0) { 
                mClickedOnCell = true;
            }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }
        });
        
        addMouseMotionListener( new MouseMotionListener() {
            // FIXME: ignore not selectable sectors
            // get current hex by inspecting each shape area
            @Override
            public void mouseMoved(MouseEvent e) {
                Point cell = getCell(e.getPoint());
                
                synchronized(renderLoopMutex) {
                    currentHexCoordinates = cell;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) { }
        });
    }

    /**
     * Creates the sector colors map.
     */
    private void createSectorColorsMap() {
        sectorColors = new HashMap<>();   

        sectorColors.put(SectorBuilder.ALIEN, new Color(0,50,0));
        sectorColors.put(SectorBuilder.DANGEROUS, new Color(150,150,150));
        sectorColors.put(SectorBuilder.NOT_DANGEROUS, new Color(255,255,255));
        sectorColors.put(SectorBuilder.HATCH, new Color(47,53,87));
        sectorColors.put(SectorBuilder.HUMAN, new Color(50,0,0));
        sectorColors.put(SectorBuilder.NOT_VALID, null); 
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
        
        int x = -canvasWidth; // too much?
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.5f));
        while(x < canvasWidth) {
            g2d.drawLine(x, 0, x + (int)(Math.cos(Math.PI/360*60)*canvasHeight), canvasHeight);
            x += 30;
        }
        synchronized(renderLoopMutex) {
            drawHexagons(g2d);
        }
    }

    /**
     * Draw hexagons.
     *
     * @param g2d The Graphics2D object where to draw on
     */
    private void drawHexagons(Graphics2D g2d) { 
        DrawingMode dm;
        // Draw columns first since it's easier to do
        for( int col = 0; col < GameMap.COLUMNS; ++col ) {
            for( int row = 0; row < GameMap.ROWS; ++ row ) {
            	// draw only if it is a valid sector
                Sector sector = gameMap.getSectorAt(row, col);
                if( sector.getId() != SectorBuilder.NOT_VALID) {
                    Point pos = new Point(row, col);
                    dm = DrawingMode.NORMAL;
                    if(sector.equals(mPlayerPosition)) {
                        boolean a = true;
                        a =!a;
                    }
                    if(!sector.equals(mPlayerPosition) && mEnabledCells != null && !mEnabledCells.contains(pos))
                        dm = DrawingMode.DISABLED;
                    
                    if(!pos.equals(currentHexCoordinates))
                        drawHexAt(g2d, pos, dm);
                }
            }
        }
        
        if( currentHexCoordinates != null ) {
            Sector sector = gameMap.getSectorAt(currentHexCoordinates);
            if(mEnabledCells != null && !mEnabledCells.contains(currentHexCoordinates))
                dm = DrawingMode.DISABLED;
            else if(!sector.isCrossable())
                dm = DrawingMode.NORMAL;
            else
                dm = DrawingMode.SELECTED_HEX;
            
            drawHexAt(g2d, currentHexCoordinates, dm);
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
        boolean drawStroke = true;
        switch( mode ) {
            case NORMAL:
                g2d.setColor( sectorColors.get( currentSector.getId() ) );
                break;
                
            case SELECTED_HEX:
                g2d.setColor( Color.CYAN ); // FIXME color hardcoded
                break;
                
            case DISABLED:
                Color real = sectorColors.get( currentSector.getId() );
                drawStroke = false;
                g2d.setColor( new Color(real.getRed()/2, real.getGreen()/2, real.getBlue()/2, 0xa0)); // FIXME color hardcoded
                break;
                
            default:
                throw new DrawingModeException("Drawing mode not supported");
        }
        
        g2d.fill(hexagons[position.x][position.y].getPath());
        
        // border
        if(drawStroke) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(hexagons[position.x][position.y].getPath());
        }
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
        SELECTED_HEX,       // hover color
        DISABLED            // grey
    }

    public Point getChosenMapCell() {
        if(mClickedOnCell)
            return currentHexCoordinates;
        return null;
    }

    public void setEnabledCells(Set<Point> pnt) {
        synchronized(renderLoopMutex) {
            mEnabledCells = pnt;
        }
    }
    
    private Point getCell(Point p) {
        for( int i = 0; i < hexagons.length; ++i )
            for( int j = 0; j < hexagons[i].length; ++j )
                if( hexagons[i][j] != null && hexagons[i][j].getPath().contains( p ))
                    if(gameMap.getSectorAt(i, j).isCrossable())
                        return new Point( i, j );
                    else
                        return null;
        
        return null;
    }

    public void setPlayerPosition(Point point) {
        mPlayerPosition = point;
    }
}