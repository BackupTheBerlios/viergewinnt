package de.fhhn.viergewinnt.game;

import java.util.EventObject;

/**
 * Enthält die durch den Zug eines Spielers entstandenen Änderungen im Spiel.
 * Objekte dieser Klasse sind <em>nicht</em> immutable.
 * @author $Author: p_herk $
 * @version $Revision: 1.9 $
 * @since LCA
 */
public class MoveEvent extends EventObject {
	/** 
	 * Der Spielstein, der mit diesem Spielzug eingeworfen wurde. Leere
	 * Spielsteine können nicht eingeworfen werden.
	 */
    private Token token = Token.EMPTY;
	
	/** Die Spalte, in die der Spielstein eingeworfen wurde. */
    private int column = -1;
	/** Die Zeile, in die der Spielstein eingeworfen wurde. */
    private int row = -1;

    /**
     * Konstruktor. XXX Nachdem ein MoveEvent von der View erzeugt wurde, muss der Controller
     * noch die Farbe des Spielers per setToken() hinzufügen!
     */
    public MoveEvent(Object source, int column) {
        super(source);

        if(column < 0 || column > Game.ROWS) {
			throw new IllegalArgumentException();
        }

        this.column = column;
    }

	/**
	 * Kopierkonstruktor. Diskussion über das Für und Wider von clone in 
	 * "Effective Java" von Joshua Bloch, Seite 45.
	 */
	public MoveEvent(MoveEvent move) {
		super(move.getSource());
		column = move.column;
        row = move.row;
        token = move.token;
        /*
		if (move.getRow() != -1) {
			row = move.getRow();
		}
		if (move.getToken() != Token.EMPTY) {
			token = move.getToken();
		}
        */
	}


    public void setToken(Token t) {
		if (t == Token.EMPTY) {
			throw new IllegalArgumentException(
				"Mit einem leeren Spielstein kann kein Spielzug gemacht werden!");
		}
        this.token = t;
    }

    public Token getToken() {
        return token;
    }

    public int getColumn() {
        return column;
    }


    public String toString() {
        return "MoveEvent[token=" + token + ", column=" + column
           + ", row=" + row + "]";
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean equals(Object other) {
        if (other != null && getClass() == other.getClass()) {
			MoveEvent otherMoveEvent = (MoveEvent) other;
			// Identität hängt von row und column ab
			return (row == otherMoveEvent.row && column == otherMoveEvent.column);
		} else {
			return false;
		}
    }
}
