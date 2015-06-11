/**
 * 
 */
package it.polimi.ingsw.client.gui;
import it.polimi.ingsw.client.GameController;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * @author Michele
 * @since 6 Jun 2015
 */
public class CardButton extends JButton {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger( CardButton.class.getName() );

    private CardButtons type;
    private transient GameController controller;

    private float alpha = 1f;

    private int myId;
    private boolean canBeDiscarded;
    private boolean canBeUsed;

    public CardButton( final CardButtons type, GameController c, int id) {
        super( );

        controller = c;
        this.type = type;
        myId = id;
        
        canBeDiscarded = false;
        canBeUsed = false;
           
        changeTo( type );

        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if( ((JButton)e.getSource() ).isEnabled() ) {
                    if( canBeUsed && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                        controller.sendChosenObjectCard( myId );
                    } else if (canBeDiscarded && SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                        controller.sendDiscardObjectCard( myId );
                    }
                }
            }

            public void mousePressed(MouseEvent e) { }

            public void mouseReleased(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { 
                if( canBeUsed || canBeDiscarded ) {
                    setAlpha(0.5f);
                }
            }

            public void mouseExited(MouseEvent e) { 
                if( canBeUsed || canBeDiscarded ) {
                    setAlpha(1f);
                }
            }
        });
    }

    public void changeTo(CardButtons type) {
        try {
            this.type = type;
            setIcon( new ImageIcon( ImageIO.read( type.getImageFile() ) ) );

            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);
            setEnabled( type.isEnabled() );
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot create " + type.toString() + " card button. Please check your assets in img folder.", e);
        }
    }

    public float getAlpha()
    {
        return alpha;
    }

    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2);
    }

    public CardButtons getType() {
        return type;
    }

    /**
     * @param value
     */
    public void setCanBeDiscarded(boolean value) {
        canBeDiscarded = value;
    }
    
    public void setCanBeUsed(boolean value) {
        canBeUsed = value;
    }

}
