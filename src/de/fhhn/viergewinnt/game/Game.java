package de.fhhn.viergewinnt.game;

import java.util.Observable;

/**
 * Modell des Spiels "Vier Gewinnt" mit Regeln (Züge prüfen, Gewinner bestimmen)
 * und Zustand (Position der Spielsteine, wer gerade am Zug ist, wer eventuell
 * gewonnen hat). Akzeptiert Eingaben der beiden Player (z.B. einen Zug des
 * Benutzers). Ungültige Eingaben werden dabei einfach ignoriert (z.B. wenn
 * ein Spieler einen Zug macht, obwohl er nicht dran ist).
 * @author $Author: kathrin $
 * @version $Revision: 1.24 $
 * @since LCA
 * @stereotype Model
 */
public class Game extends Observable {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private Token whoseTurn;
    private MoveEvent lastMoveEvent;

    /** 
	 * Spielbrett mit 6 Zeilen und 7 Spalten. Die Spalten beginnen mit der 
	 * Zählung von unten. 
	 */
    private Token[] [] board;

    /** 
     * Der Gewinner des Spiels. Solange noch niemand gewonnen hat, ist der
     * Wert Token.Empty.
     */
    private Token winner;

    public Game(Token beginner) {
        whoseTurn = beginner;
        board = new Token[ROWS] [COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i] [j] = Token.EMPTY;
            }
        }
    }


    private void save(MoveEvent m) {
        int column = m.getColumn();
        Token token = m.getToken();
        // die richtige Zeile berechnen
        int row = 0;
        //for (row = 0; !(board[row] [column] == Token.EMPTY); row++) {
        //}
        while (!(board[row] [column] == Token.EMPTY)) {
            row++;
        }
        board[row] [column] = token;
        // Move is valid and will be saved
        m.setRow(row);
        lastMoveEvent = m;
    }

	/* This method will be called from the observers
	 *
     */

    public MoveEvent getLastMoveEvent() {
        // we need this for remote and ai player
        // not checking
		return new MoveEvent(lastMoveEvent);
    }


    private boolean isValid(MoveEvent m) {
        int column = m.getColumn();
        // assert Wertebereich eingehalten (0 < column < 6)
        boolean valid = false;
        // richtige Spalte?
        if (board[5] [column] == Token.EMPTY) {
            valid = true;
        } else {
            valid = false;
            System.out.println("Game.isValid(): falsche Spalte -> ungültiger Move");
        }
        // richtiger Spieler?
        if (valid && (whoseTurn == m.getToken())) {
            valid = true;
        } else {
            valid = false;
            System.out.println("Game.isValid(): falscher Spieler -> ungültiger Move");
        }
        return valid;
    }



    private Token checkWinner() {
        // Diagonale prüfen links unten nach rechts oben
		MoveEvent last = getLastMoveEvent();
        int row = last.getRow();
        System.out.println("checkWinner(): row=" + row);
        int col = last.getColumn();
        System.out.println("checkWinner(): col=" + col);
        Token token = last.getToken();

        int counterHorizontal = 1;

        // Auswertung horizontal linke Hälfte
        while (col > 0) {
            col--;
			if (token == board[row][col]) {
				counterHorizontal += 1;
            }
        }
		row = last.getRow();
        col = last.getColumn();

		// Auswertung horizontal rechte Hälfte
        while (col < COLS - 1) {
            col++;
			if (token == board[row][col]) {
                System.out.println("Game.checkWinner(): horizontales gleiches Token gefunden: " + board[row][col]);
				counterHorizontal += 1;
            }
        }
		row = last.getRow();
        col = last.getColumn();

        int counterVertical = 1;

		// Auswertung vertikal
        while (row > 0) {
            row--;
			if (token == board[row] [col]) {
				counterVertical += 1;
            } else {
                break;
            }
        }
		row = last.getRow();
        col = last.getColumn();

		 // zählt ob vier Tokens von einer Farbe in einer Diagonalen links
        //oben nach rechts unten liegen.
		int counterLupRdown = 1;

		// linker oberer Teil der Diagonale von links oben nach rechts unten.
        while (row < ROWS && col > 0) {
            row++;
            col--;
			if (token == board[row] [col]) {
				counterLupRdown += 1;
            } else {
				break; //eventuell Variable continue einführen, damit man aus
                		// for Schleife kommt sobald das if nicht mehr stimmt.
            }
        }
		row = last.getRow();
        col = last.getColumn();

		// rechter unterer Teil der Diagonale links oben nach rechts unten.
        while (row > 0 && col < COLS) {
            row--;
            col++;
			if (token == board[row] [col]) {
				counterLupRdown += 1;
            } else {
				break; //s.o.
            }
        }
		row = last.getRow();
        col = last.getColumn();

        // Counter für Diagonale rechts oben nach links unten.
		int counterRupLdown = 1;

        // rechter oberer Teil der Diagonalen von links unten nach rchts oben.
		while (row < ROWS && col < COLS) {
            row++;
            col++;
			if (token == board[row] [col]) {
				counterRupLdown += 1;
            } else {
				break; // s.o.
            }
        }
		row = last.getRow();
        col = last.getColumn();

        // linker unterer Teil der Diagonalen links unten nach rechts oben.
        while (row > 0 && col > 0) {
            row--;
            col--;
			if (token == board[row] [col]) {
				counterRupLdown += 1;
            } else {
				break; // s.o.
            }
        }
		System.out.println("counterRupLdown=" + counterRupLdown);
		System.out.println("counterLupRdown=" + counterLupRdown);
		System.out.println("counterVertical=" + counterVertical);
		System.out.println("counterHorizontal=" + counterHorizontal);
        if ((counterRupLdown >= 4) || (counterLupRdown >= 4)
            	|| (counterVertical >= 4) || (counterHorizontal >= 4)) {
            System.out.println(token + " hat gewonnen!");
			return token;
        } else {
			return Token.EMPTY;
        }
    }

    public void accept(MoveEvent m) {
        System.out.println("Game.accept(): move=" + m);
        if (isValid(m)) {
            System.out.println("Game.accept(): move ist gültig");
            save(m); // Reihenfolge wichtig!
            winner = checkWinner(); // s.o.
            // XXX der andere ist dran:
            if (whoseTurn == Token.RED) {
                whoseTurn = Token.YELLOW;
            } else if (whoseTurn == Token.YELLOW) {
                whoseTurn = Token.RED;
            } else {
                // assert false
            }
           // notify observers, the model has changed
	        setChanged();
    	    notifyObservers();

            System.out.println("Game.accept():" + whoseTurn + " ist dran");
        } else {
            System.out.println("Game.accept(): move ist ungültig!");
            //Zug ungültig -> Fehler
        }
    }

	/* This method will be called from the observers
	 *
     */

    public Token[] [] getBoard() {
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
