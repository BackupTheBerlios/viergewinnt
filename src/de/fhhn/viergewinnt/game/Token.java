package de.fhhn.viergewinnt.game;

/**
 * Repräsentiert einen Spielstein. Enthält Position des Spielsteins
 * [Oder macht das Board/Hole?]
 * @author Malte Knörr
 * @version 0.1
 * @since LCA
 */
public class Token {
    private Player owner;

    public Token(Player player) {
    }

    public Player getOwner() {
        return owner;
    }
}
