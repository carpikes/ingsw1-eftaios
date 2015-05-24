package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */

public class GUIFrame extends JFrame {

    private static final long serialVersionUID = 4286896317209068573L;
    private static final Logger LOG = Logger.getLogger( GUIFrame.class.getName() );
    
    // Constants
    private static final int CANVAS_WIDTH  = 1024;
    private static final int CANVAS_HEIGHT = 768;

    // Drawing canvas
    private MapCanvasPanel canvas;

    /**
     * Instantiates main game screen.
     */
    public GUIFrame() {
        try {
            GameMap map = GameMap.createFromMapFile( new File("maps/galilei.txt") );
            canvas = new MapCanvasPanel( map, CANVAS_WIDTH, CANVAS_HEIGHT );    
        
            canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            this.add(canvas);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);   
            this.pack();              
            this.setTitle("Escape from the Aliens in Outer Space");
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setVisible(true);
        } catch (IOException e) { 
            LOG.log(Level.SEVERE, "Cannot read map file: " + e);
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException | SectorException | NumberFormatException e) {
            LOG.log(Level.SEVERE, "File is not well formatted: " + e);
            System.exit(1);
        }
    }
    
    public static void main(String[] args) {
        // Run the GUI codes on the EDT for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIFrame();
            }
        });
    }

}