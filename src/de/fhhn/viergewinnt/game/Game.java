package de.fhhn.viergewinnt.game;

import java.util.Observable;

/**
 * Modell des Spiels "Vier Gewinnt" mit Regeln (Züge prüfen, Gewinner bestimmen)
 * und Zustand (Position der Spielsteine, wer gerade am Zug ist, wer eventuell
 * gewonnen hat). Akzeptiert Eingaben der beiden Player (z.B. einen Zug des
 * Benutzers). Ungültige Eingaben werden dabei einfach ignoriert (z.B. wenn
 * ein Spieler einen Zug macht, obwohl er nicht dran ist).
 * @author $Author: malte $
 * @version $Revision: 1.45 $
 * @since LCA
 * @stereotype Model
 */
public class Game extends Observable {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private Token whoseTurn;

    /**
    * Zustand des Spiels mit Spielbrett.
    */	
    private GameState state;
	
    /** 
    * Hilfs-Spielbrett mit 6 Zeilen und 7 Spalten. Die Zeilen beginnen mit der 
    * Zählung von unten. Wird bei Änderungen aus state in makeMove aktualisiert.
    */
    private Token[][] board;

    /** 
     * Der Gewinner des Spiels. Solange noch niemand gewonnen hat, ist der
     * Wert Token.Empty.
     */
    private Token winner = Token.EMPTY;

    /**
     * Konstruktor.
     * @param beginner der Spieler, der den ersten Zug machen darf
     */
    public Game(Token beginner) {
        whoseTurn = beginner;
		state = new GameState(whoseTurn);
		board = state.getBoard();
    }

	/**
	 * Verarbeitet den Spielzug eines Spielers. Wenn der Spielzug ungültig ist
	 * (z.B. weil der enstpsrechende Spieler gerade nicht dran ist), wird er 
	 * ignoriert und.
	 * @param m Spielzug mit Token des ziehenden Spielers.
     * @returns false, wenn der Zug ungültig ist und ignoriert wird
	 */
    public boolean accept(MoveEvent m) {
		/*
		 * XXX Wie können wir verhindern, dass ein Spieler für den _anderen_
		 * einen Zug macht? Wenn beispielsweise der rote Spieler erst ein 
		 * MoveEvent mit Token.RED, und anschließend gleich ein zweites mit
		 * Token.YELLOW übergibt, hat er das Spiel sabotiert.
		 */

        System.out.println("------\nGame.accept(): move=" + m);

		if (m.getToken() != Token.RED && m.getToken() != Token.YELLOW) {
			throw new IllegalArgumentException();
		}

        
        if (isValid(m)) { // MoveEvent gültig?
            System.out.println("Game.accept(): move ist gültig");
            // XXX der andere Spieler ist dran:
            if (whoseTurn == Token.RED) {
                whoseTurn = Token.YELLOW;
            } else if (whoseTurn == Token.YELLOW) {
                whoseTurn = Token.RED;
            } else {
                // assert false
            }
            makeMove(m); // Zug ins interne Spielbrett eintragen
            winner = state.checkWinner(); // prüfen, ob ein Spieler gewonnen hat

            System.out.println("Game.accept():" + whoseTurn + " ist dran");

            // Observer benachrichtigen
	        setChanged();
    	    notifyObservers();

            return true;
        } else {
            System.out.println("Game.accept(): move ist ungültig!");
            return false;
        }
    }

	/**
	 * Prüft, ob das MoveEvent gültig ist. Ein MoveEvent ist gültig, wenn 
	 * <ul>
	 *   <li>der Spielstein in eine Spalte gesteckt wird, in der noch mindestens 
	 *	     ein Platz frei ist
	 *	 </li>
	 *   <li>der Spielstein die Farbe des Spielers hat, der gerade am Zug
	 *       ist
	 *   </li>
	 *   <li>noch niemand gewonnen hat
	 *   </li>
	 * </ul>
	 */
    private boolean isValid(MoveEvent m) {
        int column = m.getColumn();
        // assert (? < column && column < ?)
		
        boolean valid = false;
        // in Spalte noch ein Platz frei?
        if (board[ROWS - 1][column] == Token.EMPTY) {
            valid = true;
        } else {
            valid = false;
            System.out.println("Game.isValid(): falsche Spalte -> ungültiger Move");
        }
        // richtige Spielstein-Farbe?
        if (valid && (whoseTurn == m.getToken())) {
            valid = true;
        } else  if (valid) { // nur wenn der Zug nicht bereits ungültig ist
            valid = false;
            System.out.println("Game.isValid(): falsche Farbe -> ungültiger Move");
        }
		
		// gibt es schon einen Gewinner?
		if (valid && (!(winner == Token.RED)) && (!(winner == Token.YELLOW))) {
			valid = true;
		} else if (valid) { // nur wenn der Zug nicht bereits ungültig ist
			valid = false;
			System.out.println("Game.isValid(): es gibt schon einen Gewinner " +
                               "(" + winner + ") -> ungültiger Move");
		}
        return valid;
    }



	/**
	 * Trägt den Spielzug in das interne Spielbrett ein.
	 * @param m ein gültiger Spielzug
	 */
    private void makeMove(MoveEvent m) {
		// assert m.isValid();

        int column = m.getColumn();
        Token token = m.getToken();
		
        // die Zeile berechnen, in der der Spielstein landen soll
        int row = 0;
        while (!(board[row][column] == Token.EMPTY)) {
            row++;
        }

        board[row][column] = token; // Zug in Spielbrett eintragen
        m.setRow(row);

		state = new GameState(whoseTurn, board, m);
    }

    /**
     *
	 * Gibt den zuletzt gemachten Spielzug als MoveEvent zurück.
	 */
	public MoveEvent getLastMoveEvent() {
		return state.getLastMoveEvent();
    }

	/**
	 * Gibt eine Kopie des Spielfelds zurück.
	 */
    public Token[][] getBoard() {
		Token[][] boardCopy = new Token[ROWS][COLS];
    	for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, boardCopy[i], 0, board[i].length);
		}
        return boardCopy;
    }

    /** Welcher Spieler ist jetzt am Zug? */
    public Token getWhoseTurn() {
        return whoseTurn;
    }

    /** 
	 * Wer hat gewonnen? Gibt Token.EMPTY zurück, wenn noch niemand 
	 * gewonnen hat. 
	 */
    public Token getWinner() {
        return winner;
    }
}
