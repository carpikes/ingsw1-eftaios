package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.game.config.Config;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */
class LoginCanvasPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger( LoginCanvasPanel.class.getName() );
    
    private static final long serialVersionUID = 1L;
    private Font mBigFont;
    private Font mSmallFont;
    private int mTime = -1;
    private int mPlayers = 0;

    public LoginCanvasPanel() {
        mBigFont = new Font("Helvetica", Font.PLAIN, 36);
        mSmallFont = new Font("Helvetica", Font.PLAIN, 24);

        new Timer(25, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                LoginCanvasPanel m = LoginCanvasPanel.this;
                if(m != null)
                    m.repaint();
            }

        }).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     // paint parent's background

        Graphics2D g2d = (Graphics2D)g;
        
        setBackground(Color.BLACK);  // set background color for this JPanel
        try {
            Image bg = ImageIO.read(new File("img/loginbg.jpg"));
            g.drawImage(bg, 0, 0, null);
            
            Image logo = ImageIO.read(new File("img/logo.png"));
            g.drawImage(logo, (int)(g2d.getClipBounds().getWidth() - logo.getWidth(null) - 60), 60, null);
        } catch( IOException e ) {
            LOG.warning("Cannot load login images!");
            LOG.log(Level.FINEST, e.toString(), e);
        }
        setForeground(Color.WHITE);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int nextY = Config.HEIGHT*2/3;
        nextY = drawRightAligned(g2d, mBigFont, "Waiting for other players",nextY);
        nextY = drawRightAligned(g2d, mSmallFont, "Players online: " + mPlayers, nextY + 20);

        int dt = mTime - (int)(System.currentTimeMillis()/1000);
        if(mTime >= 0 && dt >= 0)
            nextY = drawRightAligned(g2d, mSmallFont, "Remaining time: " + dt + "s",nextY);
        else
            nextY = drawRightAligned(g2d, mSmallFont, "Starting...",nextY);
    }

    private int drawRightAligned(Graphics2D g, Font font, String str, int y) {
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D win = g.getClipBounds();
        Rectangle2D rect = font.getStringBounds(str, frc);

        g.setFont(font);
        g.drawString(str, (int)(win.getWidth() - rect.getWidth()) - 60, (int) rect.getHeight() + y);
        return (int) (rect.getHeight() + y);
    }

    public void setTime(int remainingTime) {
        mTime = (int) (System.currentTimeMillis()/1000 + remainingTime);
    }
    public void setPlayers(int newPlayers) {
        mPlayers = newPlayers;
    }
}
