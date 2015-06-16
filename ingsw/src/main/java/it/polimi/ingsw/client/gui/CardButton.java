package it.polimi.ingsw.client.gui;
import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.game.card.object.ObjectCardBuilder;

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

/** Class representing a card in the upper-right panel of the main GUI screen.
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since 6 Jun 2015
 */
public class CardButton extends JButton {
    
    /** Serial version */
    private static final long serialVersionUID = 1L;

    /** Logger */
    private static final Logger LOG = Logger.getLogger( CardButton.class.getName() );

    /** Type of card */
    private CardButtons mType;
    
    /** Game controller */
    private transient GameController mController;

    /** Visibility */
    private float mAlpha = 1f;

    /** Card id */
    private int mCardId;

    /** Usable card id */
    private int mUsableCardId;

    /** Can be discarded? */
    private boolean mCanBeDiscarded;

    /** Can be used? */
    private boolean mCanBeUsed;


    /** Constructor for creating a new CardButton.
     * Only used once in the main GUI JFrame. Every other change is applied through changeTo() method.
     *
     * @param type The type of object card this button is representing
     * @param c The supervisor through which you can send the commands to the server
     * @param cardId Absolute position ID in the cardPanel
     * @param usableCardId Position ID in the cardPanel among the *usable* cards only
     */
    public CardButton( final CardButtons type, GameController c, int cardId, int usableCardId ) {
        super( );

        /** initialize */
        mController = c;
        mCanBeDiscarded = false;
        mCanBeUsed = false;

        /** set properties according to type */
        changeTo( type, cardId, usableCardId );

        /** add left-click and right-click listeners */
        addMouseListener(new MouseListener() {
            
            /** Mouse clicked
             * 
             * @param e MouseEvent
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if( ((JButton)e.getSource() ).isEnabled() ) {
                    if( mCanBeUsed && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1 && mUsableCardId != -1 ) {
                        /** use card with LEFT CLICK */
                        mController.sendChosenObjectCard( mUsableCardId );
                    } else if (mCanBeDiscarded && SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                        /** discard card with RIGHT CLICK */
                        mController.sendDiscardObjectCard( mCardId );
                    }
                }
            }

            /** Mouse pressed event handler
             *
             * @param e MouseEvent
             */
            @Override
            public void mousePressed(MouseEvent e) { 
                /** unused */
            }
            
            /** Mouse released event handler
             *
             * @param e MouseEvent
             */
            @Override
            public void mouseReleased(MouseEvent e) { 
             /** unused */
            }
            
            /** Mouse entered event handler
             *
             * @param e MouseEvent
             */
            @Override
            public void mouseEntered(MouseEvent e) { 
                /** set half-transparent property to this card */
                if( mCanBeUsed ) {
                    setAlpha(0.5f);
                }
            }
            
            /** Mouse exited event handler
             *
             * @param e MouseEvent
             */
            @Override
            public void mouseExited(MouseEvent e) { 
                /** make this card opaque */
                if( mCanBeUsed ) {
                    setAlpha(1f);
                }
            }
        });
    }

    /** Set properties to this card according to the parameters given
     *
     * @param type The type of object card this button is representing
     * @param id Absolute position ID in the cardPanel
     * @param usableId Position ID in the cardPanel among the *usable* cards only
     */
    public void changeTo(CardButtons type, int id, int usableId) {
        try {
            mType = type;
            
            BufferedImage bImg = ImageIO.read( type.getImageFile() );
            /** change image */
            setIcon( new ImageIcon( bImg ) );

            /** no borders nor margins */
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);

            /** set tooltip only for actual object cards */
            if( type != CardButtons.NULL )
                setToolTipText( ObjectCardBuilder.idToString( type.getId() ) );

            mCardId = id;
            mUsableCardId = usableId;

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot create " + type.toString() + " card button. Please check your assets in img folder.", e);
        }
    }

    /** Get the value of alpha channel set for this card (aka: how transparent it is)
     *
     * @return How opaque it is
     */
    public float getAlpha()
    {
        return mAlpha;
    }

    /** Set a value for the alpha channel and repaint the screen
     * 
     * @param alpha The value of the alpha channel
     */
    public void setAlpha(float alpha)
    {
        mAlpha = alpha;
        repaint();
    }

    /** Repaint method
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        /** apply transparent effect */
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, mAlpha));

        super.paintComponent(g2);
    }

    /** Get type of object card represented 
     *
     * @return Card button
     */
    public CardButtons getType() {
        return mType;
    }

    /** Set whether you can discard this card
     *
     * @param value True if you want to discard it
     */
    public void setCanBeDiscarded(boolean value) {
        mCanBeDiscarded = value;
    }

    /** Set whether you can use this card
     *
     * @param value True if you want to use it
     */
    public void setCanBeUsed(boolean value) {
        mCanBeUsed = value;

        /** Change transparency according to canBeUsed value */
        if( !mCanBeUsed )
            setAlpha(0.5f);
        else
            setAlpha(1f);
    }

}
