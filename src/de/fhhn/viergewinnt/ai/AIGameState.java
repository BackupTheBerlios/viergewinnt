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
class AIGameState extends GameState {
	public AIGameState(Token whoseTurn) {
		super(whoseTurn);
	}

	public AIGameState(Token whoseTurn, Token[][] board) {
		super(whoseTurn, board);
	}

	public static void expand(GraphNode node, GraphNodeList list, int limit) {
		// Abbruchbedingung
		AIGameState state = node.getState();
		if (state.isFinalState(node)) {
			// Knotenbewertung
			return;
		} else if (limit == 0) {
			// heuristische Stellungsbewertung
			return;
		}

		// Nachfolgerzustände von state berechnen
		ArrayList succStates = state.calculateSuccessors();
		ListIterator it = succStates.listIterator();


		while (it.hasNext()) {
			// Für alle Nachfolgerzustände von state
			AIGameState succState = (AIGameState) it.next();
			if (list.contains(succState)) {
				GraphNode succNode = list.getNode(succState);
				node.addSuccessor(succNode);
				continue;
			}
			GraphNode succNode = new GraphNode(succState, node);
			node.addSuccessor(succNode);
			list.add(succNode);
			expand(succNode, list, limit - 1); // Rekursion!
		}
	}

	private boolean isFinalState(GraphNode node) {
		GraphNode parent = node.getParent();

		// hat jemand gewonnen?
		if (parent != null) { // XXX lastMoveEvent ermitteln
			AIGameState parentState = parent.getState();
			int row = 0;
			int col = 0;
			
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					if (board[i][j] != parent.getState().board[i][j]) {
						row = i;
						col = j;
					}
				}
			}
			
			if (checkWinner(row, col) == Token.RED || checkWinner(row, col) == Token.YELLOW) {
				return true;
			}
		}
		
		// Spielbrett voll?
		if (boardIsFull()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean boardIsFull() {
		// oberste Reihe voll?
		for (int i = 0; i < board[Game.ROWS - 1].length; i++) {
			if (board[Game.ROWS - 1][i] == Token.EMPTY) {
				return false;
			}
		}
		return true;
	}
	
	/** Berechnet die Nachfolgerzustände dieses Zustands. */
	private ArrayList calculateSuccessors() {
		
		// alle Nachfolger-Zustände bauen
		ArrayList successors = new ArrayList();
		// für jede Spalte
		for (int cols = 0; cols < board[0].length; cols++) {
			// Zeile suchen, in der noch Platz frei ist
			int row = -1;
			do {
				row++;
			} while (!(board[row][cols] == Token.EMPTY) && (row < Game.ROWS)); 

			if (row == Game.ROWS) { // Spalte voll?
				continue;
			}
			
			// (zweidimensionales Array kopieren)
			Token[][] succ = new Token[Game.ROWS][Game.COLS];
			for (int k = 0; k < board.length; k++) {
				System.arraycopy(board[k], 0, succ[k], 0, board[k].length);
			}
				
			if (whoseTurn == Token.RED) {
				whoseTurn = Token.YELLOW;
			} else if (whoseTurn == Token.YELLOW) {
				whoseTurn = Token.RED;
			} else {
				// assert false
			}
			// neue Marke setzen
			succ[row][cols] = whoseTurn;
			successors.add(new AIGameState(whoseTurn, succ));
		}
		
		return successors;
	}

	/** Wird für's Hashen benötigt. */
	public boolean equals(Object other) {
		if (other != null && getClass() == other.getClass()) {
			AIGameState otherAIGameState = (AIGameState) other;
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
		GraphNode root = new GraphNode(new AIGameState(Token.RED), null);
		GraphNodeList list = new GraphNodeList();
		AIGameState.expand(root, list, 4);
		System.out.println((root.getSuccessorAmount() +  1) + " Knoten");
	}
}
