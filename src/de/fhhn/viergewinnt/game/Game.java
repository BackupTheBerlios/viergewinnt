package de.fhhn.viergewinnt.game;

import java.util.Observable;

/**
 * Modell des Spiels "Vier Gewinnt" mit Regeln (Züge prüfen, Gewinner bestimmen)
 * und Zustand (Position der Spielsteine, wer gerade am Zug ist, wer eventuell
 * gewonnen hat). Akzeptiert Eingaben der beiden Player (z.B. einen Zug des
 * Benutzers). Ungültige Eingaben werden dabei einfach ignoriert (z.B. wenn
 * ein Spieler einen Zug macht, obwohl er nicht dran ist).
 * @author $Author: malte $
 * @version $Revision: 1.12 $
 * @since LCA
 * @stereotype Model
 */
public class Game extends Observable {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private Token whoseTurn;

    /**
     * Spielbrett mit 6 Zeilen und 7 Spalten. Die Spalten beginnen mit der
     * Zählung von unten.
     */
    private Token[] [] board;

    public Game() {
        whoseTurn = Token.RED; // XXX
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
        for (row = 0; !(board[row] [column] == Token.EMPTY); row++) { }
        board[row][column] = token;
        // der View Bescheid geben, dass sich was geändert hat
        setChanged();
        notifyObservers();
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
        }
        // richtiger Spieler?
        if (valid && (whoseTurn == m.getToken())) {
            valid = true;
        } else {
            valid = false;
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
        // Diagonale prüfen
        for (int i = 0, j = 0; (i < COLS) && (j < ROWS); i++, j++) { }
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
        } else {
            System.out.println("Game.accept(): move ist ungültig!");
            //Zug ungültig -> Fehler
        }
    }

    public Token[][] getBoard() {
        return board; // XXX mit arraycopy Kopie zurückgeben?
    }
}
