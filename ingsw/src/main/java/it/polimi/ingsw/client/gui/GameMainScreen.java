package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.Sectors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
        canvas = new MapCanvasPanel( GameMap.createFromMapFile( new File("maps/fermi.txt") ) );    
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
        private Shape[][] hexagons;

        // Color bindings
        private Map<Integer, Color> sectorColors;

        // Selected hex
        private Shape currentHexagon;

        // Values for every hexagon
        private double hexWidth;
        private double hexHeight;
        private double marginHeight;

        public MapCanvasPanel( GameMap map ) {
            // initialization 
            hexagons = new Polygon[GameMap.ROWS][GameMap.COLUMNS];
            gameMap = map;
            currentHexagon = null;

            // methods for detecting mouse position
            this.addMouseMotionListener( new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

            });

            // calc width, height and margins according to size
            calculateValuesForHexagons();

            // set colors for every sector
            createSectorColorsMap();
        }

        private void createSectorColorsMap() {
            sectorColors = new HashMap<>();   

            sectorColors.put(Sectors.ALIEN, new Color(0,0,0));
            sectorColors.put(Sectors.DANGEROUS, new Color(50,0,0));
            sectorColors.put(Sectors.NOT_DANGEROUS, new Color(100,0,0));
            sectorColors.put(Sectors.HATCH, new Color(150,0,0));
            sectorColors.put(Sectors.HUMAN, new Color(200,0,0));
            sectorColors.put(Sectors.NOT_VALID, new Color(245,0,0));
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

            // default position is (0, marginHeight), in order to align vertically all the hexagons
            g2d.translate(0, marginHeight);
            AffineTransform t = g2d.getTransform();

            drawHexagons(g2d, t);
        }

        private void drawHexagons(Graphics2D g2d,
                AffineTransform defaultTransformation) {
            Sector currentSector;

            // Draw columns first since it's easier to do
            for( int col = 0; col < GameMap.COLUMNS; ++col ) {
                // reset axis to first hex in column
                g2d.setTransform(defaultTransformation);
                g2d.translate(hexWidth*3/4*col, ( isEvenColumn(col) ) ? 0 : hexHeight/2);

                for( int row = 0; row < GameMap.ROWS; ++ row ) {
                    /* create current shape clip: every time the same point (translation of x and y axis only) */
                    Shape s = HexagonFactory.createHexagon(new Point2D.Double(hexWidth/2, hexHeight/2), hexWidth/2);

                    /* set color according to type */
                    currentSector = gameMap.getSectorAt(row, col);
                    g2d.setColor( sectorColors.get( currentSector.getId() ) );
                    g2d.fill(s);

                    g2d.setColor(Color.BLACK);
                    g2d.draw(s);

                    /* translate axis */
                    g2d.translate(0.0, hexHeight);
                }
            }
        }
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

    private static boolean isEvenColumn( int col ) {
        return col % 2 == 0;
    }
}