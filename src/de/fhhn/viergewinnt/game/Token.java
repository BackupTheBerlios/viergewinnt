package de.fhhn.viergewinnt.game;

/**
 * Repr�sentiert einen Spielstein. Ein Token liegt entweder noch "im S�ckchen"
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
