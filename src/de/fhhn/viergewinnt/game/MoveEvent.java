package de.fhhn.viergewinnt.game;

import java.util.EventObject;

/**
 * Enth�lt die durch den Zug eines Spielers entstandenen �nderungen im Spiel.
 * @author $Author: malte $
 * @version $Revision: 1.2 $
 * @since LCA
 */
public class MoveEvent extends EventObject {
    private Token token;
    private int column;

    /**
     * XXX Nachdem ein MoveEvent von der View erzeugt wurde, muss der Controller
     * noch die Farbe des Spielers per setTken() hinzuf�gen!
     */
    public MoveEvent(Object source, /*Token t,*/ int column) {
        super(source);
        //this.token = t;
        this.column = column;
    }

    public void setToken(Token t) {
        this.token = t;
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
