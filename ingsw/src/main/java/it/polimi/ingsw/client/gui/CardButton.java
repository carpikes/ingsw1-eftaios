/**
 * 
 */
package it.polimi.ingsw.client.gui;
import it.polimi.ingsw.client.GameController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author Michele
 * @since 6 Jun 2015
 */
public class CardButton extends JButton {

    private static final Logger LOG = Logger.getLogger( CardButton.class.getName() );
    
    private int id;
    private BufferedImage image;
    private GameController controller;
    
    public CardButton( CardButtons type, GameController c ) {
       super( );
       
       controller = c;
       
       try {
           image = ImageIO.read( type.getImageFile() );
           setIcon( new ImageIcon( image ) );
           
           setBorder(BorderFactory.createEmptyBorder());
           setContentAreaFilled(false);
           setEnabled( type.isEnabled() );
           
           this.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                //controller.useCard( getId() );
            }
               
           });
       } catch( IOException e ) {
           LOG.log(Level.SEVERE, "Cannot create " + type.toString() + " card button. Please check your assets in img folder.");
       }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
