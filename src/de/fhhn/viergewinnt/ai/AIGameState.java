package de.fhhn.viergewinnt.ai;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * "Zustand des Spielfelds, Methode zum Teste ob Endzustand, Methode zur
 * Berechnung der Nachfolgerzustände". erweitert
 * de.fhhn.viergewinnt.game.GameState um KI-spezifische Funktionen.
 * @author $Author: p_herk $
 * @version $Revision: 1.12 $
 * @since IOC
 * @testcase test.de.fhhn.viergewinnt.ai.TestAIGameState
 */
public class AIGameState extends GameState {
    private boolean[][] emptyTokenBoard; //XXX
    
    
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
		super(whoseTurn, board);
	}

    /**
     * Kopierkonstruktor.
     */
    public AIGameState(AIGameState state) {
        super(state.whoseTurn);
        if (lastMoveEvent != null) {
	        lastMoveEvent = new MoveEvent(state.lastMoveEvent);
        }
		for (int i = 0; i < state.board.length; i++) {
			System.arraycopy(state.board[i], 0, this.board[i], 0,
                             state.board[i].length);
		}
    }

	/**
     * Baut einen Spielgraphen auf.
     * @param node Wurzel des Spielgraphen
     * @param list Container für bereits erzeugte Knoten
     * @param limit Suchtiefe (beeinflusst die Spielstärke)
     */
	public void expand(GraphNode node, GraphNodeList list, int limit) {
		// Abbruchbedingung
		AIGameState state = node.getState();
		if (state.isFinalState(node)) {
			// Min-Max-Bewertung
            int row = state.getLastMoveEvent().getRow();
            int col = state.getLastMoveEvent().getColumn();
            Token winner = state.checkWinner(row, col);
            if (winner == Token.RED) { // FIXME
                node.setRating(Integer.MAX_VALUE);
            } else if (winner == Token.YELLOW) { // FIXME
                node.setRating(Integer.MIN_VALUE);
            } else if (winner == Token.EMPTY) { // FIXME
                node.setRating(0);
            }
			return;
		} else if (limit == 0) {
			// heuristische Stellungsbewertung
			int rating = state.ratePosition();
			node.setRating(rating);
			return;
		}

		// für die Min-Max-Bewertung
        int b;
        if (state.whoseTurn == Token.RED) {
            b = Integer.MIN_VALUE; // FIXME oder -1?
        } else {
            b = Integer.MAX_VALUE; // FIXME oder 1?
        }

		// Nachfolgerzustände von state berechnen
		ArrayList succStates = state.calculateSuccessors();
		ListIterator it = succStates.listIterator();


		while (it.hasNext()) {
			// Für alle Nachfolgerzustände von state
			AIGameState succState = (AIGameState) it.next();
			if (list.contains(succState)) { // schon berechnet?
				GraphNode succNode = list.getNode(succState);
				node.addSuccessor(succNode);
				continue;
			}
			GraphNode succNode = new GraphNode(succState, node);
			node.addSuccessor(succNode);
			list.add(succNode);
			expand(succNode, list, limit - 1); // Rekursion!

			// Min-Max-Bewertung
            if (state.whoseTurn == Token.RED) {
                b = Math.max(b, succNode.getRating());
            } else {
                b = Math.min(b, succNode.getRating());
            }
            node.setRating(b);
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
	private int ratePosition() { //FIXME: bin nochnicht fertig!!!
		emptyTokenBoard = new boolean[6][7];
		
		int[] finalRating = new int[2];
		int[] rowRating = new int[2];
		int[] colRating = new int[2];
		int[] leftDiagRating = new int[2];
		int[] rightDiagRating = new int[2];
		
		rowRating = rateRows();
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



		if (whoseTurn == Token.RED) {
			return finalRating[0] - finalRating[1];
        } else {
            return finalRating[1] - finalRating[0];
        }
	}
	
	private int[] rateRows() {
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
     * @return true wenn in der obersten Spielbrettzeile kein Platz mehr ist
     */
	private boolean boardIsFull() {
		// oberste Reihe voll?
		for (int i = 0; i < board[Game.ROWS - 1].length; i++) {
			if (board[Game.ROWS - 1][i] == Token.EMPTY) {
				return false;
			}
		}
		return true;
	}
	
	/**
     * Berechnet die Nachfolgerzustände dieses Zustands.
     * @return Liste der Nachfolger
     */
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

			if (row == Game.ROWS - 1) { // Spalte voll?
				continue;
			}
			
			// (zweidimensionales Array kopieren)
			Token[][] succ = new Token[Game.ROWS][Game.COLS];
			for (int k = 0; k < board.length; k++) {
				System.arraycopy(board[k], 0, succ[k], 0, board[k].length);
			}

			// in den Nachfolgerzuständen ist der andere Spieler dran.
			if (whoseTurn == Token.RED) {
				whoseTurn = Token.YELLOW;
			} else if (whoseTurn == Token.YELLOW) {
				whoseTurn = Token.RED;
			} else {
				// assert false
			}
			// neue Marke setzen
			succ[row][cols] = whoseTurn;
            MoveEvent move = new MoveEvent(this, cols);
            move.setRow(row);
            AIGameState succState = new AIGameState(whoseTurn, succ, move);
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

    /** Nur zum Ausprobieren. */
	public static void main(String[] args) {
		GraphNode root = new GraphNode(new AIGameState(Token.RED), null);
		GraphNodeList list = new GraphNodeList();
		root.getState().expand(root, list, 4);
		System.out.println((root.getSuccessorAmount() +  1) + " Knoten");
	}
}
