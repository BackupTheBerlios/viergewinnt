package de.fhhn.viergewinnt.ui.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Der Dialog zum Anlegen eines neuen Spiels.
 * 
 * @author $Author: kathrin $
 * @version $Revision: 1.10 $
 */
public class NewGameDialog extends JDialog implements ActionListener {
	
	/*
	 * einige oft verwendete Stringliterale (verhindert vertippen)
	 */
	// Spielertypen
	public static final String SPIELER			= "Spieler";
	public static final String KI				= "KI";
	public static final String NETZ				= "Netz";
	// mögliche Stärken der KI
	public static final String SCHWACH			= "schwach";
	public static final String MITTEL			= "mittel";
	//public static final String STARK				= "stark";
	// ActionCommands für ComboBoxen
	public static final String SELECTEDREDPLAYER	= "selectedRedPlayer";
	public static final String SELECTEDREDPLAYERSTRENGTH		= "selectRedPlayerStrength";
	public static final String SELECTYELLOWPLAYER			= "selectYellowPlayer";
	public static final String SELECTEDYELLOWPLAYERSTRENGTH	= "selectYellowPlayerStrength";
	//ActionCommands für Buttons
	public static final String STARTNEWGAME		= "startNewGame";
	public static final String CANCELSTARTNEWGAME = "cancelStartNewGame";
	
	/*
	 * GUI Elemente
	 */
	// Inhalt des Fensters ...
	private JPanel dialogDesktop = new JPanel();
	// ... linke Seite (roter Spieler) ...
	private JPanel redDialogPane1 = new JPanel();
	private JPanel redDialogPane2 = new JPanel();
	private JPanel redPlayerTypePane = new JPanel();
	private JLabel redPlayerTypeLabel = new JLabel();
	private JComboBox redPlayerTypeSelector = new JComboBox();
	private JPanel redPlayerHostPane = new JPanel();
	private JLabel redPlayerHostLabel = new JLabel();
	private JTextField redPlayerHostTextField = new JTextField();
	private JComboBox redPlayerStrengthSelector = new JComboBox();
	// ... rechte Seite (gelber Spieler) ...
	private JPanel yellowDialogPane1 = new JPanel();
	private JPanel yellowDialogPane2 = new JPanel();
	private JPanel yellowPlayerTypePane = new JPanel();
	private JLabel yellowPlayerTypeLabel = new JLabel();
	private JComboBox yellowPlayerTypeSelector = new JComboBox();
	private JPanel yellowPlayerHostPane = new JPanel();
	private JLabel yellowPlayerHostLabel = new JLabel();
	private JTextField yellowPlayerHostTextField = new JTextField();
	private JComboBox yellowPlayerStrengthSelector = new JComboBox();
	// ... unterer Teil (Buttons)
	private JPanel dialogButtonPane = new JPanel();
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	
	// zeigt an, ob auch wirklich ein neues Spiel angelegt werden soll,
	// Wird entsprechend des Klicks auf 'nen Button gesetzt
	private boolean isNewGameInitialized = false;
	
	/**
	 * Konstruktor
	 */
	NewGameDialog() {
		
		super();
		initGUI();
		setVisible(true);
		
	}

