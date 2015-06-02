package it.polimi.ingsw.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */
class LoginCanvasPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Font mBigFont;
    private Font mSmallFont;
    private int mTime = -1;
    private int mPlayers = 0;
    
    public LoginCanvasPanel() {
        mBigFont = new Font("Helvetica", Font.PLAIN, 48);
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
        setBackground(Color.WHITE);  // set background color for this JPanel

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int nextY = 0;
        nextY = drawCentered(g2d, mBigFont, "Waiting for other players",nextY);
        nextY = drawCentered(g2d, mSmallFont, "Players online: " + mPlayers, nextY + 20);
        
        int dt = (int) (mTime - (int)(System.currentTimeMillis()/1000));
        if(mTime >= 0 && dt >= 0)
            nextY = drawCentered(g2d, mSmallFont, "Remaining time: " + dt + "s",nextY);
        else
            nextY = drawCentered(g2d, mSmallFont, "Starting...",nextY);
    }
    
    private int drawCentered(Graphics2D g, Font font, String str, int y) {
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D win = g.getClipBounds();
        Rectangle2D rect = font.getStringBounds(str, frc);
        
        g.setFont(font);
        g.drawString(str, (int)(win.getWidth() - rect.getWidth())/2 , (int) rect.getHeight() + y);
        return (int) (rect.getHeight() + y);
    }
    
    public void setTime(int remainingTime) {
        mTime = (int) (System.currentTimeMillis()/1000 + remainingTime);
    }
    public void setPlayers(int newPlayers) {
        mPlayers = newPlayers;
    }
}
