package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameController;
import it.polimi.ingsw.exception.GUIException;
import it.polimi.ingsw.exception.SectorException;
import it.polimi.ingsw.game.GameMap;
import it.polimi.ingsw.game.common.GameInfo;
import it.polimi.ingsw.game.common.PlayerInfo;
import it.polimi.ingsw.game.config.Config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.swing.text.DefaultCaret;

/** Main Frame of the game. This is where all the GUI stuff takes place.
 * @author Alain
 * @since 24/mag/2015
 *
 */

public class GUIFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger( GUIFrame.class.getName() );

    private final transient GameController mController;

    // Constants
    private static EmptyBorder RIGHT_PANEL_MARGIN = new EmptyBorder(Config.PANEL_MARGIN, Config.PANEL_MARGIN, Config.PANEL_MARGIN, Config.PANEL_MARGIN);
    private static final Insets TEXT_AREA_PADDING = new Insets(5, 5, 5, 5);
    private static final EmptyBorder TEXT_AREA_MARGIN = new EmptyBorder( 10, 0, 10, 0 );

    private static final Dimension mDimensionLeftPanel = new Dimension(Config.WIDTH_LEFT, Config.HEIGHT_TOP);
    private static final Dimension mDimensionRightPanel = new Dimension(Config.WIDTH_RIGHT, Config.HEIGHT_TOP);
    private static final Dimension mDimensionCardPanel = new Dimension(Config.WIDTH_RIGHT, Config.CARD_HEIGHT);
    private static final Dimension mDimensionBottomPanel = new Dimension(Config.WIDTH, Config.HEIGHT_BOTTOM);

    // Drawing canvas on the left
    private MapCanvasPanel mMapCanvas;
    private LoginCanvasPanel mLoginCanvas;
    private EndingCanvasPanel mEndingCanvas;
    
    // elements on right panel
    private static final int NUMBER_OF_CARD_BUTTONS = Config.MAX_NUMBER_OF_OBJ_CARDS + 1;
    private CardButton[] cardButtons = new CardButton[ NUMBER_OF_CARD_BUTTONS ];

    private JPanel bottomPanel, rightPanel, cardPanel, actionButtonsPanel;
    private JScrollPane scrollTextAreaPane;

    private JTextArea textArea;
    private JButton btnAttack, btnDrawDangerousCard, btnEndTurn;

    private JLabel[] userLabel;

    private GameInfo gameInfo = null;

    /** Create the main frame
     * @param controller The local game controller
     */
    public GUIFrame(GameController controller) {
        super();

        mController = controller;

        setDefaultCloseOperation(EXIT_ON_CLOSE);   
        setTitle("Escape from the Aliens in Outer Space");
        setSize(Config.WIDTH, Config.HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        createRightPanel();
        switchToLogin();
    }


    /** Create player's info panel
     * 
     */
    private void createBottomPanel() {
        // general settings
        bottomPanel = new JPanel(  );

        bottomPanel.setPreferredSize( mDimensionBottomPanel );
        bottomPanel.setBorder( RIGHT_PANEL_MARGIN );
        bottomPanel.setLayout( new BorderLayout() );

        bottomPanel.add( new JLabel( gameInfo.getPlayersList().length + " players in game: "), BorderLayout.WEST );

        PlayerInfo[] players = gameInfo.getPlayersList();
        userLabel = new JLabel[ players.length ];

        FlowLayout flow = new FlowLayout();
        flow.setHgap( Config.USERS_HGAP );
        JPanel listOfUsers = new JPanel( flow );
        
        for( int i = 0; i < players.length; ++i ) {
            userLabel[i] = new JLabel(  );
        }
        
        updateLabels( );
        
        for( int i = 0; i < players.length; ++i ) {
            listOfUsers.add( userLabel[i] );
        }
        
        bottomPanel.add( listOfUsers, BorderLayout.CENTER);
        this.add( bottomPanel, BorderLayout.SOUTH );
    }

    
    /** Update all player info labels
     *  
     */
    private void updateLabels() {
        PlayerInfo[] players = gameInfo.getPlayersList();
        
        for( int i = 0; i < players.length; ++i ) {
            createTextForLabel(i);
        }
    }

    /** Create text for i-th player label
     * @param i The index of the player
     */
    private void createTextForLabel(int i) {
        PlayerInfo[] players = gameInfo.getPlayersList();

        String txt = "[" + (i+1) + "] ";

        if( i == gameInfo.getId() ) {
            // Red foreground for current player
            userLabel[i].setForeground( Color.RED );
            txt += (gameInfo.isHuman()) ? "** HUMAN ** " : "** ALIEN ** ";
        } else {
            userLabel[i].setForeground( Color.BLACK );
        }

        txt +=  players[i].getUsername() + " - " + players[i].getNumberOfCards() + " object cards";

        userLabel[i].setText( txt );
    }

    /** Create right panel with cards, text area and action buttons
     * 
     */
    private void createRightPanel() {
        // general settings
        rightPanel = new JPanel(  );

        rightPanel.setPreferredSize( mDimensionRightPanel );
        rightPanel.setBorder( RIGHT_PANEL_MARGIN );
        rightPanel.setLayout( new BorderLayout() );

        // card panel
        cardPanel = createCardPanel();
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

    /** Create action button panel (the one below the text area)
     * @return
     */
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel( new GridLayout(2,2) );

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
                resetViewStatus();
                mController.endTurn();
            }
        });

        actionPanel.add( btnAttack );
        actionPanel.add( btnDrawDangerousCard );
        actionPanel.add( btnEndTurn );

        return actionPanel;
    }

    /** Create the text area and set some default properties
     * @return The text area
     */
    private JScrollPane createMessageArea() {
        textArea = new JTextArea( 5, 20 );
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setMargin( TEXT_AREA_PADDING );

        JScrollPane scrollPane = new JScrollPane(textArea); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollPane.setBorder( TEXT_AREA_MARGIN );

        return scrollPane;
    }

    /** Create the panel containing the 4 object cards
     * @return The create JPanel
     */
    private JPanel createCardPanel( ) {
        GridLayout layout = new GridLayout(0, 2);

        layout.setHgap( Config.CARD_HGAP );
        layout.setVgap( Config.CARD_VGAP );

        JPanel cardTempPanel = new JPanel( layout ); // set 2 card per line, "unlimited" number of rows
        cardTempPanel.setPreferredSize(mDimensionCardPanel);

        for( int i = 0; i < NUMBER_OF_CARD_BUTTONS; ++i ) {
            cardButtons[i] = new CardButton( CardButtons.NULL, mController, i, -1 );
            cardTempPanel.add( cardButtons[i] );
        }

        return cardTempPanel;
    }

    /** Set remaining time in the login screen 
     * @param remainingTime The remaining time
     */
    public void setRemainingTime(int remainingTime) {
        if(mLoginCanvas != null)
            mLoginCanvas.setTime(remainingTime);
    }
    
    /** Set number of players in the login screen
     * @param newPlayers
     */
    public void setPlayers(int newPlayers) {
        if(mLoginCanvas != null)
            mLoginCanvas.setPlayers(newPlayers);
    }

    /** Add login canvas with the right dimensions
     * 
     */
    private void switchToLogin() {
        mLoginCanvas = new LoginCanvasPanel( );
        mLoginCanvas.setPreferredSize(mDimensionLeftPanel);

        if( rightPanel != null )
            rightPanel.setVisible(false);

        add(mLoginCanvas, BorderLayout.CENTER);
    }

    
    /** Disable login screen and move to the actual game screen
     * @param map The map to load
     * @param startingPoint The starting point of the player
     */
    public void switchToMap(GameMap map, Point startingPoint) {
        if(mLoginCanvas == null)
            throw new GUIException("Map is already loaded");
        try {
            mMapCanvas = new MapCanvasPanel(mController, map, Config.WIDTH_LEFT, Config.HEIGHT_TOP, startingPoint);
            mMapCanvas.setPreferredSize(mDimensionLeftPanel);

            remove(mLoginCanvas);
            add(mMapCanvas, BorderLayout.CENTER);
            mLoginCanvas = null;

            if( rightPanel != null )
                rightPanel.setVisible( true );

            createBottomPanel();

            resetViewStatus();

            // It won't change anything otherwise!
            validate();
            repaint();
        } catch (ArrayIndexOutOfBoundsException | SectorException | NumberFormatException e) {
            LOG.log(Level.SEVERE, "File is not well formatted: " + e);
            mController.stop();
        }
    }

    /** Calls underlying map canvas function
     * @return The chosen point
     */
    public Point getChosenMapCell() {
        return mMapCanvas.getChosenMapCell();
    }

    /** Calls underlying map canvas function
     * @param The set of points to choose among
     */
    public void enableMapCells(Set<Point> pnt) {
        mMapCanvas.setEnabledCells(pnt);
    }

    /** Calls underlying map canvas function
     * @param startingPoint The starting point of the player
     */
    public void setPlayerPosition(Point startingPoint) {
        mMapCanvas.setPlayerPosition(startingPoint);
    }

    /** Display info in the text area
     * @param user The player who sent this message( -- INFO -- if this is null )
     * @param message The message sent
     */
    public void showInfo(String user, String message) {
        if( user != null )
            textArea.append( String.format("%s: %s%n%n", user, message) );
        else 
            textArea.append( String.format("-- INFO --: %s%n%n", message) );
    }

    /** Set game info about players' usernames and number of cards
     * @param container The source of data
     */
    public void setGameInfo(GameInfo container) {
        gameInfo  = container;
    }

    /** Enable card buttons if you can use them (you're not an alien and you're not selecting a defense card)
     * @param value The value to set
     */
    public void setCanSelectObjCard(boolean value) {
        for( CardButton btn : cardButtons ) {
            if( value ) {
                btn.setCanBeUsed( btn.getType().isEnabled() );
            } else {
                btn.setCanBeUsed( false );
            }
        } 
    }

    /** Enable attack button
     * @param value The value to use
     */
    public void setAttackEnabled(boolean value) {
        btnAttack.setEnabled( value );
    }

    /** Enable right click action on card buttons
     * @param value The value to use
     */
    public void setDiscardObjCardEnabled(boolean value) {
        for( CardButton btn : cardButtons ) {
            btn.setCanBeDiscarded( value );
        }
    }

    /** Enable End Turn button
     * @param value The value to use
     */
    public void setEndTurnEnabled(boolean value) {
        btnEndTurn.setEnabled( value );
    }

    /** Enable draw dangerous card button
     * @param value The value to use
     */
    public void setCanDrawDangerousCard(boolean value) {
        btnDrawDangerousCard.setEnabled( value );
    }

    /** Reset all buttons and disable map selection (used at the beginning of every player state)
     * 
     */
    public void resetViewStatus() {
        enableMapCells( new HashSet<Point>() );
        setCanSelectObjCard( false );
        setAttackEnabled( false );
        setDiscardObjCardEnabled( false );
        setCanDrawDangerousCard( false );
        setEndTurnEnabled( false );
    }

    /** Update card button info's on every change
     * @param listOfCards A list of the card possessed by this player
     */
    public void notifyObjectCardListChange(List<Integer> listOfCards) {
        int usableIndex = 0;
        for( int i = 0; i < GUIFrame.NUMBER_OF_CARD_BUTTONS; ++i ) {
            try {
                int idCard = listOfCards.get(i);

                for( CardButtons btn : CardButtons.values() ) {
                    if( btn.getId() == idCard ) {
                        cardButtons[i].changeTo( btn, i, ( !btn.isEnabled() ) ? -1 : usableIndex++ );
                    }
                }
            } catch( IndexOutOfBoundsException e ) {
                LOG.log(Level.FINER, e.toString(), e);
                cardButtons[i].changeTo( CardButtons.NULL, i, -1 );
            }
        }
    }

    /** Delegate to map canvas underlying function 
     * @param user The user who did this action
     * @param p The point where the action took place
     */
    public void showNoiseInSector(String user, Point p) {
        showInfo(user, "NOISE IN SECTOR " + gameInfo.getMap().pointToString(p));
        mMapCanvas.showNoiseInSector(user, p);
    }

    /** Delegate to map canvas underlying function 
     * 
     */
    public void resetBlinkingElements() {
        mMapCanvas.resetBlinkingElements();
    }

    /** Update player info for the specified player
     * @param idPlayer The id of the player
     */
    public void updatePlayerInfoDisplay(int idPlayer) {        
        this.createTextForLabel( idPlayer );
    }

    /** Remove all elements and show ending screen
     * @param winnerList The list of all winners' id
     * @param loserList The list of all losers' id
     */
    public void showEnding(List<Integer> winnerList, List<Integer> loserList) {
        mEndingCanvas = new EndingCanvasPanel(gameInfo, winnerList, loserList);
        mEndingCanvas.setPreferredSize(mDimensionLeftPanel);

        // SWING FUCK*NG BUG: DO NOT USE REMOVE ALL()!
        this.remove( mMapCanvas );
        this.remove( bottomPanel );
        this.remove( rightPanel );
        
        this.add( mEndingCanvas, BorderLayout.CENTER );
        
        validate();
        repaint();
    }

    /** Delegate to map canvas underlying function 
     * @param data The map of usernames - positions 
     */
    public void setSpotlightData(Map<String, Point> data) {
        mMapCanvas.setSpotlightData( data );
    }

    /** Delegate to map canvas underlying function 
     * @param p The point where to attack
     */
    public void handleAttack(Point p) {
        mMapCanvas.handleAttack( p );
    }

}

