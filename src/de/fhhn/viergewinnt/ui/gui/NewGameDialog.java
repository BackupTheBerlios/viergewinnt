package de.fhhn.viergewinnt.ui.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Der Dialog zum Anlegen eines neuen Spiels.
 * 
 * @author		p.herk
 * @version	06.01.2003 / 14:38:30
 * 
 * :TODO:	Spielstärke noch mit abfragen
 */
public class NewGameDialog extends JDialog implements ActionListener {
	
	/*
	 * GUI Elemente
	 */
	// Inhalt des Fensters
	private JPanel dialogDesktop = new JPanel();
	// linke Seite (roter Spieler)
	private JPanel redDialogPane1 = new JPanel();
	private JPanel redDialogPane2 = new JPanel();
	private JPanel redPlayerTypePane = new JPanel();
	private JLabel redPlayerTypeLabel = new JLabel();
	private JComboBox redPlayerTypeSelector = new JComboBox();
	private JPanel redPlayerHostPane = new JPanel();
	private JLabel redPlayerHostLabel = new JLabel();
	private JTextField redPlayerHostTextField = new JTextField();
	// rechte Seite (gelber Spieler)
	private JPanel yellowDialogPane1 = new JPanel();
	private JPanel yellowDialogPane2 = new JPanel();
	private JPanel yellowPlayerTypePane = new JPanel();
	private JLabel yellowPlayerTypeLabel = new JLabel();
	private JComboBox yellowPlayerTypeSelector = new JComboBox();
	private JPanel yellowPlayerHostPane = new JPanel();
	private JLabel yellowPlayerHostLabel = new JLabel();
	private JTextField yellowPlayerHostTextField = new JTextField();
	// unterer Teil (Buttons)
	private JPanel dialogButtonPane = new JPanel();
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	
	// zeigt an, ob auch wirklich ein neues Spiel angelegt werden soll,
	// Wird entsprechend des Klicks auf nen Button gesetzt
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
		setBounds(new java.awt.Rectangle(0, 0, 485, 168));
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
		
		redPlayerTypeSelector.addItem("Spieler");
		redPlayerTypeSelector.addItem("KI");
		redPlayerTypeSelector.addItem("Netz");
		redPlayerTypeSelector.addActionListener(this);
		redPlayerTypeSelector.setActionCommand("selectRedPlayer");
		redPlayerTypeSelector.setSelectedItem("Spieler");
		
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
		yellowPlayerHostTextField.setEditable(false);
		yellowPlayerHostTextField.setPreferredSize(new java.awt.Dimension(118, 20));
		
		yellowPlayerTypeSelector.addItem("Spieler");
		yellowPlayerTypeSelector.addItem("KI");
		yellowPlayerTypeSelector.addItem("Netz");
		yellowPlayerTypeSelector.addActionListener(this);
		yellowPlayerTypeSelector.setActionCommand("selectYellowPlayer");
		yellowPlayerTypeSelector.setSelectedItem("KI");
		
		/*
		 * Buttonleiste anlegen
		 */	
		dialogButtonPane.add(okButton);
		dialogButtonPane.add(cancelButton);
		
		okButton.setText("Ok");
		okButton.setPreferredSize(new java.awt.Dimension(130, 27));
		okButton.setActionCommand("startNewGame");
		okButton.addActionListener(this);
		
