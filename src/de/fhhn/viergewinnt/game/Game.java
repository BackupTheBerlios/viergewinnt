package de.fhhn.viergewinnt.game;

/**
 * Enth�lt die Regeln des Spiels (Z�ge pr�fen, Gewinner bestimmen).
 * Enth�lt Daten, die vom Benutzer gesehen werden: Position der Spielsteine
 * (oder macht das Hole?), wer gerade am Zug ist, wer eventuell gewonnen hat.
 * Akzeptiert einen Zug des Benutzers und bereitet ihn zur �berpr�fung vor.
 * @author $Author: malte $
 * @version $Revision: 1.7 $
 * @since LCA
 */
public class Game {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    /**
     * @supplierCardinality 0..*
     * @directed
     */
    private Move lnkMove;
    private Token whoseTurn;

    /**
     * Spielbrett mit 6 Zeilen und 7 Spalten. Die Spalten beginnen mit der
     * Z�hlung von unten.
     */
    Token[][] board;

    /** @bidirectional */
    private Player lnkrevPlayer;

    public Game() {
        board = new Token[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = Token.EMPTY;
            }
        }
    }

    void save(Move m) {
        int column = m.getColumn();
        Token token = m.getToken();
        int row;
        for (row = 0; !(board[row][column] == Token.EMPTY); row++) { }
        board[row][column] = token;
    }

    boolean isValid(Move m) {
        int column = m.getColumn();
        // assert Wertebereich eingehalten (0 < column < 6)
        boolean valid = false;
        // richtige Spalte?
        if (board[5][column] == Token.EMPTY) {
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

    Token checkWinner() {
        // Zeilen pr�fen
        for (int i = 0; i < ROWS; i++) { // f�r jede Zeile
            // Scanline
            for (int j = 0; j < COLS - 3; j++) { // f�r alle n-4 Elemente
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
        // Spalten pr�fen
        for (int j = 0; j < COLS; j++) { // f�r jede Spalte
            // Scanline
            for (int i = 0; i < ROWS - 3; i++) { // f�r alle n-3 Elemente
                if (board[i][j] != Token.EMPTY) {
                    boolean equal = false;
                    for (int k = 0; k < 4; k++) {
                        equal = (board[i][j] == board[i][j + k]);
                        if (!equal) {
                            break;
                        }
                    }
                    return board[i][j];
                }
            }
        }
        // Diagonale pr�fen
        for (int i = 0, j = 0; i < COLS && j < ROWS; i++, j++) {
        }
        return Token.EMPTY;
    }

    public void accept(Move m) {
        if (isValid(m)) {
            save(m);
            checkWinner();
        } else {
            //Zug ung�ltig -> Fehler
        }
    }
}
