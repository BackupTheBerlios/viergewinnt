package de.fhhn.viergewinnt.game;

/**
 * Repr�sentiert einen Spielstein. Enth�lt Position des Spielsteins
 * [Oder macht das Board/Hole?]
 * @author $Author: malte $
 * @version $Revision: 1.2 $
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