		cancelButton.setText("Abbrechen");
		cancelButton.setPreferredSize(new java.awt.Dimension(130, 27));
		cancelButton.setActionCommand("cancelStartNewGame");
		cancelButton.addActionListener(this);

	}

	/**
	 * Behandelt das Klicken eines Buttons und Änderungen an der Selektion des
	 * Spielertyps
	 * 
	 * Existenz dieser Methode wird durch "ActionListener" bedingt.
	 * 
	 * @param	e	Event der erzeugt wurde
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		String selectedItem = "";
		try {
			selectedItem =
				(((JComboBox) e.getSource()).getSelectedItem()).toString();
		} catch (Exception ex) {
			//
		}
		
		System.out.println("actionCommand: " + actionCommand);
		System.out.println("selectedItem: " + selectedItem);
		
		// roter Spielertyp wurde verändert
		if (actionCommand.equalsIgnoreCase("selectRedPlayer")) {

			if (selectedItem.equalsIgnoreCase("Spieler")) {
				redPlayerHostTextField.setEditable(false);
			} else if (selectedItem.equalsIgnoreCase("KI")) {
				redPlayerHostTextField.setEditable(false);
			} else if (selectedItem.equalsIgnoreCase("Netz")) {
				redPlayerHostTextField.setEditable(true);
			}

		// gelber Spielertyp wurde verändert
		} else if (actionCommand.equalsIgnoreCase("selectYellowPlayer")) {

			if (selectedItem.equalsIgnoreCase("Spieler")) {
				yellowPlayerHostTextField.setEditable(false);
			} else if (selectedItem.equalsIgnoreCase("KI")) {
				yellowPlayerHostTextField.setEditable(false);
			} else if (selectedItem.equalsIgnoreCase("Netz")) {
				yellowPlayerHostTextField.setEditable(true);
			}
		
		// OK-Button wurde angeklickt
		} else if (actionCommand.equalsIgnoreCase("startNewGame")) {
			startNewGame();
		
		// Abbrechen-Button wurde angeklickt
		} else if (actionCommand.equalsIgnoreCase("cancelStartNewGame")) {
			cancelStartNewGame();
		}
	}
	
	/**
	 * Kombination der Spielertypen prüfen und wenn ok Dialog beenden, sonst
	 * Dialog offen lassen
	 * 
	 * :TODO:	Kombis prüfen
	 */
	private void startNewGame() {
		System.out.println("starting new Game");
		boolean isKombinationOk = true;
		
		// mögliche Kombis prüfen, sonst Fehler: S-K, K-S, S-N, N-S, K-N, N-K
		
		if (isKombinationOk) {
			this.dispose();
			isNewGameInitialized = true;
		}
	}

	/**
	 * "NeuesSpiel"-Dialog abbrechen
	 */
	private void cancelStartNewGame() {
		System.out.println("exiting dialog");
		this.dispose();
	}

	/**
	 * Farbe des "lokalen" Spielers ermitteln. Entweder sitzt ein HumanPlayer 
	 * vor dem Rechner, oder der Benutzer lässt die KI gegen einen Remote-Spieler
	 * spielen. In diesem Fall ist der AIPlayer lokal (Benutzer sitzt davor und
	 * hofft, dass seine KI gewinnt ;)
	 * 
	 * @return	"Farbe" des lokalen Spielers
	 */
	public Token getLocalPlayerColor() {
		if (redPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase("Spieler")) {
			return Token.RED;
		} else if (yellowPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase("Spieler")) {
			return Token.YELLOW;
		} else if (redPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase("KI")) {
			return Token.RED;
		} else if (yellowPlayerTypeSelector.getSelectedItem().toString().equalsIgnoreCase("KI")) {
			return Token.YELLOW;
		} else {
			return Token.EMPTY;
		}
	}
	
	/**
	 * Typ (Human/AI/Remote) des Spielers einer bestimmten Farbe ermitteln
	 * 
	 * @param	color	"Farbe" des Spielers dessen Typ ermittelt werden soll
	 * @return			Typ des Spielers ("Spieler"/"KI"/"Netz"))
	 */
	public String getPlayerType(Token color) {
		if (color == Token.RED) {
			return redPlayerTypeSelector.getSelectedItem().toString();
		} else {
			return yellowPlayerTypeSelector.getSelectedItem().toString();
		}
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
	
	
	/** nur zu Testzwecken */
	public static void main(String[] args) {
		new NewGameDialog();
	}
}
