package de.fhhn.viergewinnt.ui.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import de.fhhn.viergewinnt.game.*;
import de.fhhn.viergewinnt.ui.*;

/**
 * Die grafische Benutzeroberfl�che f�r VierGewinnt.
 * GUI ist als View f�r ein Spielmodel (Game) konzipiert.
 * 
 * @author		p.herk
 * @version	06.01.2003 / 14:38:30
 */
public class SwingUI
	extends JFrame
	implements MouseListener, ActionListener, View {

	// Anzahl der Zeilen aus Game-Klasse besorgen
	static final int ROWS = Game.ROWS;
	// Anzahl der Spalten aus Game-Klasse besorgen
	static final int COLS = Game.COLS;

	/*
	 * m�gliche Icons f�r Spielsteine
	 */
	private ImageIcon blankIcon = new ImageIcon("./img/blank.gif");
	private ImageIcon redArrowIcon = new ImageIcon("./img/redarrow.gif");
	private ImageIcon yellowArrowIcon = new ImageIcon("./img/yellowarrow.gif");
	private ImageIcon emptyTokenIcon = new ImageIcon("./img/emptytoken.gif");
	private ImageIcon redTokenIcon = new ImageIcon("./img/redtoken.gif");
	private ImageIcon yellowTokenIcon = new ImageIcon("./img/yellowtoken.gif");

	/*
	 * GUI Elemente
	 */
	// Inhalt des Spielefensters unterhalb der Men�zeile
	private JPanel applicationDesktop = new JPanel();
	// das Panel f�r das eingentliche Spielfeld
	private JPanel playField = new JPanel();
	private JMenuBar mainMenuBar = new JMenuBar();
	private JMenu gameMenu = new JMenu();
	private JMenuItem gameMenuNew = new JMenuItem();
	private JMenuItem gameMenuExit = new JMenuItem();
	private JMenu helpMenu = new JMenu();
	private JMenuItem helpMenuAbout = new JMenuItem();
	// Icons �ber dem eigentlichen Spielfeld (f�r "Einwurf"pfeile)
	private JLabel[] insertToken = new JLabel[COLS];
	// die Icons des eigentlichen Spielfelds. (0,0) ist links unten
	private JLabel[][] playfieldToken = new JLabel[ROWS][COLS];
	// Nachrichtenzeile
	private JLabel messageLabel = new JLabel();

	// Hier werden die MoveListener verwaltet.
	private EventListenerList listenerList;

	// Farbe des Spielers.
	private Token playerColor;

	// ganz wichtig: das Modell des aktuellen Spiels
	private Game game;

	/**
	 * Konstruktor
	 */
	public SwingUI() {
		
		super();
		listenerList = new EventListenerList();
		initGUI();
		
		/* 
		 * alternativ zu startNewGame(); wird hier das Spiel
		 * mit Standardwerten Spieler-KI
		 * gestartet
		 */
		Game model = new Game(Token.RED);
		setModel(model);
		model.addObserver(this);

		View actualView = this;
		
		Player redPlayer;
		Player yellowPlayer;
		
		playerColor = Token.RED;
		
		redPlayer = new HumanPlayer(actualView, model, Token.RED);
		yellowPlayer = new AIPlayer(AIPlayer.MEDIUM, model, Token.YELLOW);
		
		initPlayfield();
		this.setVisible(true);
		// Ende Spiel anlegen
		
	}

	/**
	 * Initialisiert das Spielfenster (noch
	 * bevor ein Spiel gestartet wird)
	 */
	private void initGUI() {

		/*
		 * Labels f�r "Einwurfanzeiger" anlegen
		 */
		for (int i = 0; i < COLS; i++) {
			insertToken[i] = new JLabel();
			insertToken[i].setIcon(blankIcon);
			insertToken[i].addMouseListener(this);
			playField.add(insertToken[i]);
		}

		/*
		 * Labels f�r das eigentliche Spielfeld anlegen
		 */
		for (int i = ROWS - 1; i >= 0; i--) {
			for (int j = 0; j < COLS; j++) {
				playfieldToken[i][j] = new JLabel();
				setToken(i, j, Token.EMPTY);
				playfieldToken[i][j].addMouseListener(this);
				playField.add(playfieldToken[i][j]);
			}
		}

		/*
		 * allgemeine Fenstereigenschaften festlegen
		 */
		setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		setBounds(new java.awt.Rectangle(0, 0, 402, 437));
		setBackground(new java.awt.Color(255, 255, 255));
		setResizable(false);
		setTitle("Vier Gewinnt");
		setIconImage((new ImageIcon("./img/titleicon.gif")).getImage());
		setLocationRelativeTo(null);
		
		/*
		 * Men�leiste anlegen
		 */
		setJMenuBar(mainMenuBar);
		gameMenu.setText("Spiel");
		gameMenu.add(gameMenuNew);
		gameMenu.add(gameMenuExit);
		mainMenuBar.add(gameMenu);
		mainMenuBar.add(helpMenu);
		gameMenuNew.setText("Neu");
		gameMenuNew.setActionCommand("startNewGame");
		gameMenuNew.addActionListener(this);
		gameMenuExit.setText("Beenden");
		gameMenuExit.setActionCommand("exitGame");
		gameMenuExit.addActionListener(this);
		helpMenu.add(helpMenuAbout);
		helpMenu.setText("Info");
		helpMenuAbout.setText("Info");
		helpMenuAbout.setActionCommand("aboutGame");
		helpMenuAbout.addActionListener(this);

		/*
		 * Inhalt des Spielfensters anlegen
		 */
		getContentPane().add(applicationDesktop, java.awt.BorderLayout.CENTER);
		applicationDesktop.setLayout(new java.awt.FlowLayout());
		applicationDesktop.setSize(new java.awt.Dimension(400, 400));
		applicationDesktop.setBackground(new java.awt.Color(255, 255, 255));
		applicationDesktop.add(playField);
		applicationDesktop.add(messageLabel);

	}

	/**
	 * Initialisiert das eigentliche Spielfeld anhand des aktuellen Spiele-
	 * models
	 */
	private void initPlayfield() {

		/*
		 * Labels f�r "Einwurfanzeiger" anlegen
		 */
		for (int i = 0; i < COLS; i++) {
			insertToken[i].setIcon(blankIcon);
		}

		/*
		 * Labels f�r das eigentliche Spielfeld anlegen
		 */
		drawPlayfield();

		/*
		 * Inhalt des Spielfenldes anlegen
		 */
		playField.setLayout(new java.awt.GridLayout(7, 7));
		playField.setSize(new java.awt.Dimension(200, 200));
		playField.setBackground(new java.awt.Color(255, 255, 255));
		
		messageLabel.setText("");

	}
	
	/**
	 * Labels f�r das eigentliche Spielfeld anlegen
	 * (von Game holen und entsprechend aufbauen)
	 */
	private void drawPlayfield() {
		Token[][] board = game.getBoard();
		for (int i = (board.length - 1); i >= 0; i--) {
			for (int j = 0; j < board[i].length; j++) {
				setToken(i, j, board[i][j]);
			}
		}
	}

	/**
	 * Erzeugt einen "Neues Spiel" Dialog und erzeugt daraufhin
	 * ein neues Spielemodel.
	 * 
	 * :TODO:	Netzwerkspieler einbauen (wird im Moment durch KI substituiert)
	 */
	private void startNewGame() {
		
		NewGameDialog ngd = new NewGameDialog();
		
		if (ngd.isNewGameInitialized()) {
			
			// neues Spiel, rot beginnt
			Game model = new Game(Token.RED);
			setModel(model);
			model.addObserver(this);
			View actualView = this;
		
			Player redPlayer = null;
			String redPlayerStrength;
			Player yellowPlayer = null;
			String yellowPlayerStrength;
			boolean aiStarts = false;
			
			playerColor = ngd.getLocalPlayerColor();
			
			if (ngd.getPlayerType(Token.RED).equalsIgnoreCase("Spieler")) {
				yellowPlayer = new AIPlayer(ngd.getPlayerStrength(Token.YELLOW), model, Token.YELLOW);
				redPlayer = new HumanPlayer(actualView, model, Token.RED);
			} else if (ngd.getPlayerType(Token.YELLOW).equalsIgnoreCase("Spieler")) {
				yellowPlayer = new HumanPlayer(actualView, model, Token.YELLOW);
				redPlayer = new AIPlayer(ngd.getPlayerStrength(Token.RED), model, Token.RED);
				aiStarts = true;		
			} else if (ngd.getPlayerType(Token.RED).equalsIgnoreCase("KI")) {
				redPlayer = new AIPlayer(ngd.getPlayerStrength(Token.RED), model, Token.RED);
				aiStarts = true;
				yellowPlayer = new AIPlayer(ngd.getPlayerStrength(Token.YELLOW), model, Token.YELLOW);
			} else if (ngd.getPlayerType(Token.YELLOW).equalsIgnoreCase("KI")) {
				yellowPlayer = new AIPlayer(ngd.getPlayerStrength(Token.YELLOW), model, Token.YELLOW);
				redPlayer = new AIPlayer(ngd.getPlayerStrength(Token.RED), model, Token.RED);
				aiStarts = true;
			} else {
				// schei�e
			}
			
			// GUI fertigmachen
			initPlayfield();
			
			if (aiStarts) {
				((AIPlayer) redPlayer).update(model, new Object());
			}
			
		}
		
		this.setVisible(true);

	}

	/**
	 * Beendet das Spiel
	 */
	private void exitGame() {
		this.dispose();
	}

	/**
	 * Zeigt Informationen zum Spiel an
	 * 
	 * :TODO:	About-Dialog anzeigen
	 */
	private void aboutGame() {
		//
	}

	/**
	 * Modell des Spiels f�r die GUI setzen
	 * 
	 * @param	game	Model des zu verwendenden Spiels
	 */
	private void setModel(Game game) {
		this.game = game;
	}

	/**
	 * Steckt ein Token in Zeile row und Spalte column ins Spielbrett.
	 * 
	 * @param	row		Position in der Zeile
	 * @param	column	Position in der Spalte
	 * @param	t		"Farbe" des zu setzenden Tokens
	 */
	private void setToken(int row, int column, Token t) {
		ImageIcon tokenIcon = null;
		if (t == Token.RED) {
			tokenIcon = redTokenIcon;
		} else if (t == Token.YELLOW) {
			tokenIcon = yellowTokenIcon;
		} else if (t == Token.EMPTY) {
			tokenIcon = emptyTokenIcon;
		} else {
			// assert false
		}
		playfieldToken[row][column].setIcon(tokenIcon);
	}

	/**
	 * Wird der Mauszeiger �ber das Spielfeld gef�hrt so wird �ber der
	 * aktuellen Spalte �ber der sich der Zeiger befindet ein "Einwurfzeiger"
	 * entsprechend der Farbe des aktuellen Spielers angezeigt.
	 * Hier: Betreten einer Spalte
	 * 
	 * Existenz dieser Methode wird durch "MouseListener" bedingt.
	 * 
	 * @param	e	Event der durch Mausbewegung erzeugt wurde
	 * 
	 * :TODO:	aktuellen Spieler ermitteln und Zeiger einf�rben
	 */
	public void mouseEntered(MouseEvent e) {
		boolean jumpout = false;
		for (int i = ROWS - 1; i >= 0; i--) {
			for (int j = 0; j < COLS; j++) {
				if (e.getSource().equals(playfieldToken[i][j])) {
					insertToken[j].setIcon(redArrowIcon);
					jumpout = true;
				}
				if (jumpout) {	// :TODO:	labeled break verwenden?
					break;
				}
			}
			if (jumpout) {
				break;
			}
		}
	}

	/**
	 * Wird der Mauszeiger �ber das Spielfeld gef�hrt so wird �ber der
	 * aktuellen Spalte �ber der sich der Zeiger befindet ein "Einwurfzeiger"
	 * entsprechend der Farbe des aktuellen Spielers angezeigt.
	 * Hier: Verlassen einer Spalte
	 * 
	 * Existenz dieser Methode wird durch "MouseListener" bedingt.
	 * 
	 * @param	e	Event der durch Mausbewegung erzeugt wurde
	 * 
	 * :TODO:	aktuellen Spieler ermitteln und Zeiger einf�rben
	 */
	public void mouseExited(MouseEvent e) {
		boolean jumpout = false;
		for (int i = ROWS - 1; i >= 0; i--) {
			for (int j = 0; j < COLS; j++) {
				if (e.getSource().equals(playfieldToken[i][j])) {
					insertToken[j].setIcon(blankIcon);
					jumpout = true;
				}
				if (jumpout) {	// :TODO:	labeled break verwenden?
					break;
				}
			}
			if (jumpout) {
				break;
			}
		}
	}

	/**
	 * Behandelt den Klick eines Spieler auf das Spielfeld
	 * um einen Zug durchzuf�hen
	 * 
	 * Existenz dieser Methode wird durch "MouseListener" bedingt.
	 * 
	 * @param	e	Event der durch Dr�cken der linken
	 * 				Maustaste erzeugt wurde
	 * 
	 * :TODO:	finden der "beklickten" Spalte schneller und zuverl�ssiger
	 * 			behandeln
	 */
	public void mousePressed(MouseEvent e) {
		boolean jumpout = false;
		for (int i = ROWS - 1; i >= 0; i--) {
			for (int j = 0; j < COLS; j++) {
				if (e.getSource().equals(playfieldToken[i][j])) {
					messageLabel.setText("Klick @ col. " + j);
					fireMoveEventTokenMoved(j);
					jumpout = true;
				}
				if (jumpout) {
					break;
				}
			}
			if (jumpout) {
				break;
			}
		}
	}

	/**
	 * Existenz dieser Methode wird durch "MouseListener" bedingt.
	 * Sie wird nicht verwendet
	 */
	public void mouseReleased(MouseEvent e) {
		//
	}

	/**
	 * Existenz dieser Methode wird durch "MouseListener" bedingt.
	 * Sie wird nicht verwendet
	 */
	public void mouseClicked(MouseEvent e) {
		//
	}

	/**
	 * Behandelt die Auswahl eines Men�punktes
	 * 
	 * Existenz dieser Methode wird durch "ActionListener" bedingt.
	 * 
	 * @param	e	Event der durch Auswahl eines Men�punktes
	 * 				erzeugt wurde
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equalsIgnoreCase("startNewGame")) {
			startNewGame();
		} else if (actionCommand.equalsIgnoreCase("exitGame")) {
			exitGame();
		} else if (actionCommand.equalsIgnoreCase("aboutGame")) {
			aboutGame();
		}
	}


	/**
	 * XXX Bin mir nicht sicher, ob das n�tig ist: Component.update(Graphics)
	 * �berladen, wegen "konflikt" mit View.update(Observable, Object).
	 */
	public void update(java.awt.Graphics g) {
		super.update(g);
	}

	/**
	 * Im Model hat sich was ge�ndert, also Model neu darstellen
	 * 
	 * Existenz dieser Methode wird durch "View" bedingt.
	 * 
	 * @param	o	das Objekt das sich ge�ndert hat
	 * @param	arg	???
	 * 
	 * :TODO:	echte Nachricht des Spiels ausgeben
	 */
	public void update(Observable o, Object arg) {
		System.out.println("SwingUI.update(): " + o + " sagt " + arg);
		// Spielbrett komplett neu zeichnen:
		drawPlayfield();
		// Nachricht des Spiels ausgeben
		messageLabel.setText(game.toString());	// :TODO:	echte "Message" verarbeiten
	}

	/**
	 * F�gt einen MoveEventListener hinzu.
	 * 
	 * @params	listener	???
	 */
	public void addMoveEventListener(MoveEventListener listener) {
		listenerList.add(MoveEventListener.class, listener);
	}

	/**
	 * Entfernt einen MoveEventListener hinzu.
	 * 
	 * @params	listener	???
	 */
	public void removeMoveEventListener(MoveEventListener listener) {
		listenerList.remove(MoveEventListener.class, listener);
	}
	
	/**
	 * "Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method."
	 * 
	 * XXX: Besser ChangeEvent?
	 * 
	 * @param	column	???
	 */
	protected void fireMoveEventTokenMoved(int column) {
		MoveEvent move = null;
		System.out.println("SwingUI.fireMoveEventTokenMoved(): column=" + column);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MoveEventListener.class) {
				// Lazily create the event:
				if (move == null) {
					//move = new Move(this);
					//move = new MoveEvent (playerColor, column);
					move = new MoveEvent(this, column);
				}
				((MoveEventListener) listeners[i + 1]).tokenMoved(move);
			}
		}
	}

}
