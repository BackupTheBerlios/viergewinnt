package de.fhhn.viergewinnt.ai;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * "Zustand des Spielfelds, Methode zum Teste ob Endzustand, Methode zur
 * Berechnung der Nachfolgerzustände". erweitert
 * de.fhhn.viergewinnt.game.GameState um KI-spezifische Funktionen.
 * @author $Author: malte $
 * @version $Revision: 1.16 $
 * @since IOC
 * @testcase test.de.fhhn.viergewinnt.ai.TestAIGameState
 */
public class AIGameState extends GameState {
    private static boolean[][] emptyTokenBoard; //XXX
    
    
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
            int row = thisState.getLastMoveEvent().getRow();
            int col = thisState.getLastMoveEvent().getColumn();
			if (thisState.checkWinner(row, col) == Token.RED || thisState.checkWinner(row, col) == Token.YELLOW) {
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
     * Heuristische Stellungsbewertung der aktuellen Zustandes.
     * @return rating Bewertung
     */
	static int ratePosition(GameState state) { //FIXME: bin nochnicht fertig!!!
		Token[][] board = state.getBoard();
		emptyTokenBoard = new boolean[6][7];
		
		int[] finalRating = new int[2];
		int[] rowRating = new int[2];
		int[] colRating = new int[2];
		int[] leftDiagRating = new int[2];
		int[] rightDiagRating = new int[2];
		
		rowRating = rateRows(state);
		//rateCols();
		//rateLeftDiags();
		//rateRightDiags();
		
		//TODO: auswertung (kummulieren, vergleichen)
		
        /*
         * XXX Soll nur noch für Min _oder_ Max funktionieren, wird dann
         * zweimal ausfgerufen
         */
		
		// sehr primitive Stellungsbewertung die nur überprüft, ob in einer
        // Spalte 3 Tokens der selben Farbe übereinander liegen, und die
        // Position darüber noch frei ist.
		for (int i = 0; i < board.length - 4; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if ((board[i][j] == board[i+1][j]) 
						&& (board[i][j] == board[i+2][j]) 
						&& (board[i+3][j] == Token.EMPTY)) {
					if (board[i][j] == Token.RED) {
						finalRating[0]++;
					} else if (board[i][j] == Token.YELLOW) {
						finalRating[1]++;
					}
				}
			}
		}



		if (state.getWhoseTurn() == Token.RED) {
			return finalRating[0] - finalRating[1];
        } else {
            return finalRating[1] - finalRating[0];
        }
	}
	
	private static int[] rateRows(GameState state) {
        Token[][] board = state.getBoard();
		int[] rating = new int[2];
		int[] emptyTokenPos = new int[2]; //XXX
		
		for(int i = 0; i < board.length; i++) { // anzahl der Zeilen
			for(int j=0; j < 4; j++) { // vierer tupel betrachten
				int localMax = 0;
				int localMin = 0;
				int localEmpty = 0;
				
				for(int k=0; k < 4; k++) { // scanline
					Token token = board[i][(j+k)];
					
					if(token == Token.RED) {
						localMax++;
					} else if (token == Token.YELLOW) {
						localMin++;
					} else { // Token.EMPTY
						localEmpty++;
						emptyTokenPos[0] = i;
						emptyTokenPos[1] = (j+k);
					}					
				}
				
				if(localEmpty == 1) {
					if(!emptyTokenBoard[emptyTokenPos[0]][emptyTokenPos[1]]) {
						emptyTokenBoard[emptyTokenPos[0]][emptyTokenPos[1]] = true;
						
						if(localMin == 0) {
							rating[0]++; //MAX	
						} else if (localMax == 0) {
							rating[1]++; //MIN	
						}
					}
				}
			}
		}

		return rating;	
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

			// in den Nachfolgerzuständen ist der andere Spieler dran.
            Token newWhoseTurn = Token.EMPTY;
			if (state.getWhoseTurn() == Token.RED) {
				newWhoseTurn = Token.YELLOW;
			} else if (state.getWhoseTurn() == Token.YELLOW) {
				newWhoseTurn = Token.RED;
			} else {
				// assert false
			}
			// neue Marke setzen
			succ[row][cols] = newWhoseTurn;
            MoveEvent move = new MoveEvent(state, cols);
            move.setRow(row);
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
