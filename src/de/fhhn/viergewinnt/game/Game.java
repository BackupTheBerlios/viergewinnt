package de.fhhn.viergewinnt.game;

import java.util.Observable;

/**
 * Modell des Spiels "Vier Gewinnt" mit Regeln (Züge prüfen, Gewinner bestimmen)
 * und Zustand (Position der Spielsteine, wer gerade am Zug ist, wer eventuell
 * gewonnen hat). Akzeptiert Eingaben der beiden Player (z.B. einen Zug des
 * Benutzers). Ungültige Eingaben werden dabei einfach ignoriert (z.B. wenn
 * ein Spieler einen Zug macht, obwohl er nicht dran ist).
 * @author $Author: malte $
 * @version $Revision: 1.20 $
 * @since LCA
 * @stereotype Model
 */
public class Game extends Observable {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private Token whoseTurn;
    private MoveEvent lastMoveEvent;

    /** Spielbrett mit 6 Zeilen und 7 Spalten. Die Spalten beginnen mit der Zählung von unten. */
    private Token[] [] board;

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
        int row;
        for (row = 0; !(board[row] [column] == Token.EMPTY); row++) {
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

        /*
        // Zeilen prüfen
        for (int i = 0; i < ROWS; i++) { // für jede Zeile
            // Scanline
            for (int j = 0; j < COLS - 3; j++) { // für alle n-4 Elemente
                if (board[i] [j] != Token.EMPTY) {
                    boolean equal = false;
                    for (int k = 0; k < 4; k++) {
                        equal = (board[i] [j] == board[i] [j + k]);
                        if (!equal) {
                            break;
                        }
                    }
                    return board[i] [j];
                }
            }
        }
        // Spalten prüfen
        for (int j = 0; j < COLS; j++) { // für jede Spalte
            // Scanline
            for (int i = 0; i < ROWS - 3; i++) { // für alle n-3 Elemente
                if (board[i] [j] != Token.EMPTY) {
                    boolean equal = false;
                    for (int k = 0; k < 4; k++) {
                        equal = (board[i] [j] == board[i] [j + k]); //FIXME
                        if (!equal) {
                            break;
                        }
                    }
                    return board[i] [j];
                }
            }
        }
        */

        // Diagonale prüfen links unten nach rechts oben
		MoveEvent last = getLastMoveEvent();
        int row = last.getRow();
        System.out.println(row);
        int col = last.getColumn();
        Token token = last.getToken();

        int counterHorizontal = 1;

        // Auswertung horizontal linke Hälfte
        for (; col >= 0; col--) {
			if (token == board[row] [col]) { 
				counterHorizontal += 1;
            }
        }
		row = last.getRow();
        col = last.getColumn();

		// Auswertung horizontal rechte Hälfte
        for (; col < COLS; col++) {
			if (token == board[row] [col]) {
				counterHorizontal += 1;
            }
        }
		row = last.getRow();
        col = last.getColumn();

        int counterVertical = 1;

		// Auswertung vertikal
        for (; row >= 0; row--) {
			if (token == board[row] [col]) {
				counterVertical += 1;
            }
        }
		row = last.getRow();
        col = last.getColumn();

		 // zählt ob vier Tokens von einer Farbe in einer Diagonalen links
        //oben nach rechts unten liegen.
		int counterLupRlo = 1;

		// linker oberer Teil der Diagonale von links oben nach rechts unten.
        for (; row < ROWS && col >= 0; row++, col--) {
			if (token == board[row] [col]) {
				counterLupRlo += 1;
            } else {
				break; //eventuell Variable continue einführen, damit man aus
                		// for Schleife kommt sobald das if nicht mehr stimmt.
            }
        }
		row = last.getRow();
        col = last.getColumn();

		// rechter unterer Teil der Diagonale links oben nach rechts unten.
        for (; row >= 0 && col < COLS; row--, col++) {
			if (token == board[row] [col]) {
				counterLupRlo += 1;
            } else {
				break; //s.o.
            }
        }
		row = last.getRow();
        col = last.getColumn();

        // Counter für Diagonale rechts oben nach links unten.
		int counterRupLdown = 1;

        // rechter oberer Teil der Diagonalen von links unten nach rchts oben.
		for (; row < ROWS && col < COLS; row++, col++) {
			if (token == board[row] [col]) {
				counterRupLdown += 1;
            } else {
				break; // s.o.
            }
        }
		row = last.getRow();
        col = last.getColumn();

        // linker unterer Teil der Diagonalen links unten nach rechts oben.
        for (; row >= 0 && col >= 0; row--, col--) {
			if (token == board[row] [col]) {
				counterRupLdown += 1;
            } else {
				break; // s.o.
            }
        }

        if ((counterRupLdown == 4) || (counterRupLdown == 4)
            	|| (counterVertical == 4) || (counterHorizontal == 4)) {
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
            checkWinner(); // s.o.
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
        // return no reference, copy the array!
        return board;
    }

    /** Welcher Spieler ist jetzt am Zug? */
    public Token getWhoseTurn() {
        return whoseTurn;
    }
}
