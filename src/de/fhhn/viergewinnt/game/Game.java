package de.fhhn.viergewinnt.game;

/**
 * Enthält die Regeln des Spiels (Züge prüfen, Gewinner bestimmen).
 * 
 * Enthält Daten, die vom Benutzer gesehen werden: Position der Spielsteine
 * (oder macht das Hole?), wer gerade am Zug ist, wer eventuell gewonnen hat.
 * Akzeptiert einen Zug des Benutzers und bereitet ihn zur Überprüfung vor.
 * @author $Author: kathrin $
 * @version $Revision: 1.4 $
 * @since LCA 
 */
public class Game {
    /**
     * @supplierCardinality 0..*
     * @directed 
     */
    private Move lnkMove;

    private void save(Move m) {
    }

    private boolean validate(Move move) {
    }

    private void checkWinner() {
    }

    public void accept(Move move) {
    }
}
