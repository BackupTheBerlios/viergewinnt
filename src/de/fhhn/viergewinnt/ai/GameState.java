package de.fhhn.viergewinnt.ai;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * "Zustand des Spielfelds, Methode zum Teste ob Endzustand, Methode zur
 * Berechnung der Nachfolgerzustände".
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since IOC
 */
class GameState {
	private Token beginner = Token.EMPTY;
	private int remainingEmptyHoles = 9;

	/** Zustand des Spielfelds. */
	private Token[][] board = new Token[Game.ROWS][Game.COLS];

	public GameState(Token beginner) {
		this.beginner = beginner;
		// board initialisieren
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = Token.EMPTY;
			}
		}
	}

	public GameState(Token beginner, Token[][] board) {
		this.beginner = beginner;
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
		}
	}

	public static void expand(GraphNode node, GraphNodeList list) {
		// Abbruchbedingung
		GameState state = node.getState();
		if (state.isFinalState()) {
			return;
		}

		// Nachfolgerzustände von state berechnen
		ArrayList succStates = state.calculateSuccessors();
		ListIterator it = succStates.listIterator();


		while (it.hasNext()) {
			// Für alle Nachfolgerzustände von state
			GameState succState = (GameState) it.next();
			if (list.contains(succState)) {
				GraphNode succNode = list.getNode(succState);
				node.addSuccessor(succNode);
				continue;
			}
			GraphNode succNode = new GraphNode(succState);
			node.addSuccessor(succNode);
			list.add(succNode);
			expand(succNode, list); // Rekursion!
		}
	}

	private boolean isFinalState() {
		return true;
		/*
		 * Endzustand := ein Spieler hat gewonnen
		 *               oder es sind alle Löcher belegt (unentschieden)
		 */

		/*
		int winner = Token.EMPTY;
		for (int i = 0; i < board.length - 1; i++) {
  		if (board[i][0] == board[i][1]
				&& board[i][0] == board[i][2]) {
	 	      winner = board[i][0];
    		}
      }

		for (int j = 0; j < board.length - 1; j++) {
	    if (board[0][j] == board[1][j]
			&& board[0][j] == board[2][j]) {
				winner = board[0][j];
      }
		}

		if (board[0][0] == board[1][1]
		&& board[0][0] == board[2][2]) {
	   	winner = board[0][0];
		}

		if (board[2][0] == board[1][1]
		&& board[2][0] == board[0][2]) {
			winner = board[2][0];
		}

		if (winner == GameState.X || winner == GameState.O) {
	    return true;
		} else if (remainingEmptyHoles == 0) {
	    return true;
		} else {
	    return false;
		}
		*/
	}

	/** Berechnet die Nachfolgerzustände dieses Zustands. */
	private ArrayList calculateSuccessors() {
		// Anzahl der bereits gesetzten Marken und der noch leeren Felder
		// berechnen
		int countRed = 0;
		int countYellow = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != Token.EMPTY) {
					remainingEmptyHoles--;
					if (board[i][j] == Token.RED) {
						countRed++;
					} else {
						countYellow++;
					}
				}
			}
		}
		
		// wer ist gerade dran?
		Token whoseTurn;
		if (countRed == countYellow) {
			whoseTurn = beginner;
		} else if (countRed < countYellow) {
			whoseTurn = Token.RED;
		} else {
			whoseTurn = Token.YELLOW;
		}
		
		// alle Nachfolger-Zustände bauen
		ArrayList successors = new ArrayList();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				// Für jedes noch freie Feld
				if (board[i][j] == Token.EMPTY) {
					// (zweidimensionales Array kopieren)
					Token[][] succ = new Token[board.length][board[i].length];
					for (int k = 0; k < board.length; k++) {
						System.arraycopy(board[k], 0, succ[k], 0, board[k].length);
					}
					
					// neue Marke setzen
					succ[i][j] = whoseTurn;

					// neuen GameState speichern
					successors.add(new GameState(beginner, succ));
				}
			}
		}
		/*
		System.out.println(whoseTurn + " ist dran, " + remainingEmptyHoles
			+ " leere Löcher, Nachfolger=" + successors);
		*/
		return successors;
	}

	/** Wird für's Hashen benötigt. */
	public boolean equals(Object other) {
		if (other != null && getClass() == other.getClass()) {
			GameState otherGameState = (GameState) other;
			return (other.hashCode() == hashCode()); // XXX Legal?
		} else {
			return false;
		}
	}

	/** Wird für's Hashen benötigt. */
	public int hashCode() {
		// den Inhalt aller Spielfelder in einem String hintereinanderhängen
		// -> einmaliger String für diesen Zustand -> Hash-Wert
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				sb.append("" + board[i][j]);
			}
		}
		String s = sb.toString(); // XXX Umwandlung in String nötig?
		return s.hashCode();
	}

	public static void main(String[] args) {
		GraphNode root = new GraphNode(new GameState(Token.RED));
		GraphNodeList list = new GraphNodeList();
		GameState.expand(root, list);
		System.out.println((list.size()  + 1)+ " Zustände");
	}
}
