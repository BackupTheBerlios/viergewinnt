package de.fhhn.viergewinnt.game;

import java.util.Observable;

/**
 * Modell des Spiels "Vier Gewinnt" mit Regeln (Züge prüfen, Gewinner bestimmen)
 * und Zustand (Position der Spielsteine, wer gerade am Zug ist, wer eventuell
 * gewonnen hat). Akzeptiert Eingaben der beiden Player (z.B. einen Zug des
 * Benutzers). Ungültige Eingaben werden dabei einfach ignoriert (z.B. wenn
 * ein Spieler einen Zug macht, obwohl er nicht dran ist).
 * @author $Author: kathrin $
 * @version $Revision: 1.16 $
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
        return lastMoveEvent; // do we need to clone() ?;
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
                        equal = (board[i] [j] == board[i] [j + k]);
                        if (!equal) {
                            break;
                        }
                    }
                    return board[i] [j];
                }
            }
        }
        // Diagonale prüfen links unten nach rechts o


        return Token.EMPTY;
    }

    public void accept(MoveEvent m) {
        System.out.println("Game.accept(): move=" + m);
        if (isValid(m)) {
            System.out.println("Game.accept(): move ist gültig");
            save(m);
            checkWinner();
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