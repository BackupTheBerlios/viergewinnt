package de.fhhn.viergewinnt.game;

/**
 * Enthält alle benötigten Benutzer-Daten. Identifiziert einen Spieler.
 * @author $Author: kathrin $
 * @version $Revision: 1.3 $
 * @since LCA 
 */
public class Player {
    /**
     * @bidirectional <{de.fhhn.viergewinnt.game.Token#lnkrevPlayer}>
     * @clientCardinality 1
     * @supplierCardinality 0..21 
     */
    private Token lnkToken;

    /**
     * @directed
     * @supplierCardinality 1 
     */
    private Board lnkBoard;
}
