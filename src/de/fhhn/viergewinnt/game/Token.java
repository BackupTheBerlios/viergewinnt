package de.fhhn.viergewinnt.game;

/**
 * Repräsentiert einen Spielstein. Ein Token liegt entweder noch "im Säckchen"
 * beim Player oder es steckt im Board.
 * 
 * @author $Author: kathrin $
 * @version $Revision: 1.3 $
 * @since LCA 
 */
public class Token {
    /**
     * @bidirectional 
     */
    private Player lnkrevPlayer;

    public Token(Player player) {
    }

    public Player getOwner() {
        return lnkrevPlayer;
    }
}
