package de.fhhn.viergewinnt.game;

/**
 * Enthält Daten, die vom Benutzer gesehen werden: Position der Spielsteine
 * (oder macht das Hole?), wer gerade am Zug ist, wer eventuell gewonnen hat.
 * Akzeptiert einen Zug des Benutzers und bereitet ihn zur Überprüfung vor.
 * @author $Author: kathrin $
 * @version $Revision: 1.3 $
 * @since LCA 
 */
public class Board {
    /**
     * @supplierCardinality 42
     * @directed 
     */
    private Hole lnkHole;

    /**
     * @supplierCardinality 1
     * @directed 
     */
    private Game lnkGame;

    public void insertToken(int column, Player player){}
}
