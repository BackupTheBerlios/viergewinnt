package de.fhhn.viergewinnt.game;

/**
 * Enthält alle benötigten Benutzer-Daten. Identifiziert einen Spieler.
 * @author $Author: kathrin $
 * @version $Revision: 1.6 $
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
