package de.fhhn.viergewinnt.game;

/**
 * Enth�lt die Regeln des Spiels (Z�ge pr�fen, Gewinner bestimmen).
 * @author $Author: kathrin $
 * @version $Revision: 1.3 $
 * @since LCA 
 */
public class Game {
    /**
     * @supplierCardinality 0..*
     * @directed 
     */
    private Move lnkMove;

    public boolean validate(Move move) {
    }
}
