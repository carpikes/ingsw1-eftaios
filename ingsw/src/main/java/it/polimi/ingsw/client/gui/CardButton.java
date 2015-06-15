package it.polimi.ingsw.client.gui;
import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;

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

/** Class representing a card in the upper-right panel of the main GUI screen.
 * @author Michele Albanese <michele.albanese93@gmail.com>
 * @since 6 Jun 2015
 */
public class CardButton extends JButton {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger( CardButton.class.getName() );

    private CardButtons type;
    private transient GameController controller;

    private float alpha = 1f;

    private int mCardId;
    private int mUsableCardId;
    private boolean canBeDiscarded;
    private boolean canBeUsed;


    /** Constructor for creating a new CardButton. Only used once in the main GUI JFrame. Every other change is applied through changeTo() method.
     * @param type The type of object card this button is representing
     * @param c The supervisor through which you can send the commands to the server
     * @param cardId Absolute position ID in the cardPanel
     * @param usableCardId Position ID in the cardPanel among the *usable* cards only
     */
    public CardButton( final CardButtons type, GameController c, int cardId, int usableCardId ) {
        super( );

        // initialize
        controller = c;
        canBeDiscarded = false;
        canBeUsed = false;

        // set properties according to type
        changeTo( type, cardId, usableCardId );

        // add left-click and right-click listeners
        this.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if( ((JButton)e.getSource() ).isEnabled() ) {
                    if( canBeUsed && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1 && mUsableCardId != -1 ) {
                        // use card with LEFT CLICK
                        controller.sendChosenObjectCard( mUsableCardId );
                    } else if (canBeDiscarded && SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                        // discard card with RIGHT CLICK
                        controller.sendDiscardObjectCard( mCardId );
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { 
                // unused
            }
            
            @Override
            public void mouseReleased(MouseEvent e) { 
             // unused
            }
            
            @Override
            public void mouseEntered(MouseEvent e) { 
                // set half-transparent property to this card
                if( canBeUsed ) {
                    setAlpha(0.5f);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) { 
                // make this card opaque
                if( canBeUsed ) {
                    setAlpha(1f);
                }
            }
        });
    }

    /** Set properties to this card according to the parameters given
     * @param type The type of object card this button is representing
     * @param cardId Absolute position ID in the cardPanel
     * @param usableCardId Position ID in the cardPanel among the *usable* cards only
     */
    public void changeTo(CardButtons type, int id, int usableId) {
        try {
            this.type = type;
            
            // change image
            setIcon( new ImageIcon( ImageIO.read( type.getImageFile() ) ) );

            // no borders nor margins
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);

            // set tooltip only for actual object cards
            if( type != CardButtons.NULL )
                setToolTipText( ObjectCardBuilder.idToString( type.getId() ) );

            mCardId = id;
            mUsableCardId = usableId;

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot create " + type.toString() + " card button. Please check your assets in img folder.", e);
        }
    }

    /** Get the value of alpha channel set for this card (aka: how transparent it is)
     * @return How opaque it is
     */
    public float getAlpha()
    {
        return alpha;
    }

    /** Set a value for the alpha channel and repaint the screen
     * @param alpha The value of the alpha channel
     */
    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
        repaint();
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        // apply transparent effect
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        super.paintComponent(g2);
    }

    /** Get type of object card represented 
     * @return
     */
    public CardButtons getType() {
        return type;
    }

    /** Set whether you can discard this card
     * @param value True if you want to discard it
     */
    public void setCanBeDiscarded(boolean value) {
        canBeDiscarded = value;
    }

    /** Set whether you can use this card
     * @param value True if you want to use it
     */
    public void setCanBeUsed(boolean value) {
        canBeUsed = value;

        // Change transparency according to canBeUsed value
        if( !canBeUsed )
            setAlpha(0.5f);
        else
            setAlpha(1f);
    }

}
