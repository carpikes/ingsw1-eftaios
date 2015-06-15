package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.PlayerInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */
class EndingCanvasPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger( EndingCanvasPanel.class.getName() );
    
    private static final long serialVersionUID = 1L;
    private Font mBigFont;
    private Font mSmallFont;
    private Font mMediumFont;
    
    private boolean mIsWinner;
    private List<Integer> mWinnerList;
    private List<Integer> mLoserList;
    private GameInfo mGameInfo;

    public EndingCanvasPanel( GameInfo gameInfo, List<Integer> winnerList, List<Integer> loserList ) {
        
        mGameInfo = gameInfo;
        mIsWinner = winnerList.contains( mGameInfo.getId() );
        mWinnerList = winnerList;
        mLoserList = loserList;
        
        mBigFont = new Font("Helvetica", Font.PLAIN, 48);
        mMediumFont = new Font("Helvetica", Font.PLAIN, 36);
        mSmallFont = new Font("Helvetica", Font.PLAIN, 24);
        
        new Timer(25, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EndingCanvasPanel m = EndingCanvasPanel.this;
                if(m != null)
                    m.repaint();
            }

        }).start();

    }

    /**
     * @param playerInfos 
     * @return 
     * 
     */
    private int writePlayers( Graphics2D g2d, int offsetY, List<Integer> listOfPlayers ) {
        PlayerInfo[] players = mGameInfo.getPlayersList();
        
        for( int id : listOfPlayers ) {
            offsetY = drawRightAligned(g2d, mSmallFont, players[ id ].getUsername(), offsetY + 20);
        }
        
        return offsetY;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background

        Graphics2D g2d = (Graphics2D)g;
        
        setBackground(Color.BLACK);  // set background color for this JPanel
        try {
            Image bg = ImageIO.read(new File("img/ending.jpg"));
            g.drawImage(bg, 0, 0, null);
        } catch( IOException e ) {
            LOG.warning("Cannot load ending images!");
        }
        setForeground(Color.WHITE);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int nextY = 20;
        
        String title = (mIsWinner) ? "YOU WIN!" : "YOU LOSE!";
        
        nextY = drawRightAligned(g2d, mBigFont, title,nextY);
        
        nextY = drawRightAligned(g2d, mMediumFont, "Winners:", nextY + 80);
        nextY = writePlayers( g2d, nextY, mWinnerList );
        
        nextY = drawRightAligned(g2d, mMediumFont, "Losers:", nextY + 40);
        nextY = writePlayers( g2d, nextY, mLoserList );
    }

    private int drawRightAligned(Graphics2D g, Font font, String str, int y) {
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D win = g.getClipBounds();
        Rectangle2D rect = font.getStringBounds(str, frc);

        g.setFont(font);
        g.drawString(str, (int)(win.getWidth() - rect.getWidth()) - 60, (int) rect.getHeight() + y);
        return (int) (rect.getHeight() + y);
    }
}
