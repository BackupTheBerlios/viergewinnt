package de.fhhn.viergewinnt.game;

import java.util.EventObject;

/**
 * Enthält die durch den Zug eines Spielers entstandenen Änderungen im Spiel.
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since LCA
 */
public class MoveEvent extends EventObject {
    private Token token;
    private int column;

    public MoveEvent(Token t, int column) {
        super(t);
        this.token = t;
        this.column = column;
    }

    //  public void confirm(){}
    public Token getToken() {
        return token;
    }

    public int getColumn() {
        return column;
    }

    public String toString() {
        return "MoveEvent[token=" + token + ", column=" + column + "]";
    }
}
