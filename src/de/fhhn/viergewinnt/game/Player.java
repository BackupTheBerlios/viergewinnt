package de.fhhn.viergewinnt.game;

/**
 * Enth�lt alle ben�tigten Benutzer-Daten. Identifiziert einen Spieler.
 * @author $Author: malte $
 * @version $Revision: 1.7 $
 * @since LCA
 */
public class Player {
    /**
     * @bidirectional <{de.fhhn.viergewinnt.game.Game#lnkrevPlayer}>
     * @clientCardinality 2
     * @supplierCardinality 1
     */
    private Game lnkGame;

    /**
     * @supplierCardinality 0..*
     * @directed
     */
    private Move lnkMove;

    public void makeYourTurn() {
    }
}
