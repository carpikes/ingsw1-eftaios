package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.Timer;

/** Panel where the map is drawn. It is used in {@link GUIFrame} class. 
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class MapCanvasPanel extends JPanel {

    private static final long serialVersionUID = -5583245069814214909L;

    // the map being loaded
    private GameMap mGameMap;

    // the array of all drawn mHexagons
    private transient Hexagon[][] mHexagons;

    // Selected hex: contains indexes i and j in mHexagons[][] array
    private Point mHoveringCellCoords;

    // Values for every hexagon
    private int mHexWidth;
    private int mHexHeight;
    private int mMarginHeight;

    // canvas width and height
    private int mCanvasWidth;
    private int mCanvasHeight;

    // Set of all sectors available for selection
    // VERY IMPORTANT THING TO KEEP IN MIND
    // mEnabledCells == null means that you can select EVERY VALID CELL on map!
    private transient Set<Point> mEnabledCells = new HashSet<>();

    private boolean mClickedOnCell = false;

    private transient final Object mRenderLoopMutex = new Object();

    private Point mPlayerPosition;

    // Blinking sectors
    private Point mNoisePosition = null;
    private Map<String, Point> mSpotlightSectors = null;
    private Point mAttackPoint = null;
    private Color blinkingColor = null;
    
    private transient final GameController mController;

    
    /**
     * Instantiates a new map canvas panel.
     *
     * @param map the map to be drawn
     * @param mCanvasWidth the canvas width
     * @param mCanvasHeight the canvas height
     */
    public MapCanvasPanel( GameController controller, GameMap map, int canvasWidth, int canvasHeight, Point playerPosition) {
        // initialization 
        mController = controller;
        this.mCanvasWidth = canvasWidth;
        this.mCanvasHeight = canvasHeight;
        mHexagons = new Hexagon[GameMap.ROWS][GameMap.COLUMNS];
        mGameMap = map;
        mHoveringCellCoords = null;
        mPlayerPosition = playerPosition;

        // add listeners
        addMouseListeners();

        // calc width, height and margins according to size
        calculateValuesForHexagons();

        // create all hexagons images according to values given by the previous function
        for( int i = 0; i < GameMap.ROWS; ++i ) {
            for( int j = 0; j < GameMap.COLUMNS; ++j ) {
                Sector sector = mGameMap.getSectorAt(j, i);
                int startX = (int)(mHexWidth*3/4.0*j);
                int startY = (int)(mMarginHeight + i * mHexHeight + ( isEvenColumn(j)  ? 0 : mHexHeight/2 ) );

                // create hexagon: center of it is distant (mHexWidth/2, mHexHeight/2) from the starting point
                mHexagons[i][j] = HexagonFactory.createHexagon( 
                        new Point(startX + mHexWidth/2, startY + mHexHeight/2), mHexWidth/2, sector.getId());
            }
        }


        // Repaint the frame at 30fps (more or less)
        new Timer(35, new ActionListener() {

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

                if(mHoveringCellCoords != null) {
                    // VERY IMPORTANT THING TO KEEP IN MIND
                    // mEnabledCells == null means that you can select EVERY VALID CELL on map!

                    // Here we want to move the player's position when the mEnabledCells set
                    // DOES NOT contains all sectors (because that means we are not moving,
                    // but only choosing a position in noise in any sector or in spotlight state!)
                    if( mEnabledCells != null ) {
                        mPlayerPosition = mHoveringCellCoords;

                        // notify that we have chosen a position on map by clicking on it
                        mController.onMapPositionChosen(mPlayerPosition);
                    } else {
                        mController.onMapPositionChosen(mHoveringCellCoords);
                    }

                    mHoveringCellCoords = null;

                }
            }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }
        });

        addMouseMotionListener( new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point cell = getCell(e.getPoint());

                synchronized(mRenderLoopMutex) {
                    // If we can choose every valid sector or if we are on one of the available sectors...
                    if( mEnabledCells == null || (mEnabledCells != null && mEnabledCells.contains(cell)) )    
                        // register the current sector we are hovering on
                        mHoveringCellCoords = cell;
                    else
                        mHoveringCellCoords = null;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) { }
        });
    }


    /**
     * Calculate values for mHexagons.
     */
    private void calculateValuesForHexagons() {
        // Reference: http://www.redblobgames.com/grids/mHexagons/
        mHexWidth = (int)(mCanvasWidth / ( 0.75 * (GameMap.COLUMNS-1) + 1 ));
        mHexHeight =  (int)(mHexWidth * Math.sqrt(3)/2);
        mMarginHeight = (int)((mCanvasHeight - mHexHeight * ( GameMap.ROWS + 0.5 )) / 2.0);
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background

        Graphics2D g2d = (Graphics2D)g;
        setBackground(ColorPalette.BACKGROUND.getColor());  // set background color for this JPanel

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(2f));
        synchronized(mRenderLoopMutex) {
            drawHexagons(g2d);
        }
    }

    /**
     * Draw mHexagons.
     *
     * @param g2d The Graphics2D object where to draw on
     */
    private void drawHexagons(Graphics2D g2d) { 
        for( int i = 0; i <= GameMap.ROWS; ++i ) 
            for( int j = 0; j < GameMap.COLUMNS; ++j ) {
                Point p = mHoveringCellCoords;
                if(i != GameMap.ROWS)
                    p = new Point(j,i);

                if(p == null)
                    continue;

                boolean isPlayerHere = p.equals(mPlayerPosition);
                
                blinkingColor = null;
                boolean shouldBlink = shoudBlink(p);

                boolean enabled;
                if( mEnabledCells != null && !mEnabledCells.contains(p) )
                    enabled = false;
                else
                    enabled = true;

                mHexagons[p.y][p.x].draw(g2d, isPlayerHere, enabled, (i == GameMap.ROWS), shouldBlink, blinkingColor );
            }
    }
    
    private boolean shouldDrawNoise( Point p ) {
        if( mNoisePosition != null && p.equals( mNoisePosition ) ) {
            blinkingColor = ColorPalette.NOISE.getColor();
            return true;
        } else {
           return false;
        }
    }
    
    private boolean shouldDrawSpotlight( Point p ) {
        if( mSpotlightSectors != null && mSpotlightSectors.values().contains(p) ) {
            blinkingColor = ColorPalette.SPOTLIGHT.getColor();
            return true;
        } else {
            return false;
        }
    }
    
    private boolean shouldDrawAttack(Point p) {
        if( mAttackPoint != null && p.equals( mAttackPoint ) ) {
            blinkingColor = ColorPalette.ATTACK.getColor();
            return true;
        } else {
            return false;
        }
    }

    private boolean shoudBlink(Point p) {
        return shouldDrawNoise(p) || shouldDrawSpotlight(p) || shouldDrawAttack(p);
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

    public Point getChosenMapCell() {
        if(mClickedOnCell) {
            mClickedOnCell = false;
            if( mEnabledCells == null || (mEnabledCells != null && mEnabledCells.contains(mHoveringCellCoords)) )
                return mHoveringCellCoords;
        }
        return null;
    }

    public void setEnabledCells(Set<Point> pnt) {
        synchronized(mRenderLoopMutex) {
            mEnabledCells = pnt;
        }
    }

    private Point getCell(Point p) {
        for( int i = 0; i < mHexagons.length; ++i )
            for( int j = 0; j < mHexagons[i].length; ++j )
                if( mHexagons[i][j] != null && mHexagons[i][j].getPath().contains( p ))
                    if(mGameMap.getSectorAt(j, i).isCrossable())
                        return new Point( j, i );
                    else
                        return null;

        return null;
    }

    public void setPlayerPosition(Point point) {
        mPlayerPosition = point;
    }

    /**
     * @param user
     * @param p
     */
    public void showNoiseInSector(String user, Point p) {
        mNoisePosition = p;
    }

    /**
     * 
     */
    public void resetBlinkingElements() {
        java.util.Timer timer = new java.util.Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mNoisePosition = null;
                mSpotlightSectors = null;
                mAttackPoint = null;
            }
        }, 2*1000);
    }

    /**
     * @param data
     */
    public void setSpotlightData(Map<String, Point> data) {
        mSpotlightSectors = data;
    }

    /**
     * @param p
     */
    public void handleAttack(Point p) {
        mAttackPoint = p;
    }
}
