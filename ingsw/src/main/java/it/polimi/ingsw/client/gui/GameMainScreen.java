package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.Sectors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameMainScreen extends JFrame {
    private static final Logger log = Logger.getLogger( GameMainScreen.class.getName() );

    // Constants
    public static final int CANVAS_WIDTH  = 1024;
    public static final int CANVAS_HEIGHT = 768;

    // Drawing canvas
    private MapCanvasPanel canvas;

    public GameMainScreen() {
        canvas = new MapCanvasPanel( GameMap.createFromMapFile( new File("maps/galvani.txt") ) );    
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        this.add(canvas);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);   
        this.pack();              
        this.setTitle("Escape from the Aliens in Outer Space");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private class MapCanvasPanel extends JPanel {
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

        public MapCanvasPanel( GameMap map ) {
            // initialization 
            hexagons = new Hexagon[GameMap.ROWS][GameMap.COLUMNS];
            gameMap = map;
            currentHexCoordinates = null;

            // methods for detecting mouse position and clicking
            this.addMouseListener( new MouseListener() {
                @Override
                public void mousePressed(MouseEvent e) {
                    
                }
                
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
                // TODO: ignore not selectable sectors
                // get current hex by inspecting each shape area
                @Override
                public void mouseMoved(MouseEvent e) {
                    for( int i = 0; i < hexagons.length; ++i )
                        for( int j = 0; j < hexagons[i].length; ++j )
                            if( hexagons[i][j].getShape().contains( e.getPoint() ) ) {
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

        private void createSectorColorsMap() {
            sectorColors = new HashMap<>();   

            sectorColors.put(Sectors.ALIEN, new Color(0,0,0));
            sectorColors.put(Sectors.DANGEROUS, new Color(236,20,83));
            sectorColors.put(Sectors.NOT_DANGEROUS, new Color(222,189,218));
            sectorColors.put(Sectors.HATCH, new Color(47,53,87));
            sectorColors.put(Sectors.HUMAN, new Color(200,0,0));
            sectorColors.put(Sectors.NOT_VALID, new Color(239,236,243));
        }

        private void calculateValuesForHexagons() {
            // Reference: http://www.redblobgames.com/grids/hexagons/
            hexWidth = CANVAS_WIDTH / ( 0.75 * (GameMap.COLUMNS-1) + 1 );
            hexHeight =  hexWidth * Math.sqrt(3)/2;
            marginHeight = (CANVAS_HEIGHT - hexHeight * ( GameMap.ROWS + 0.5 )) / 2;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background

            Graphics2D g2d = (Graphics2D)g;
            setBackground(Color.WHITE);  // set background color for this JPanel

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            drawHexagons(g2d);
        }

        private void drawHexagons(Graphics2D g2d) {            
            // Draw columns first since it's easier to do
            for( int col = 0; col < GameMap.COLUMNS; ++col ) {
                for( int row = 0; row < GameMap.ROWS; ++ row ) {
                    drawHexAt(g2d, new Point(row, col), DrawingMode.NORMAL);
                }
            }
            
            if( currentHexCoordinates != null ) {
                drawHexAt(g2d, currentHexCoordinates, DrawingMode.SELECTED_HEX);
            }
        }

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
            }
            
            g2d.fill(hexagons[position.x][position.y].getShape());
            
            // border
            g2d.setColor(Color.BLACK);
            g2d.draw(hexagons[position.x][position.y].getShape());
        }
        
        private boolean isEvenColumn( int col ) {
            return col % 2 == 0;
        }
    }
    
    private enum DrawingMode {
        NORMAL,             // set color from sectorColors map
        SELECTED_HEX        // hover color
    }
    
    public static void main(String[] args) {
        // Run the GUI codes on the EDT for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameMainScreen();
            }
        });
    }

}