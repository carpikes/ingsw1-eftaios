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
import java.awt.image.BufferedImage;
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

    private static final Logger LOG = Logger.getLogger( CardButton.class.getName() );
    
    private int id;
    private BufferedImage image;
    private GameController controller;

    private float alpha = 1f;
    
    public CardButton( final CardButtons type, GameController c ) {
        super( );

        controller = c;
        changeTo( type );
                
        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if( SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    controller.sendChosenObjectCard( type.getId() );
                } else if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                   //controller.discardObjectCard( type.getId() );
                }
            }

            public void mousePressed(MouseEvent e) { }

            public void mouseReleased(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { 
                setAlpha(0.5f);
            }

            public void mouseExited(MouseEvent e) { 
                setAlpha(1f);
                
            }
        });
    }

    public void changeTo(CardButtons type) {
        try {
            image = ImageIO.read( type.getImageFile() );
            setIcon( new ImageIcon( image ) );
            
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);
            setEnabled( type.isEnabled() );
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot create " + type.toString() + " card button. Please check your assets in img folder.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
