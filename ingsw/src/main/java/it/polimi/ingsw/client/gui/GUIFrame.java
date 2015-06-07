package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.EnemyInfo;
import it.polimi.ingsw.game.network.GameStartInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * @author Alain
 * @since 24/mag/2015
 *
 */

public class GUIFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger( GUIFrame.class.getName() );

    private final GameController mController;

    // Constants
    private static final int WIDTH = 1024;
    private static final int WIDTH_LEFT  = 745;
    private static final int WIDTH_RIGHT  = WIDTH - WIDTH_LEFT;
    
    private static final int HEIGHT = 768;
    private static final int HEIGHT_BOTTOM = 50;
    private static final int HEIGHT_TOP = HEIGHT - HEIGHT_BOTTOM;

    private static final int PANEL_MARGIN = 10;
    private static EmptyBorder RIGHT_PANEL_MARGIN = new EmptyBorder(PANEL_MARGIN, PANEL_MARGIN, PANEL_MARGIN, PANEL_MARGIN);
    private static final int CARD_HGAP = 5;
    private static final int CARD_VGAP = 5;
    
    private static final int USERS_HGAP = 5;    

    private static final int CARD_WIDTH = WIDTH_RIGHT - 2 * PANEL_MARGIN - 2 * CARD_HGAP;
    private static final int CARD_HEIGHT = CARD_WIDTH * 745 / 490; // values based on image size

    private static final Insets TEXT_AREA_PADDING = new Insets(5, 5, 5, 5);
    private static final EmptyBorder TEXT_AREA_MARGIN = new EmptyBorder( 10, 0, 10, 0 );

    private static final Dimension mDimensionLeftPanel = new Dimension(WIDTH_LEFT, HEIGHT_TOP);
    private static final Dimension mDimensionRightPanel = new Dimension(WIDTH_RIGHT, HEIGHT_TOP);
    private static final Dimension mDimensionCardPanel = new Dimension(WIDTH_RIGHT, CARD_HEIGHT);
    private static final Dimension mDimensionBottomPanel = new Dimension(WIDTH, HEIGHT_BOTTOM);
    
    // Drawing canvas on the left
    private MapCanvasPanel mMapCanvas;
    private LoginCanvasPanel mLoginCanvas;

    // elements on right panel
    private final int numberOfCardButtons = Config.MAX_NUMBER_OF_OBJ_CARDS + 1;
    private CardButton[] cardButtons = new CardButton[ numberOfCardButtons ];

    private JPanel bottomPanel, rightPanel, cardPanel, actionButtonsPanel;
    private JScrollPane scrollTextAreaPane;

    private JTextArea textArea;
    private JButton btnAttack, btnDrawDangerousCard, btnEndTurn;
    
    private JLabel[] userLabel;

    private GameStartInfo startInfo = null;

    public GUIFrame(GameController controller) {
        super();

        mController = controller;

        setDefaultCloseOperation(EXIT_ON_CLOSE);   
        setTitle("Escape from the Aliens in Outer Space");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        createRightPanel();
        switchToLogin();
    }

    /**
     * 
     */
    private void createBottomPanel() {
     // general settings
        bottomPanel = new JPanel(  );

        bottomPanel.setPreferredSize( mDimensionBottomPanel );
        bottomPanel.setBorder( RIGHT_PANEL_MARGIN );
        bottomPanel.setLayout( new BorderLayout() );
        
        bottomPanel.add( new JLabel("Players in game: "), BorderLayout.WEST );
        
        try {
            EnemyInfo[] players = startInfo.getPlayersList();
            userLabel = new JLabel[ players.length ];
            
            JPanel listOfUsers = new JPanel( new FlowLayout() );
            
            for( int i = 0; i < players.length; ++i ) {
                userLabel[i] = new JLabel( (i+1) + ") " + players[i].getUsername() + " - " + players[i].getNumberOfCards() + " object cards." );
                if( i == startInfo.getId() )
                    userLabel[i].setForeground( Color.RED );
                
                listOfUsers.add( userLabel[i] );
            }
            
            bottomPanel.add( listOfUsers, BorderLayout.CENTER);
            this.add( bottomPanel, BorderLayout.SOUTH );
        } catch( NullPointerException e ) {
            LOG.log(Level.SEVERE, "Cannot load list of enemies!");
        }
    }

    /**
     * 
     */
    private void createRightPanel() {
        // general settings
        rightPanel = new JPanel(  );

        rightPanel.setPreferredSize( mDimensionRightPanel );
        rightPanel.setBorder( RIGHT_PANEL_MARGIN );
        rightPanel.setLayout( new BorderLayout() );

        // card panel
        cardPanel = createCardPanel(rightPanel);
        rightPanel.add(cardPanel, BorderLayout.NORTH);

        // Message Area
        scrollTextAreaPane = createMessageArea();
        rightPanel.add(scrollTextAreaPane, BorderLayout.CENTER);

        // Action buttons panel 
        actionButtonsPanel = createActionPanel();
        rightPanel.add(actionButtonsPanel, BorderLayout.SOUTH);

        // add all this to parent's frame
        this.add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createActionPanel() {
        JPanel actionButtonsPanel = new JPanel( new GridLayout(2,2) );

        btnAttack = new JButton("Attack");
        btnAttack.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mController.attack();
            }
        });

        btnDrawDangerousCard = new JButton("Dangerous Card");
        btnDrawDangerousCard.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mController.drawDangerousCard();
            }
        });

        btnEndTurn = new JButton("End Turn");
        btnEndTurn.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mController.endTurn();
            }
        });

        actionButtonsPanel.add( btnAttack );
        actionButtonsPanel.add( btnDrawDangerousCard );
        actionButtonsPanel.add( btnEndTurn );

        return actionButtonsPanel;
    }

    private JScrollPane createMessageArea() {
        textArea = new JTextArea( 5, 20 );
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setMargin( TEXT_AREA_PADDING );

        JScrollPane scrollPane = new JScrollPane(textArea); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder( TEXT_AREA_MARGIN );

        return scrollPane;
    }

    private JPanel createCardPanel(JPanel rightPanel) {
        GridLayout layout = new GridLayout(0, 2);

        layout.setHgap( CARD_HGAP );
        layout.setVgap( CARD_VGAP );

        JPanel cardPanel = new JPanel( layout ); // set 2 card per line, "unlimited" number of rows
        cardPanel.setPreferredSize(mDimensionCardPanel);

        for( int i = 0; i < numberOfCardButtons; ++i ) {
            cardButtons[i] = new CardButton( CardButtons.NULL, mController );
            cardPanel.add( cardButtons[i] );
        }

        return cardPanel;
    }

    public void setRemainingTime(int remainingTime) {
        if(mLoginCanvas != null)
            mLoginCanvas.setTime(remainingTime);
    }
    public void setPlayers(int newPlayers) {
        if(mLoginCanvas != null)
            mLoginCanvas.setPlayers(newPlayers);
    }

    private void switchToLogin() {
        mLoginCanvas = new LoginCanvasPanel( );
        mLoginCanvas.setPreferredSize(mDimensionLeftPanel);

        add(mLoginCanvas, BorderLayout.CENTER);
    }

    public void switchToMap(GameMap map, Point startingPoint) {
        if(mLoginCanvas == null)
            throw new RuntimeException("Map is already loaded");
        try {
            mMapCanvas = new MapCanvasPanel(mController, map, WIDTH_LEFT, HEIGHT_TOP, startingPoint);
            mMapCanvas.setPreferredSize(mDimensionLeftPanel);
        } catch (ArrayIndexOutOfBoundsException | SectorException | NumberFormatException e) {
            LOG.log(Level.SEVERE, "File is not well formatted: " + e);
            System.exit(1);
        }

        remove(mLoginCanvas);
        add(mMapCanvas, BorderLayout.CENTER);
        mLoginCanvas = null;
        
        createBottomPanel();
        
        validate();
        repaint();
    }

    public Point getChosenMapCell() {
        return mMapCanvas.getChosenMapCell();
    }

    public void enableMapCells(Set<Point> pnt) {
        mMapCanvas.setEnabledCells(pnt);
    }

    public void resetChosenMapCell() {
        mMapCanvas.setEnabledCells(null);
    }

    public void setPlayerPosition(Point startingPoint) {
        mMapCanvas.setPlayerPosition(startingPoint);
    }

    /**
     * @param user
     * @param message
     */
    public void showInfo(String user, String message) {
        if( user != null )
            textArea.append( String.format("%s: %s\n\n", user, message) );
        else 
            textArea.append( String.format("-- INFO --: %s\n\n", message) );
    }
    
    /**
     * @param container
     */
    public void setStartInfo(GameStartInfo container) {
        startInfo  = container;
    }

}

