package de.fhhn.viergewinnt.game;

import java.util.Observable;

/**
 * Modell des Spiels "Vier Gewinnt" mit Regeln (Z�ge pr�fen, Gewinner bestimmen)
 * und Zustand (Position der Spielsteine, wer gerade am Zug ist, wer eventuell
 * gewonnen hat). Akzeptiert Eingaben der beiden Player (z.B. einen Zug des
 * Benutzers). Ung�ltige Eingaben werden dabei einfach ignoriert (z.B. wenn
 * ein Spieler einen Zug macht, obwohl er nicht dran ist).
 * @author $Author: manuel $
 * @version $Revision: 1.49 $
 * @since LCA
 * @stereotype Model
 */
public class Game extends Observable {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private Token whoseTurn;

    /**
    * Zustand des Spiels mit Spielbrett.
    */	
    private GameState state;
	
    /** 
    * Hilfs-Spielbrett mit 6 Zeilen und 7 Spalten. Die Zeilen beginnen mit der 
    * Z�hlung von unten. Wird bei �nderungen aus state in makeMove aktualisiert.
    */
    private Token[][] board;

    /** 
     * Der Gewinner des Spiels. Solange noch niemand gewonnen hat, ist der
     * Wert Token.Empty.
     */
    private Token winner = Token.EMPTY;
    
	/** 
	 * Der Gewinner des Spiels. Solange noch niemand gewonnen hat, ist der
	 * Wert Token.Empty.
	 */
	private String lastMessage= new String();

    /**
     * Konstruktor.
     * @param beginner der Spieler, der den ersten Zug machen darf
     */
    public Game(Token beginner) {
        whoseTurn = beginner;
		state = new GameState(whoseTurn);
		board = state.getBoard();
        setMessage("Spiel gestartet. Spieler "+ beginner + " beginnt!");
    }

	/**
	 * Verarbeitet den Spielzug eines Spielers. Wenn der Spielzug ung�ltig ist
	 * (z.B. weil der enstpsrechende Spieler gerade nicht dran ist), wird er 
	 * ignoriert und.
	 * @param m Spielzug mit Token des ziehenden Spielers.
     * @returns false, wenn der Zug ung�ltig ist und ignoriert wird
	 */
    public boolean accept(MoveEvent m) {
		/*
		 * XXX Wie k�nnen wir verhindern, dass ein Spieler f�r den _anderen_
		 * einen Zug macht? Wenn beispielsweise der rote Spieler erst ein 
		 * MoveEvent mit Token.RED, und anschlie�end gleich ein zweites mit
		 * Token.YELLOW �bergibt, hat er das Spiel sabotiert.
		 */
        //System.out.println("------\nGame.accept(): move=" + m);

        if (isValid(m)) { // MoveEvent g�ltig?
            //System.out.println("Game.accept(): move ist g�ltig");
            // XXX der andere Spieler ist dran:
            if (whoseTurn == Token.RED) {
                whoseTurn = Token.YELLOW;
            } else if (whoseTurn == Token.YELLOW) {
                whoseTurn = Token.RED;
            } else {
                // assert false
            }
            makeMove(m); // Zug ins interne Spielbrett eintragen
            winner = state.checkWinner(); // pr�fen, ob ein Spieler gewonnen hat

			if(winner != Token.EMPTY) {
				setMessage("Spieler "+ winner + " hat gewonnen!");
            } else {
				setMessage(whoseTurn + " ist dran");
            }

            //System.out.println("Game.accept():" + whoseTurn + " ist dran");

            // Observer benachrichtigen
	        setChanged();
    	    notifyObservers();

            return true;
        } else {
            //System.out.println("Game.accept(): move ist ung�ltig!");
            return false;
        }
    }

	/**
	 * Pr�ft, ob das MoveEvent g�ltig ist. Ein MoveEvent ist g�ltig, wenn 
	 * <ul>
	 *   <li>der Spielstein in eine Spalte gesteckt wird, in der noch mindestens 
	 *	     ein Platz frei ist
	 *	 </li>
	 *   <li>der Spielstein die Farbe des Spielers hat, der gerade am Zug
	 *       ist
	 *   </li>
	 *   <li>noch niemand gewonnen hat
	 *   </li>
	 * </ul>
	 */
    private boolean isValid(MoveEvent m) {
        int column = m.getColumn();
        // assert (? < column && column < ?)
		
        boolean valid = false;
        // in Spalte noch ein Platz frei?
        if (board[ROWS - 1][column] == Token.EMPTY) {
            valid = true;
        } else {
            valid = false;
            setMessage("Wurf ist ung�ltig. Die Spalte ist voll!");
            //System.out.println("Game.isValid(): falsche Spalte -> ung�ltiger Move");
        }
        // richtige Spielstein-Farbe?
        if (valid && (whoseTurn == m.getToken())) {
            valid = true;
        } else  if (valid) { // nur wenn der Zug nicht bereits ung�ltig ist
            valid = false;
            setMessage("Wurf ist ung�ltig. Spieler ist nicht dran!");
            //System.out.println("Game.isValid(): falsche Farbe -> ung�ltiger Move");
        }
		
		// gibt es schon einen Gewinner?
		if (valid && (!(winner == Token.RED)) && (!(winner == Token.YELLOW))) {
			valid = true;
		} else if (valid) { // nur wenn der Zug nicht bereits ung�ltig ist
			valid = false;
			setMessage("Es gibt schon einen Gewinner: "+ winner);
			//System.out.println("Game.isValid(): es gibt schon einen Gewinner " +
            //                   "(" + winner + ") -> ung�ltiger Move");
		}
        return valid;
    }



	/**
	 * Tr�gt den Spielzug in das interne Spielbrett ein.
	 * @param m ein g�ltiger Spielzug
	 */
    private void makeMove(MoveEvent m) {
		// assert m.isValid();

        int column = m.getColumn();
        Token token = m.getToken();
		
        // die Zeile berechnen, in der der Spielstein landen soll
        int row = 0;
        while (!(board[row][column] == Token.EMPTY)) {
            row++;
        }

        board[row][column] = token; // Zug in Spielbrett eintragen
        m.setRow(row);

		state = new GameState(whoseTurn, board, m);
    }

    /**
     *
	 * Gibt den zuletzt gemachten Spielzug als MoveEvent zur�ck.
	 */
	public MoveEvent getLastMoveEvent() {
		return state.getLastMoveEvent();
    }

	/**
	 * Gibt eine Kopie des Spielfelds zur�ck.
	 */
    public Token[][] getBoard() {
		Token[][] boardCopy = new Token[ROWS][COLS];
    	for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, boardCopy[i], 0, board[i].length);
		}
        return boardCopy;
    }

    /** Welcher Spieler ist jetzt am Zug? */
    public Token getWhoseTurn() {
        return whoseTurn;
    }

    /** 
	 * Wer hat gewonnen? Gibt Token.EMPTY zur�ck, wenn noch niemand 
	 * gewonnen hat. 
	 */
    public Token getWinner() {
        return winner;
    }

	private void setMessage(String message) {
		lastMessage = message;
    }

	/**
	 * Returns the lastMessage.
	 * @return String
	 */
	public String getLastMessage() {
		return lastMessage;
	}
}
