package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.common.ResourceLoader;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.sector.Sector;
import it.polimi.ingsw.game.sector.SectorBuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/** Panel where the map is drawn. It is used in {@link GUIFrame} class. 
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class MapCanvasPanel extends JPanel {

    /** Logger */
    private static final Logger LOG = Logger.getLogger( MapCanvasPanel.class.getName() );
    
    /** Serial version */
    private static final long serialVersionUID = -5583245069814214909L;

    /** the map being loaded */
    private GameMap mGameMap;

    /** the array of all drawn mHexagons */
    private transient Hexagon[][] mHexagons;

    /** Selected hex: contains indexes i and j in mHexagons[][] array */
    private Point mHoveringCellCoords;

    /** Values for every hexagon */
    private int mHexWidth;
    private int mHexHeight;
    private int mMarginHeight;

    /** canvas width and height */
    private int mCanvasWidth;
    private int mCanvasHeight;

    /** Set of all sectors available for selection
     *  VERY IMPORTANT THING TO KEEP IN MIND
     *  mEnabledCells == null means that you can select EVERY VALID CELL on map!
     */
    private transient Set<Point> mEnabledCells = new HashSet<>();

    /** Clicked on cell? */
    private boolean mClickedOnCell = false;

    /** Render mutex */
    private final transient Object mRenderLoopMutex = new Object();

    /** Player position */
    private Point mPlayerPosition;

    /** Blinking sectors */
    private Point mNoisePosition = null;

    /** Spotlight sectors */
    private transient Map<String, Point> mSpotlightSectors = null;
    private transient Map<Point, Integer> mFrequenciesForSpotlightPoints = null;

    /** Attack point */
    private Point mAttackPoint = null;

    /** Blinking color */
    private Color mBlinkingColor = null;
    
    /** Game controller */
    private final transient GameController mController;
    
    /** Background */
    private transient Image mBg;

    /** Create the map canvas.
     *
     * @param controller The local game controller
     * @param map The map to be displayed
     * @param canvasWidth The width of this canvas
     * @param canvasHeight The height of this canvas 
     * @param playerPosition The starting position of this player's screen
     */
    public MapCanvasPanel( GameController controller, GameMap map, int canvasWidth, int canvasHeight, Point playerPosition) {
        /** initialization  */
        mController = controller;
        mCanvasWidth = canvasWidth;
        mCanvasHeight = canvasHeight;
        mHexagons = new Hexagon[GameMap.ROWS][GameMap.COLUMNS];
        mGameMap = map;
        mHoveringCellCoords = null;
        mPlayerPosition = playerPosition;

        try {
            mBg = ImageIO.read(ResourceLoader.getInstance().loadResource("img/b3.jpg"));
        } catch( IOException e ) {
            LOG.warning("Cannot load ending images: " + e);
        }
        
        /** add listeners */
        addMouseListeners();

        /** calc width, height and margins according to size */
        calculateValuesForHexagons();

        /** create all hexagons images according to values given by the previous function */
        for( int i = 0; i < GameMap.ROWS; ++i ) {
            for( int j = 0; j < GameMap.COLUMNS; ++j ) {
                initHexagon(mGameMap.getSectorAt(j, i), i, j);
            }
        }
    }

    /** Initialize hexagon in position i, j
     * @param sector The corresponding sector in position
     * @param i The x position
     * @param j The y position
     */
    private void initHexagon(Sector sector, int i, int j) {
        
        int startX = (int)(mHexWidth*3/4.0*j);
        int startY = (int)(mMarginHeight + i * mHexHeight + ( isEvenColumn(j)  ? 0 : mHexHeight/2 ) );

        /** create hexagon: center of it is distant (mHexWidth/2, mHexHeight/2) from the starting point */
        mHexagons[i][j] = HexagonFactory.createHexagon( 
                new Point(startX + mHexWidth/2, startY + mHexHeight/2), mHexWidth/2, sector.getId());
    }

    /** Methods for detecting mouse position and clicking */
    private void addMouseListeners() {
        addMouseListener( new MouseListener() {
            
            /** Mouse pressed */
            @Override
            public void mousePressed(MouseEvent e) {
                /** not used */
            }

            /** Mouse clicked */
            @Override
            public void mouseClicked(MouseEvent arg0) { 
                mClickedOnCell = true;

                if(mHoveringCellCoords != null) {
                    /** VERY IMPORTANT THING TO KEEP IN MIND
                     * mEnabledCells == null means that you can select EVERY VALID CELL on map!
                     *
                     * Here we want to move the player's position when the mEnabledCells set
                     * DOES NOT contains all sectors (because that means we are not moving,
                     * but only choosing a position in noise in any sector or in spotlight state!)
                     */
                    if( mEnabledCells != null ) {
                        mPlayerPosition = mHoveringCellCoords;

                        /** notify that we have chosen a position on map by clicking on it */
                        mController.onMapPositionChosen(mPlayerPosition);
                    } else {
                        mController.onMapPositionChosen(mHoveringCellCoords);
                    }

                    mHoveringCellCoords = null;

                }
            }

            /** Mouse entered */
            @Override
            public void mouseEntered(MouseEvent e) {
                /** not used */
            }

            /** Mouse exited */
            @Override
            public void mouseExited(MouseEvent e) {
                /** not used */
            }

            /** Mouse released */
            @Override
            public void mouseReleased(MouseEvent e) {
                /** not used */
            }
        });

        addMouseMotionListener( new MouseMotionListener() {
            
            /** Mouse moved */
            @Override
            public void mouseMoved(MouseEvent e) {
                Point cell = getCell(e.getPoint());

                synchronized(mRenderLoopMutex) {
                    /** If we can choose every valid sector or if we are on one of the available sectors... */
                    if( mEnabledCells == null || (mEnabledCells != null && mEnabledCells.contains(cell)) )    
                        /** register the current sector we are hovering on */
                        mHoveringCellCoords = cell;
                    else
                        mHoveringCellCoords = null;
                }
            }

            /** Mouse Dragged */
            @Override
            public void mouseDragged(MouseEvent e) { 
                /** not used */
            }
        });
    }


    /** Calculate values (width, height and margins) for mHexagons. */
    private void calculateValuesForHexagons() {
        /** Reference: http://www.redblobgames.com/grids/mHexagons/ */
        mHexWidth = (int)(mCanvasWidth / ( 0.75 * (GameMap.COLUMNS-1) + 1 ));
        mHexHeight =  (int)(mHexWidth * Math.sqrt(3)/2);
        mMarginHeight = (int)((mCanvasHeight - mHexHeight * ( GameMap.ROWS + 0.5 )) / 2.0);
    }

    /** Paint this canvas.
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        /** paint parent's background */
        super.paintComponent(g);     

        Graphics2D g2d = (Graphics2D)g;

        /** set background color for this JPanel */
        setBackground(ColorPalette.BACKGROUND.getColor());  
        
        g.drawImage(mBg, 0, 0, null);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(2f));
        synchronized(mRenderLoopMutex) {
            drawHexagons(g2d);
            
            // write usernames when spotlight is on
            if( mSpotlightSectors != null ) {
                writeUsernamesOnBoard( g2d );
            }
        }
    }

    /** Write usernames when spotlight is invoked
     * @param g2d The graphics controller
     */
    private void writeUsernamesOnBoard(Graphics2D g2d) {
        
        // for each point, get counter
        for (Entry<Point, Integer> freqEntry : mFrequenciesForSpotlightPoints.entrySet())
        {
            Point p = freqEntry.getKey();
            
            int i = 0;
            for (Entry<String, Point> entry : this.mSpotlightSectors.entrySet()) {
                if( entry.getValue().equals(p) ) {
                    mHexagons[p.y][p.x].writeUsername( g2d, entry.getKey(), i );
                    i++;
                }
            }
        }
        
    }

    /** Draw all hexagons, called by paintComponent().
     *
     * @param g2d The Graphics2D object where to draw on
     */
    private void drawHexagons(Graphics2D g2d) { 
        /** for every hexagon.. */
        for( int i = 0; i <= GameMap.ROWS; ++i ) 
            for( int j = 0; j < GameMap.COLUMNS; ++j ) {
                
                Point p = mHoveringCellCoords;
                if(i != GameMap.ROWS)
                    p = new Point(j,i);

                if(p == null)
                    continue;

                /** this is the player position */
                boolean isPlayerHere = p.equals(mPlayerPosition);
                
                /** check if this sector should blink */
                mBlinkingColor = null;
                boolean shouldBlink = shoudBlink(p);

                /** check if this should be enabled */
                boolean enabled;
                if( mEnabledCells != null && !mEnabledCells.contains(p) )
                    enabled = false;
                else
                    enabled = true;

                /** delegate the actual drawing according the values set in this function */
                mHexagons[p.y][p.x].draw(g2d, isPlayerHere, enabled, i == GameMap.ROWS, shouldBlink, mBlinkingColor );
            }
    }
    
    /** Check if this sector should blink for a noise
     *
     * @param p The point to compare with
     * @return True if this should blink
     */
    private boolean shouldDrawNoise( Point p ) {
        if( mNoisePosition != null && p.equals( mNoisePosition ) ) {
            mBlinkingColor = ColorPalette.NOISE.getColor();
            return true;
        } else {
           return false;
        }
    }
    
    /** Check if this sector should blink for a spotlight action
     *
     * @param p The point to compare with
     * @return True if this should blink
     */
    private boolean shouldDrawSpotlight( Point p ) {
        if( mSpotlightSectors != null && mSpotlightSectors.values().contains(p) ) {
            mBlinkingColor = ColorPalette.SPOTLIGHT.getColor();
            return true;
        } else {
            return false;
        }
    }
    
    /** Check if this sector should blink for an attack action
     * 
     * @param p The point to compare with
     * @return True if this should blink
     */
    private boolean shouldDrawAttack(Point p) {
        if( mAttackPoint != null && p.equals( mAttackPoint ) ) {
            mBlinkingColor = ColorPalette.ATTACK.getColor();
            return true;
        } else {
            return false;
        }
    }

    /** Check if a sector should blink (delegates to the other shouldDraw* functions)
     *
     * @param p The point to compare with
     * @return True if this should blink
     */
    private boolean shoudBlink(Point p) {
        return shouldDrawNoise(p) || shouldDrawSpotlight(p) || shouldDrawAttack(p);
    }
 
    /** Checks if column % 2 == 0.
     *
     * @param col the column index
     * @return true, if is even column
     */
    private boolean isEvenColumn( int col ) {
        return col % 2 == 0;
    }

    /** Get the sector where the player clicked
     *
     * @return The point where he clicked
     */
    public Point getChosenMapCell() {
        if(mClickedOnCell) {
            mClickedOnCell = false;
            if( mEnabledCells == null || (mEnabledCells != null && mEnabledCells.contains(mHoveringCellCoords)) )
                return mHoveringCellCoords;
        }
        return null;
    }

    /** Initialize mEnabledCells property for later drawing
     *
     * @param pnt The set to use
     */
    public void setEnabledCells(Set<Point> pnt) {
        synchronized(mRenderLoopMutex) {
            mEnabledCells = pnt;
        }
    }

    /** Get a crossable point at the given point
     *
     * @param p The point 
     * @return The cell corresponding to the given point
     */
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

    /** Set current player position
     *
     * @param point The position of the player
     */
    public void setPlayerPosition(Point point) {
        mPlayerPosition = point;
    }

    /** Set noise position
     *
     * @param p The point where the noise should be drawn
     */
    public void showNoiseInSector(Point p) {
        mNoisePosition = p;
    }

    /** Toggle all blinking sectors to their default colors after 2 seconds */
    public void resetBlinkingElements() {
        java.util.Timer timer = new java.util.Timer();

        long duration = 2*1000L;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mNoisePosition = null;
                mSpotlightSectors = null;
                mAttackPoint = null;
                mFrequenciesForSpotlightPoints = null;
            }
        }, duration);
    }

    /** Set the map of spotlight blinking sectors
     *
     * @param data The map to be used
     */
    public void setSpotlightData(Map<String, Point> data) {
        mSpotlightSectors = data;
        
        Map<Point, Integer> result = new HashMap<Point, Integer>();
        
        for (Entry<String, Point> entry : mSpotlightSectors.entrySet()) {
           Point value = entry.getValue();
           Integer count = result.get(value);
           if (count == null)
              result.put(value, new Integer(1));
           else
              result.put(value, new Integer(count+1));
        }
        
        mFrequenciesForSpotlightPoints = result;
    }

    /** Set the blinking sector for an attack
     *
     * @param p The chosen point 
     */
    public void handleAttack(Point p) {
        mAttackPoint = p;
    }

    /** Change this sector to used hatch
     * @param point The point to change
     */
    public void changeSectorToUsedHatch(Point point) {
        initHexagon(SectorBuilder.getSectorFor(SectorBuilder.USED_HATCH), point.y, point.x);
    }
}
