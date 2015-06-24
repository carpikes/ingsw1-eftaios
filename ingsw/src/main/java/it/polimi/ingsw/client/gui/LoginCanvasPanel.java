package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.common.ResourceLoader;
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/** The login canvas 
 *
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 24/mag/2015
 */
class LoginCanvasPanel extends JPanel {
    /** Logger */
    private static final Logger LOG = Logger.getLogger( LoginCanvasPanel.class.getName() );
    
    /** Login BG */
    private static final String LOGINBG = "img/loginbg.jpg";
    private static final String LOGO = "img/logo.png";
    
    /** Serial version */
    private static final long serialVersionUID = 1L;

    /** Big font */
    private Font mBigFont;

    /** Small font */
    private Font mSmallFont;

    /** Time */
    private int mTime = -1;

    /** Players */
    private int mPlayers = 0;

    /** Constructor */
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

    /** Paint this component
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
        try {
            
            Image bg = ImageIO.read(ResourceLoader.getInstance().loadResource(LOGINBG));
            g.drawImage(bg, 0, 0, null);
            
            Image logo = ImageIO.read(ResourceLoader.getInstance().loadResource(LOGO));
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

    /** Set remaining time 
     *
     * @param remainingTime The time to use
     */
    public void setTime(int remainingTime) {
        mTime = (int) (System.currentTimeMillis()/1000 + remainingTime);
    }
    
    /** Set number of players connected to this game
     *
     * @param newPlayers The number of players
     */
    public void setPlayers(int newPlayers) {
        mPlayers = newPlayers;
    }
}
