package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */

class Lol extends JPanel {
    private Font mBigFont;
    private Font mSmallFont;
    private int mTime = -1;
    private int mPlayers = 0;
    
    public Lol() {
        mBigFont = new Font("Helvetica", Font.PLAIN, 48);
        mSmallFont = new Font("Helvetica", Font.PLAIN, 24);
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
        
        repaint();
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

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private Lol canvas;
    
    public MainFrame() {
        canvas = new Lol();    
        Dimension d = new Dimension(800,600);
        canvas.setPreferredSize(d);
        canvas.setMinimumSize(d);
        this.add(canvas);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);   
        this.pack();              
        this.setTitle("Escape from the Aliens in Outer Space");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    public void setRemainingTime(int remainingTime) {
        canvas.setTime(remainingTime);
    }
    public void setPlayers(int newPlayers) {
        canvas.setPlayers(newPlayers);
    }
}
