package de.fhhn.viergewinnt.game;

/**
 * Enthält die Regeln des Spiels (Züge prüfen, Gewinner bestimmen).
 * 
 * Enthält Daten, die vom Benutzer gesehen werden: Position der Spielsteine
 * (oder macht das Hole?), wer gerade am Zug ist, wer eventuell gewonnen hat.
 * Akzeptiert einen Zug des Benutzers und bereitet ihn zur Überprüfung vor.
 * @author $Author: kathrin $
 * @version $Revision: 1.5 $
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
     * Spielbrett mit 6 Zeilen und 7 Spalten. Die Spalten beginne mit der
     * Zählung von unten.
     */
    private int[][] board;

    /**
     * @bidirectional 
     */
    private Player lnkrevPlayer;

    public Game() {
		board = new int[ROWS][COLS];
    }

    private void save(Move m) {
        int column = m.getColumn();
        Token token = m.getToken();
    }

    private boolean isValid(Move m) {
        // XXX assert Wertebereich

		int column = m.getColumn();
        boolean valid = false;

		// richtige Spalte?
		if (board[5][column] == Token.EMPTY) {
			valid = true;
        } else {
			valid = false;
        }

		// richtiger Spieler?
        if (valid && whoseTurn == m.getToken()){
			valid = true;
        } else {
			valid = false;
        }
    }

    private void checkWinner() {
    }

    public void accept(Move m) {
        if (isValid(m)) {
	        save(m);
    	    checkWinner(m);
        } else {
            //Zug ungültig -> Fehler
        }
    }
}
