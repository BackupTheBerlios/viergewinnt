package de.fhhn.viergewinnt.ui.gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import de.fhhn.viergewinnt.game.*;

public class SwingUI extends JFrame implements MouseListener {
	
	static final int ROWS = 6;
	static final int COLS = 7;
	
	private ImageIcon blankIcon = new ImageIcon("./img/blank.gif");
	private ImageIcon redArrowIcon = new ImageIcon("./img/redarrow.gif");
	private ImageIcon yellowArrowIcon = new ImageIcon("./img/yellowarrow.gif");
	private ImageIcon emptyTokenIcon = new ImageIcon("./img/emptytoken.gif");
	private ImageIcon redTokenIcon = new ImageIcon("./img/redtoken.gif");
	private ImageIcon yellowTokenIcon = new ImageIcon("./img/yellowtoken.gif");

	private JPanel applicationDesktop = new JPanel();
	private JPanel playField = new JPanel();
	
	private JMenuBar mainMenuBar = new JMenuBar();
	private JMenu gameMenu = new JMenu();
	private JMenuItem gameMenuNew = new JMenuItem();
	private JMenuItem gameMenuExit = new JMenuItem();
	private JMenu helpMenu = new JMenu();
	private JMenuItem helpMenuAbout = new JMenuItem();
	
	private JLabel[] insertToken = new JLabel[COLS];
	private JLabel[][] playfieldToken = new JLabel[ROWS][COLS];
	
	private JLabel messageLabel = new JLabel();

	/** Hier werden die MoveListener verwaltet. */
    private EventListenerList listenerList;

    /** Ein Move-Event. */
    private Move move = null;

    /** Farbe des Spielers. */
    private Token playerColor;
	
	SwingUI() {
		super();
		initGUI();
		setVisible(true);
        listenerList = new EventListenerList();
        playerColor = Token.RED; // FIXME
	}
	
	public void initGUI() {
		
		for(int i=0; i<COLS; i++) {
			insertToken[i] = new JLabel();
			insertToken[i].setIcon(blankIcon);
			insertToken[i].addMouseListener(this);
			playField.add(insertToken[i]);
		}
		
		for(int i=ROWS-1; i>=0; i--) {
			for(int j=0; j<COLS; j++) {
				playfieldToken[i][j] = new JLabel();
				playfieldToken[i][j].setIcon(emptyTokenIcon);
				playfieldToken[i][j].addMouseListener(this);
				playField.add(playfieldToken[i][j]);
			}
		}
		
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		setJMenuBar(mainMenuBar);
		setBounds(new java.awt.Rectangle(0, 0, 402, 437));
		setBackground(new java.awt.Color(255, 255, 255));
		setResizable(false);
		setTitle("Vier Gewinnt");
		setIconImage(
			(new javax
				.swing
				.ImageIcon("./img/titleicon.gif"))
				.getImage());
		gameMenu.setText("Spiel");
		gameMenu.add(gameMenuNew);
		gameMenu.add(gameMenuExit);
		mainMenuBar.add(gameMenu);
		mainMenuBar.add(helpMenu);
		gameMenuNew.setText("Neu");
		gameMenuExit.setText("Beenden");
		helpMenu.add(helpMenuAbout);
		helpMenu.setText("Info");
		helpMenuAbout.setText("Info");
		getContentPane().add(applicationDesktop, java.awt.BorderLayout.CENTER);
		applicationDesktop.setLayout(new java.awt.FlowLayout());
		applicationDesktop.setPreferredSize(new java.awt.Dimension(400, 400));
		applicationDesktop.setSize(new java.awt.Dimension(400, 400));
		applicationDesktop.setMinimumSize(new java.awt.Dimension(400, 400));
		applicationDesktop.setMaximumSize(new java.awt.Dimension(32767, 32767));
		applicationDesktop.setBackground(new java.awt.Color(255, 255, 255));
		applicationDesktop.add(playField);
		applicationDesktop.add(messageLabel);
		playField.setLayout(new java.awt.GridLayout(7, 7));
		playField.setMinimumSize(new java.awt.Dimension(357, 306));
		playField.setToolTipText("");
		playField.setSize(new java.awt.Dimension(200, 200));
		playField.setMaximumSize(new java.awt.Dimension(357, 306));
		playField.setBackground(new java.awt.Color(255, 255, 255));
		
		
		// for testing purposes only
		playfieldToken[0][0].setIcon(redTokenIcon);
		playfieldToken[1][2].setIcon(yellowTokenIcon);
		playfieldToken[2][1].setIcon(redTokenIcon);
		playfieldToken[1][1].setIcon(yellowTokenIcon);
		
		
		messageLabel.setText("messageLabel");
		
	}
	
	public void mouseEntered(MouseEvent e) {
		boolean jumpout = false;
		for(int i=ROWS-1; i>=0; i--) {
			for(int j=0; j<COLS; j++) {
				if(e.getSource().equals(playfieldToken[i][j])) {
					insertToken[j].setIcon(redArrowIcon);
					jumpout = true;
				}
				if(jumpout==true) break;
			}
			if(jumpout==true) break;
		}
	}
	
	public void mouseExited(MouseEvent e) {
		boolean jumpout = false;
		for(int i=ROWS-1; i>=0; i--) {
			for(int j=0; j<COLS; j++) {
				if(e.getSource().equals(playfieldToken[i][j])) {
					insertToken[j].setIcon(blankIcon);
					jumpout = true;
				}
				if(jumpout==true) break;
			}
			if(jumpout==true) break;
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		// falls mausklick -> agieren
		boolean jumpout = false;
		for(int i=ROWS-1; i>=0; i--) {
			for(int j=0; j<COLS; j++) {
				if(e.getSource().equals(playfieldToken[i][j])) {
					
					
					messageLabel.setText("Klick @ col. " + j);
                    Move m = new Move(playerColor, j);

					
					jumpout = true;
				}
				if(jumpout==true) break;
			}
			if(jumpout==true) break;
		}
	}
	
	public void mousePressed(MouseEvent e) {
		//
	}
	
	public void mouseReleased(MouseEvent e) {
		//
	}

	public static void main(String[] args) {
		new SwingUI();
	}

	/**Fügt einen MoveListener hinzu. */
    public void addMoveListener(MoveListener listener) {
		listenerList.add(MoveListener.class, listener);
    }

    /** Entfernt einen Move-Listener. */
    public void removeMoveListener(MoveListener listener) {
        listenerList.remove(MoveListener.class, listener);
    }

	// Notify all listeners that have registered interest for
    // notification on this event type.  The event instance
    // is lazily created using the parameters passed into
    // the fire method.

    /** FIXME: Funktioniert das? Besser ChangeEvent? */
	 protected void fireMoveTokenMoved(int column) {
    	 // Guaranteed to return a non-null array
	     Object[] listeners = listenerList.getListenerList();
    	 // Process the listeners last to first, notifying
	     // those that are interested in this event
	     for (int i = listeners.length-2; i>=0; i-=2) {
    	     if (listeners[i] == MoveListener.class) {
        	     // Lazily create the event:
            	 if (move == null)
                	 //move = new Move(this);
                	 move = new Move(playerColor, column);
	             ((MoveListener)listeners[i+1]).tokenMoved();
    	     }
	     }
 	}
}
