package de.fhhn.viergewinnt.ai;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * "Zustand des Spielfelds, Methode zum Teste ob Endzustand, Methode zur
 * Berechnung der Nachfolgerzustände". erweitert
 * de.fhhn.viergewinnt.game.GameState um KI-spezifische Funktionen.
 * @author $Author: malte $
 * @version $Revision: 1.20 $
 * @since IOC
 * @testcase test.de.fhhn.viergewinnt.ai.TestAIGameState
 */
public class AIGameState extends GameState {
    /**
     * Erzeugt einen neuen Anfangs-Spielzustand.
     * @param whoseTurn der Spieler, der das Spiel beginnt
     */
	public AIGameState(Token whoseTurn) {
		super(whoseTurn);
	}

    /**
     * 
     * Erzeugt einen Spielzustand.
     * @param whoseTurn der Spieler, der gerade am Zug ist
     * @param board Zustand des Spielbretts
     */
	public AIGameState(Token whoseTurn, Token[][] board, MoveEvent last) {
		super(whoseTurn, board, last);
	}

    /**
     * Kopierkonstruktor.
     */
    public AIGameState(AIGameState state) {
        super(state.getWhoseTurn());
        if (state.lastMoveEvent != null) {
	        lastMoveEvent = new MoveEvent(state.lastMoveEvent);
        }
		for (int i = 0; i < state.board.length; i++) {
			System.arraycopy(state.board[i], 0, this.board[i], 0,
                             state.board[i].length);
		}
    }


    /**
     * Überprüft, ob der Knoten einen Endzustand enthält
     * @return true wenn der Knoten einen Endzustand enthält
     * @param node Knoten dessen Zustand überprüft werden soll
     */
	public static boolean isFinalState(GraphNode node) {
		/*
         * Der Parameter node wird gebraucht, um auf den Elternknoten
         * zuzugreifen.
         */
		GraphNode parent = node.getParent();

		// der aktuelle Zustand (eigentlic this)
        AIGameState thisState = node.getState();
		/*
         * Hat jemand gewonnen? Um das zu überprüfen, muss der letzte Zug
         * ermittelt werden, da checkWinner den benötigt.
         */
		if (parent != null) {
			if (thisState.checkWinner() == Token.RED || thisState.checkWinner() == Token.YELLOW) {
				return true;
			}
		}
		
		// Spielbrett voll?
		if (thisState.boardIsFull()) {
			return true;
		} else {
			return false;
		}
	}

	/**
     * Berechnet die Nachfolgerzustände dieses Zustands.
     * @return Liste der Nachfolger
     */
	static ArrayList calculateSuccessors(AIGameState state) {
		// alle Nachfolger-Zustände bauen
		ArrayList successors = new ArrayList();
		// für jede Spalte
		for (int cols = 0; cols < state.board[0].length; cols++) {
			// Zeile suchen, in der noch Platz frei ist
			int row = -1;
			do {
				row++;
			} while (!(state.board[row][cols] == Token.EMPTY) && (row < Game.ROWS));

			if (row == Game.ROWS - 1) { // Spalte voll?
				continue;
			}
			
			// (zweidimensionales Array kopieren)
			Token[][] succ = new Token[Game.ROWS][Game.COLS];
			for (int k = 0; k < state.board.length; k++) {
				System.arraycopy(state.board[k], 0, succ[k], 0, state.board[k].length);
			}

			// Marke setzen
			succ[row][cols] = state.getWhoseTurn();
            MoveEvent move = new MoveEvent(state, cols);
            move.setRow(row);
            move.setToken(state.getWhoseTurn());

			// im Nachfolgerzustand ist der andere Spieler dran.
            Token newWhoseTurn = Token.EMPTY;
			if (state.getWhoseTurn() == Token.RED) {
				newWhoseTurn = Token.YELLOW;
			} else if (state.getWhoseTurn() == Token.YELLOW) {
				newWhoseTurn = Token.RED;
			} else {
				throw new RuntimeException(); // assert false
			}

            AIGameState succState = new AIGameState(newWhoseTurn, succ, move);
            //succState.setLastMoveEvent(move);
			successors.add(succState);
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
}
