package de.fhhn.viergewinnt.game;

/**
 * Enthält die durch den Zug eines Spielers entstandenen Änderungen im Spiel.
 * @author $Author: kathrin $
 * @version $Revision: 1.4 $
 * @since LCA 
 */
public class Move {
	private Token token;
    private int column;

    public Move(Token t, int column) {
		this.token = t;
        this.column = column;
    }

  //  public void confirm(){}

	public Token getToken(){
		return token;
    }

    public int getColumn(){
		return column;
    }
}