	/**
	 * Initialisiert das Dialogfenster
	 */
	private void initGUI() {
		
		/*
		 * Fensterlayout vorbereiten
		 */
		getContentPane().add(Box.createRigidArea(new Dimension(10, 10)), java.awt.BorderLayout.NORTH);
		getContentPane().add(Box.createRigidArea(new Dimension(10, 10)), java.awt.BorderLayout.EAST);
		getContentPane().add(Box.createRigidArea(new Dimension(10, 10)), java.awt.BorderLayout.WEST);
		getContentPane().add(dialogButtonPane, java.awt.BorderLayout.SOUTH);
		getContentPane().add(dialogDesktop, java.awt.BorderLayout.CENTER);
		
		/*
		 * allgemeine Fenstereigenschaften festlegen
		 */
		setBounds(new java.awt.Rectangle(0, 0, 485, 200));
		setModal(true);
		setTitle("Neues Spiel");
		setResizable(false);
		setLocationRelativeTo(null);
		
		/*
		 * Inhalt des Dialogfensters anlegen
		 */
		dialogDesktop.setLayout(new java.awt.GridLayout(1, 2, 10, 10));
		dialogDesktop.setBorder(null);
		dialogDesktop.add(redDialogPane1);
		dialogDesktop.add(yellowDialogPane1);
		
		/*
		 * rote Seite anlegen
		 */
		redDialogPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 1), "Spieler Rot", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 0, 11), new java.awt.Color(60, 60, 60)));
		redDialogPane1.setLayout(new java.awt.GridLayout(1, 1));
		redDialogPane1.add(redDialogPane2);
		
		redDialogPane2.setLayout(new java.awt.GridLayout(2, 1));
		redDialogPane2.add(redPlayerTypePane);
		redDialogPane2.add(redPlayerHostPane);
		
		redPlayerTypeLabel.setText("Spielertyp");
		
		redPlayerTypePane.add(redPlayerTypeLabel);
		redPlayerTypePane.add(redPlayerTypeSelector);
		
		redPlayerTypePane.add(redPlayerTypeLabel);
		redPlayerTypePane.add(redPlayerTypeSelector);
		
		redPlayerHostLabel.setText("Hostadresse");
		
		redPlayerHostPane.add(redPlayerHostLabel);
		redPlayerHostPane.add(redPlayerHostTextField);
		
		redPlayerHostTextField.setText("");
		redPlayerHostTextField.setPreferredSize(new java.awt.Dimension(118, 20));
		
		redPlayerTypeSelector.addItem(SPIELER);
		redPlayerTypeSelector.addItem(KI);
		//redPlayerTypeSelector.addItem(NETZ);
		redPlayerTypeSelector.addActionListener(this);
		redPlayerTypeSelector.setActionCommand(SELECTEDREDPLAYER);
		redPlayerTypeSelector.setSelectedItem(SPIELER);
		
		redPlayerStrengthSelector.addItem(SCHWACH);
		redPlayerStrengthSelector.addItem(MITTEL);
		//redPlayerStrengthSelector.addItem(STARK);
		redPlayerStrengthSelector.addActionListener(this);
		redPlayerStrengthSelector.setActionCommand(SELECTEDREDPLAYERSTRENGTH);
		redPlayerStrengthSelector.setSelectedItem(MITTEL);
		
		/*
		 * gelbe Seite anlegen
		 */
		yellowDialogPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 1), "Spieler Gelb", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 0, 11), new java.awt.Color(60, 60, 60)));
		yellowDialogPane1.setLayout(new java.awt.GridLayout(1, 1));
		yellowDialogPane1.add(yellowDialogPane2);
		
		yellowDialogPane2.setLayout(new java.awt.GridLayout(2, 1));
		yellowDialogPane2.add(yellowPlayerTypePane);
		yellowDialogPane2.add(yellowPlayerHostPane);
		
		yellowPlayerTypeLabel.setText("Spielertyp");
		
		yellowPlayerTypePane.add(yellowPlayerTypeLabel);
		yellowPlayerTypePane.add(yellowPlayerTypeSelector);
		
		yellowPlayerHostLabel.setText("Hostadresse");
		
		yellowPlayerHostPane.add(yellowPlayerHostLabel);
		yellowPlayerHostPane.add(yellowPlayerHostTextField);
		
		yellowPlayerHostTextField.setText("");
		yellowPlayerHostTextField.setPreferredSize(new java.awt.Dimension(118, 20));
		
		yellowPlayerTypeSelector.addItem(SPIELER);
		yellowPlayerTypeSelector.addItem(KI);
		//yellowPlayerTypeSelector.addItem(NETZ);
		yellowPlayerTypeSelector.addActionListener(this);
		yellowPlayerTypeSelector.setActionCommand(SELECTYELLOWPLAYER);
		yellowPlayerTypeSelector.setSelectedItem(KI);
		
		yellowPlayerStrengthSelector.addItem(SCHWACH);
		yellowPlayerStrengthSelector.addItem(MITTEL);
		//yellowPlayerStrengthSelector.addItem(STARK);
		yellowPlayerStrengthSelector.addActionListener(this);
		yellowPlayerStrengthSelector.setActionCommand(SELECTEDYELLOWPLAYERSTRENGTH);
		yellowPlayerStrengthSelector.setSelectedItem(MITTEL);
		
		/*
		 * Buttonleiste anlegen
		 */	
		dialogButtonPane.add(okButton);
		dialogButtonPane.add(cancelButton);
		
		okButton.setText("Ok");
		okButton.setPreferredSize(new java.awt.Dimension(130, 27));
		okButton.setActionCommand(STARTNEWGAME);
		okButton.addActionListener(this);
		
		cancelButton.setText("Abbrechen");
		cancelButton.setPreferredSize(new java.awt.Dimension(130, 27));
		cancelButton.setActionCommand(CANCELSTARTNEWGAME);
		cancelButton.addActionListener(this);

	}

	/**
	 * Behandelt das Klicken eines Buttons und Änderungen an der Selektion des
	 * "Spielertyp"s und der Stärke der KI
	 * 
	 * Existenz dieser Methode wird durch "ActionListener" bedingt.
	 * 
	 * @param	e	Event der erzeugt wurde
	 */
	public void actionPerformed(ActionEvent e) {
		
		String actionCommand = e.getActionCommand();
		String selectedItem = "";
		
		/*
		 * falls der ActionEvent nicht durch eine Combobox ausgelöst wurde gibt
		 * es eine ClassCastException -> hier abfangen
		 */
		try {
			selectedItem =
				(((JComboBox) e.getSource()).getSelectedItem()).toString();
		} catch (ClassCastException ex) {
			// this line has been intentionally left blank :)
		}
		
		// entweder der rote "Spielertyp" wurde verändert ...
		if (actionCommand.equalsIgnoreCase(SELECTEDREDPLAYER)) {

			if (selectedItem.equalsIgnoreCase(SPIELER)) {
				
				if (redPlayerHostPane.isAncestorOf(redPlayerHostTextField)) {
					redPlayerHostLabel.setText("");
					redPlayerHostPane.remove(redPlayerHostTextField);
				} else if (redPlayerHostPane.isAncestorOf(redPlayerStrengthSelector)) {
					redPlayerHostLabel.setText("");
					redPlayerHostPane.remove(redPlayerStrengthSelector);
				}
				
			} else if (selectedItem.equalsIgnoreCase(KI)) {
				
				if (redPlayerHostPane.isAncestorOf(redPlayerHostTextField)) {
					redPlayerHostLabel.setText("");
					redPlayerHostPane.remove(redPlayerHostTextField);
				}
				
				redPlayerHostLabel.setText("Stärke");
				redPlayerHostPane.add(redPlayerStrengthSelector);
				
			} else if (selectedItem.equalsIgnoreCase(NETZ)) {
				
				if (redPlayerHostPane.isAncestorOf(redPlayerStrengthSelector)) {
					redPlayerHostLabel.setText("");
					redPlayerHostPane.remove(redPlayerStrengthSelector);
				}
				
				redPlayerHostLabel.setText("Hostadresse");
				redPlayerHostPane.add(redPlayerHostTextField);
				
			}

		// ... oder der gelbe ...
		} else if (actionCommand.equalsIgnoreCase(SELECTYELLOWPLAYER)) {
			
			if (selectedItem.equalsIgnoreCase(SPIELER)) {
				
				if (yellowPlayerHostPane.isAncestorOf(yellowPlayerHostTextField)) {
					yellowPlayerHostLabel.setText("");
					yellowPlayerHostPane.remove(yellowPlayerHostTextField);
				} else if (yellowPlayerHostPane.isAncestorOf(yellowPlayerStrengthSelector)) {
					yellowPlayerHostLabel.setText("");
					yellowPlayerHostPane.remove(yellowPlayerStrengthSelector);
				}
				
			} else if (selectedItem.equalsIgnoreCase(KI)) {
				
				if (yellowPlayerHostPane.isAncestorOf(yellowPlayerHostTextField)) {
					yellowPlayerHostLabel.setText("");
					yellowPlayerHostPane.remove(yellowPlayerHostTextField);
				}
				
				yellowPlayerHostLabel.setText("Stärke");
				yellowPlayerHostPane.add(yellowPlayerStrengthSelector);
				
			} else if (selectedItem.equalsIgnoreCase(NETZ)) {
				
				if (yellowPlayerHostPane.isAncestorOf(yellowPlayerStrengthSelector)) {
					yellowPlayerHostLabel.setText("");
					yellowPlayerHostPane.remove(yellowPlayerStrengthSelector);
				}
				
				yellowPlayerHostLabel.setText("Hostadresse");
				yellowPlayerHostPane.add(yellowPlayerHostTextField);
				
			}
		
		// ... oder der OK-Button wurde angeklickt ...
		} else if (actionCommand.equalsIgnoreCase(STARTNEWGAME)) {
			startNewGame();
		
		// ... oder der Abbrechen-Button ...
		} else if (actionCommand.equalsIgnoreCase(CANCELSTARTNEWGAME)) {
			cancelNewGame();
		
		// ... oder die Stärkeeinstellung des roten KI-Spielers wurde verändert ...
		} else if (actionCommand.equalsIgnoreCase(SELECTEDREDPLAYERSTRENGTH)) {
			// this line has been intentionally left blank :)
		
		// ...oder die des gelben KI-Spielers
		} else if (actionCommand.equalsIgnoreCase(SELECTEDYELLOWPLAYERSTRENGTH)) {
			// this line has been intentionally left blank :)
		}
		
	}
	
	/**
	 * Kombination der "Spielertyp"en prüfen und wenn ok Dialog beenden, sonst
	 * Dialog offen lassen
	 * 
	 * :TODO:	evtl. Möglickeit für K-K einbauen (siehe getLocalPlayerColor())
	 */
	private void startNewGame() {
		
		boolean isKombinationOk = false;
		
		String redPlayer = redPlayerTypeSelector.getSelectedItem().toString();
		String yellowPlayer = yellowPlayerTypeSelector.getSelectedItem().toString();
		
		// zulässige Spielerkombinationen prüfen: S-K, K-S, S-N, N-S, K-N, N-K
		if ((redPlayer.equalsIgnoreCase(SPIELER) && yellowPlayer.equalsIgnoreCase(KI)) ||
				(redPlayer.equalsIgnoreCase(KI) && yellowPlayer.equalsIgnoreCase(SPIELER)) ||
				(redPlayer.equalsIgnoreCase(SPIELER) && yellowPlayer.equalsIgnoreCase(NETZ)) ||
				(redPlayer.equalsIgnoreCase(NETZ) && yellowPlayer.equalsIgnoreCase(SPIELER)) ||
				(redPlayer.equalsIgnoreCase(KI) && yellowPlayer.equalsIgnoreCase(NETZ)) ||
				(redPlayer.equalsIgnoreCase(NETZ) && yellowPlayer.equalsIgnoreCase(KI))) {
			isKombinationOk = true;
		}
		
		// wenn Kombination ok ist, dann Flag entsprechend setzen
		// und den Dialog beenden
		if (isKombinationOk) {
			isNewGameInitialized = true;
			this.dispose();
		}
	}

	/**
	 * "NeuesSpiel"-Dialog verwerfen
	 */
	private void cancelNewGame() {
		
		this.dispose();
		
	}

	/**
	 * Farbe des "lokalen" Spielers ermitteln. Entweder sitzt ein HumanPlayer 
	 * vor dem Rechner, oder der Benutzer lässt die KI gegen einen Remote-Spieler
	 * spielen. In diesem Fall ist der AIPlayer lokal (Benutzer sitzt davor und
	 * hofft, dass seine KI gewinnt ;)
	 * 
	 * @return	"Farbe" des lokalen Spielers
	 * 
	 * :TODO:	evtl. Möglickeit für K-K einbauen (siehe startNewGame())
	 */
	public Token getLocalPlayerColor() {
		
		if (redPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase(SPIELER)) {
			return Token.RED;
		} else if (yellowPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase(SPIELER)) {
			return Token.YELLOW;
		} else if (redPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase(KI)) {
			return Token.RED;
		} else if (yellowPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase(KI)) {
			return Token.YELLOW;
		} else {
			return Token.EMPTY;
		}
		
	}
	
	/**
	 * Typ (Human/AI/Remote) des Spielers einer bestimmten Farbe ermitteln
	 * 
	 * @param	color	"Farbe" des Spielers dessen Typ ermittelt werden soll
	 * @return			Typ des Spielers (SPIELER/KI/NETZ)
	 */
	public String getPlayerType(Token color) {
		
		if (color == Token.RED) {
			return redPlayerTypeSelector.getSelectedItem().toString();
		} else {
			return yellowPlayerTypeSelector.getSelectedItem().toString();
		}
		
	}
	
	/**
	 * Stellt Informationen über die gewünschte Spielstärke eine AIPlayers
	 * zur Verfügung
	 * 
	 * @param	color	"Farbe" des Spielers dessen Stärke ermittelt werden soll
	 * @return			Stärke des Spielers (SCHWACH/MITTEL/STARK))
	 */
	public int getPlayerStrength(Token color) {
		
		int strength=AIPlayer.MEDIUM;
		if (color == Token.RED) {
//			if (redPlayerStrengthSelector.getSelectedItem().toString().equalsIgnoreCase(STARK)) {
//				strength = AIPlayer.STRONG;
//			} else
            if (redPlayerStrengthSelector.getSelectedItem().toString().equalsIgnoreCase(SCHWACH)) {
				strength = AIPlayer.WEAK;
			}
		} else {
//			if (yellowPlayerStrengthSelector.getSelectedItem().toString().equalsIgnoreCase(STARK)) {
//				strength = AIPlayer.STRONG;
//			} else
            if (yellowPlayerStrengthSelector.getSelectedItem().toString().equalsIgnoreCase(SCHWACH)) {
				strength = AIPlayer.WEAK;
			}
		}
		return strength;
		
	}
	
	/**
	 * prüft ob ein neues Spiel gewünscht ist (OK-Button wurde gedrückt),
	 * oder ob der Dialog abgebrochen wurde.
	 * 
	 * @return	soll tatsächlich neues Spiel angelegt werden
	 */
	public boolean isNewGameInitialized() {
		return isNewGameInitialized;
	}
	
}
