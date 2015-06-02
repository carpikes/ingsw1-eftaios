package it.polimi.ingsw.client.gui;

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
import java.util.Set;

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
    private Hexagon[][] mHexagons;

    // Selected hex: contains indexes i and j in mHexagons[][] array
    private Point mCurHexCoords;

    // Values for every hexagon
    private int mHexWidth;
    private int mHexHeight;
    private int mMarginHeight;
    
    // canvas width and height
    private int mCanvasWidth;
    private int mCanvasHeight;

    private Set<Point> mEnabledCells = null;

    private boolean mClickedOnCell = false;
    
    private final Object mRenderLoopMutex = new Object();

    private Point mPlayerPosition;
    
    /**
     * Instantiates a new map canvas panel.
     *
     * @param map the map to be drawn
     * @param mCanvasWidth the canvas width
     * @param mCanvasHeight the canvas height
     */
    public MapCanvasPanel( GameMap map, int canvasWidth, int canvasHeight , Point playerPosition) {
        // initialization 
        this.mCanvasWidth = canvasWidth;
        this.mCanvasHeight = canvasHeight;
        mHexagons = new Hexagon[GameMap.ROWS][GameMap.COLUMNS];
        mGameMap = map;
        mCurHexCoords = null;
        mPlayerPosition = playerPosition;
        
        // add listeners
        addMouseListeners();

        // calc width, height and margins according to size
        calculateValuesForHexagons();

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
                    if(mEnabledCells == null || mEnabledCells.contains(cell))    
                        mCurHexCoords = cell;
                    else
                        mCurHexCoords = null;
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
        setBackground(Color.WHITE);  // set background color for this JPanel

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Background
        int x = -mCanvasWidth; // too much?
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.2f));
        while(x < mCanvasWidth) {
            g2d.drawLine(x, 0, x + (int)(Math.cos(Math.PI/360*60)*mCanvasHeight), mCanvasHeight);
            x += 30;
        }
        
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
                Point p = mCurHexCoords;
                if(i != GameMap.ROWS)
                    p = new Point(j,i);
                
                if(p == null)
                    continue;
                
                boolean isPlayerHere = p.equals(mPlayerPosition);
                boolean enabled = mEnabledCells == null || mEnabledCells.contains(p);
                mHexagons[p.y][p.x].draw(g2d, isPlayerHere, enabled, (i == GameMap.ROWS));
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
    
    public Point getChosenMapCell() {
        if(mClickedOnCell) {
            mClickedOnCell = false;
            if(mCurHexCoords != null && (mEnabledCells == null || mEnabledCells.contains(mCurHexCoords)))
                return mCurHexCoords;
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
}