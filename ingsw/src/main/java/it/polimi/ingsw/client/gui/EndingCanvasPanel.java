package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.common.GameInfo;
import it.polimi.ingsw.common.PlayerInfo;
import it.polimi.ingsw.common.ResourceLoader;
import it.polimi.ingsw.game.config.Config;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/** The ending canvas with winners and losers stats.
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 24/mag/2015
 */
class EndingCanvasPanel extends JPanel {
    /** Logger */
    private static final Logger LOG = Logger.getLogger( EndingCanvasPanel.class.getName() );
    
    /** Serial id */
    private static final long serialVersionUID = 1L;

    /** Big font */
    private Font mBigFont;

    /** Small font */
    private Font mSmallFont;

    /** Medium font */
    private Font mMediumFont;
    
    /** True if is winner */
    private boolean mIsWinner;

    /** Winner list */
    private transient List<Integer> mWinnerList;

    /** Loser list */
    private transient List<Integer> mLoserList;

    /** Game info container */
    private GameInfo mGameInfo;
    
    /** String to show */
    private String mString;
    
    /** Background image */
    private transient Image mBg;
    
    /** Create the final panel
     * 
     * @param gameInfo The local game info 
     * @param winnerList The list of winners' id
     * @param loserList The list of losers' id
     */
    public EndingCanvasPanel( GameInfo gameInfo, List<Integer> winnerList, List<Integer> loserList ) {
        
        mGameInfo = gameInfo;
        
        /** save lists */
        mIsWinner = winnerList.contains( mGameInfo.getId() );
        mWinnerList = winnerList;
        mLoserList = loserList;
        
        buildPanel();
    }
    
    /** Create the final panel
     * 
     * @param gameInfo The local game info 
     * @param string String to show
     */
    public EndingCanvasPanel( GameInfo gameInfo, String string ) {
        
        mGameInfo = gameInfo;

        mString = string;
        mWinnerList = null;
        mLoserList = null;
        
        buildPanel();
    }   

    /** Initialize this panel */
    private void buildPanel() {
        /** create fonts */
        mBigFont = new Font( Config.DEFAULT_FONT, Font.PLAIN, Config.BIG_FONT_SIZE);
        mMediumFont = new Font( Config.DEFAULT_FONT, Font.PLAIN, Config.MEDIUM_FONT_SIZE);
        mSmallFont = new Font( Config.DEFAULT_FONT, Font.PLAIN, Config.SMALL_FONT_SIZE);
        
        try {
            mBg = ImageIO.read(ResourceLoader.getInstance().loadResource("img/ending.jpg"));
        } catch( IOException e ) {
            LOG.warning("Cannot load ending images: " + e);
        }
    }
    
    /** Write a list of usernames corresponding to the listOfPlayers IDs
     *
     * @param g2d The graphics context
     * @param offsetY The Y offset from where to start 
     * @param listOfPlayers The list of all IDs
     * @return The updated Y offset
     */
    private int writePlayers( Graphics2D g2d, int offsetY, List<Integer> listOfPlayers ) {
        PlayerInfo[] players = mGameInfo.getPlayersList();
        
        int y = offsetY;
        for( int id : listOfPlayers ) {
            y = drawRightAligned(g2d, mSmallFont, players[ id ].getUsername(), y + 20);
        }
        
        return y;
    }

    /** Draw the canvas
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        /** paint parent's background */
        super.paintComponent(g);     

        Graphics2D g2d = (Graphics2D)g;
        
        /** set background color for this JPanel */
        setBackground(Color.BLACK);
        if(mBg != null)
            g.drawImage(mBg, 0, 0, null);
        
        setForeground(Color.WHITE);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int nextY = 20;
        
        if(mWinnerList != null && mLoserList != null) {
            /** The title */
            String title = (mIsWinner) ? "YOU WIN!" : "YOU LOSE!";
            
            nextY = drawRightAligned(g2d, mBigFont, title,nextY);
            
            /** list of winners  */
            nextY = drawRightAligned(g2d, mMediumFont, "Winners:", nextY + 80);
            nextY = writePlayers( g2d, nextY, mWinnerList );
            
            /** list of losers */
            nextY = drawRightAligned(g2d, mMediumFont, "Losers:", nextY + 40);
            nextY = writePlayers( g2d, nextY, mLoserList );
        } else {
            drawRightAligned(g2d, mBigFont, mString, nextY);
        }
    }

    /** Draw a string to the right of the screen
     *
     * @param g The graphics context 
     * @param font The font used
     * @param str The string to display
     * @param y The Y offset
     * @return The updated Y offset
     */
    private int drawRightAligned(Graphics2D g, Font font, String str, int y) {
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D win = g.getClipBounds();
        Rectangle2D rect = font.getStringBounds(str, frc);

        g.setFont(font);
        g.drawString(str, (int)(win.getWidth() - rect.getWidth()) - 60, (int) rect.getHeight() + y);
        return (int) (rect.getHeight() + y);
    }
}
