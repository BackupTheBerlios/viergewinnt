package de.fhhn.viergewinnt.game;

import java.util.Observable;

/**
 * Modell des Spiels "Vier Gewinnt" mit Regeln (Z�ge pr�fen, Gewinner bestimmen)
 * und Zustand (Position der Spielsteine, wer gerade am Zug ist, wer eventuell
 * gewonnen hat). Akzeptiert Eingaben der beiden Player (z.B. einen Zug des
 * Benutzers). Ung�ltige Eingaben werden dabei einfach ignoriert (z.B. wenn
 * ein Spieler einen Zug macht, obwohl er nicht dran ist).
 * @author $Author: kathrin $
 * @version $Revision: 1.36 $
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
    * Z�hlung von unten. Wird bei �nderungen aus state in makeMove aktualisiert.
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
		/*
        board = new Token[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = Token.EMPTY;
            }
        }
		*/
		board = state.getBoard();
    }

	/**
	 * Verarbeitet den Spielzug eines Spielers. Wenn der Spielzug ung�ltig ist
	 * (z.B. weil der enstpsrechende Spieler gerade nicht dran ist), wird er 
	 * ignoriert.
	 * @param m Spielzug mit Token des ziehenden Spielers.
	 */
    public void accept(MoveEvent m) {
		/*
		 * XXX Wie k�nnen wir verhindern, dass ein Spieler f�r den _anderen_
		 * einen Zug macht? Wenn beispielsweise der rote Spieler erst ein 
		 * MoveEvent mit Token.RED, und anschlie�end gleich ein zweites mit
		 * Token.YELLOW �bergibt, hat er das Spiel sabotiert.
		 */

        System.out.println("------\nGame.accept(): move=" + m);

		if (m.getToken() != Token.RED && m.getToken() != Token.YELLOW) {
			throw new IllegalArgumentException();
		}

        
        if (isValid(m)) { // MoveEvent g�ltig?
            System.out.println("Game.accept(): move ist g�ltig");
            makeMove(m); // Zug ins interne Spielbrett eintragen
            winner = checkWinner(); // pr�fen, ob ein Spieler gewonnen hat
            // XXX der andere Spieler ist dran:
            if (whoseTurn == Token.RED) {
                whoseTurn = Token.YELLOW;
            } else if (whoseTurn == Token.YELLOW) {
                whoseTurn = Token.RED;
            } else {
                // assert false
            }

            // Observer benachrichtigen
	        setChanged();
    	    notifyObservers();

            System.out.println("Game.accept():" + whoseTurn + " ist dran");
        } else {
            System.out.println("Game.accept(): move ist ung�ltig!");
            //Zug ung�ltig -> Fehler
        }
    }

	/**
	 * Pr�ft, ob das MoveEvent g�ltig ist. Ein MoveEvent ist g�ltig, wenn 
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
            System.out.println("Game.isValid(): falsche Spalte -> ung�ltiger Move");
        }
        // richtige Spielstein-Farbe?
        if (valid && (whoseTurn == m.getToken())) {
            valid = true;
        } else {
            valid = false;
            System.out.println("Game.isValid(): falsche Farbe -> ung�ltiger Move");
        }
		
		// gibt es schon einen Gewinner?
		if (valid && (!(winner == Token.RED)) && (!(winner == Token.YELLOW))) {
			valid = true;
		} else {
			valid = false;
			System.out.println("---> SPIELENDE");
		}
        return valid;
    }



	/**
	 * Tr�gt den Spielzug in das interne Spielbrett ein.
	 * @param m ein g�ltiger Spielzug
	 */
    private void makeMove(MoveEvent m) {
		// assert m.isValid();

        int column = m.getColumn();
        Token token = m.getToken();
		
        // die Zeile berechnen, in die der Spielstein landen soll
        int row = 0;
        while (!(board[row][column] == Token.EMPTY)) {
            row++;
        }

        board[row][column] = token; // Zug in Spielbrett eintragen
        m.setRow(row);

		state = new GameState(whoseTurn, board);
        state.setLastMoveEvent(m);
    }

	/**
	 * Pr�ft, ob ein Spieler gewonnen hat. XXX Was ist mit unentschieden? Was
	 * wird da zur�ckgegeben, wo wird das gepr�ft?
	 * @return die Farbe des Spielers, der gewonnen hat, oder Token.EMPTY, wenn
	 * noch niemand gewonnen hat
	 */
    private Token checkWinner() {
        MoveEvent last = state.getLastMoveEvent();
        return state.checkWinner(last.getRow(), last.getColumn());
    }

    /**
     *
	 * Gibt den zuletzt gemachten Spielzug als MoveEvent zur�ck.
	 */
	/*public MoveEvent getLastMoveEvent() {
		return state.getLastMoveEvent();
    }*/

	/**
	 * Gibt eine Kopie des Spielfelds zur�ck.
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
	 * Wer hat gewonnen? Gibt Token.EMPTY zur�ck, wenn noch niemand 
	 * gewonnen hat. 
	 */
    public Token getWinner() {
        return winner;
    }
}
