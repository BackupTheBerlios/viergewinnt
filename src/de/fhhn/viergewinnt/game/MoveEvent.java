package de.fhhn.viergewinnt.game;

import java.util.EventObject;

/**
 * Enthält die durch den Zug eines Spielers entstandenen Änderungen im Spiel.
 * @author $Author: kathrin $
 * @version $Revision: 1.4 $
 * @since LCA
 */
public class MoveEvent extends EventObject {
    private Token token;
    private int column;
    private int row;

    /**
     * XXX Nachdem ein MoveEvent von der View erzeugt wurde, muss der Controller
     * noch die Farbe des Spielers per setTken() hinzufügen!
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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
