package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Zustand des Spielfelds. Immutable.
 * @author $Author: p_herk $
 * @version $Revision: 1.11 $
 * @since IOC
 */
public class GameState {
    protected MoveEvent lastMoveEvent;

	protected Token whoseTurn = Token.EMPTY;

	/** Zustand des Spielfelds. */
	protected Token[][] board = new Token[Game.ROWS][Game.COLS];

    /** Ist dieser Zustand der erste? */
    // private boolean isFirstState = false;

    /**
     * Konstruktor für den ersten Spielzustand ohne Vorgänger.
     */
	public GameState(Token whoseTurn) {
		this.whoseTurn = whoseTurn;
		// board initialisieren
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = Token.EMPTY;
			}
		}

		// isFirstState = true;
	}

    /**
     * Konstruktor für die Spielzustände nach dem ersten Zug.
     */
	protected GameState(Token whoseTurn, Token[][] board) {
		this.whoseTurn = whoseTurn;
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
		}
	}

    /**
     * Konstruktor für die Spielzustände nach dem ersten Zug.
     * @param last der Zug, der zu diesem Zustand geführt hat
     */
	public GameState(Token whoseTurn, Token[][] board, MoveEvent last) {
		this(whoseTurn, board);
    	lastMoveEvent = new MoveEvent(last);
	}


	/** zählt wie viele Steine horizotal aneinanderliegen.
	* @return Anzahl der Steine die horizontal aneinanderliegen
	* @param lastMoveRow Zeile in die der letzte Stein eingeworfen wurde
	* @param lastMoveCol Spalte in die der letzte Stein eingeworfen wurde
	*/
	public int getCounterHorizontal(int lastMoveRow, int lastMoveCol) {
		int row = lastMoveRow;
		int col = lastMoveCol;
		int counterHorizontal = 1;
		Token token = whoseTurn;

		// Auswertung horizontal linke Hälfte
		while (col > 0) {
            col--;
			if (token == board[row][col]) {
				counterHorizontal += 1;
            } else {
				break;
			}
        }
		row = lastMoveRow;
        col = lastMoveCol;

		// Auswertung horizontal rechte Hälfte
        while (col < Game.COLS - 1) {
            col++;
			if (token == board[row][col]) {
                //System.out.println("Game.checkWinner(): horizontales gleiches Token gefunden: " + board[row][col]);
				counterHorizontal += 1;
            } else {
				break;
			}
        }
		return counterHorizontal;
	}

	/** zählt wie viele Steine vertical aneinanderliegen.
	* @return Anzahl der Steine die vertical aneinanderliegen
	* @param lastMoveRow Zeile in die der letzte Stein eingeworfen wurde
	* @param lastMoveCol Spalte in die der letzte Stein eingeworfen wurde
	*/
	public int getCounterVertical(int lastMoveRow, int lastMoveCol) {
		int row = lastMoveRow;
		int col = lastMoveCol;
		int counterVertical = 1;
		Token token = whoseTurn;

		// Auswertung vertikal
        while (row > 0) {
            row--;
			if (token == board[row] [col]) {
				counterVertical += 1;
            } else {
                break;
            }
        }
		return counterVertical;
	}

	/** zählt ob vier Tokens von einer Farbe in einer Diagonalen links
    * oben nach rechts unten liegen.
	* @return Anzahl der Steine in einer Diagonalen links oben rechts unten
	* @param lastMoveRow Zeile in die der letzte Stein eingeworfen wurde
	* @param lastMoveCol Spalte in die der letzte Stein eingeworfen wurde
	*/
	public int getCounterLupRdown(int lastMoveRow, int lastMoveCol) {
		int row = lastMoveRow;
		int col = lastMoveCol;
		int counterLupRdown = 1;
		Token token = whoseTurn;

		// linker oberer Teil der Diagonale von links oben nach rechts unten.
        while (row < Game.ROWS - 1 && col > 0) {
            row++;
            col--;
			if (token == board[row] [col]) {
				counterLupRdown += 1;
            } else {
				break;
            }
        }

		row = lastMoveRow;
        col = lastMoveCol;

		// rechter unterer Teil der Diagonale links oben nach rechts unten.
        while (row > 0 && col < Game.COLS - 1) {
            row--;
            col++;
			if (token == board[row] [col]) {
				counterLupRdown += 1;
            } else {
				break;
            }
        }

		return counterLupRdown;
	}

	/** Counter für Diagonale rechts oben nach links unten ermitteln.
	* @return Anzahl der Steine in einer Diagonalen rechts oben links unten
	* @param lastMoveRow Zeile in die der letzte Stein eingeworfen wurde
	* @param lastMoveCol Spalte in die der letzte Stein eingeworfen wurde
	*/
	public int getCounterRupLdown(int lastMoveRow, int lastMoveCol) {
		int row = lastMoveRow;
		int col = lastMoveCol;
		int counterRupLdown = 1;
		Token token = whoseTurn;

		// rechter oberer Teil der Diagonalen von links unten nach rchts oben.
		while (row < Game.ROWS - 1 && col < Game.COLS - 1) {
            row++;
            col++;
			if (token == board[row] [col]) {
				counterRupLdown += 1;
            } else {
				break;
            }
        }
		row = lastMoveRow;
        col = lastMoveCol;

        // linker unterer Teil der Diagonalen links unten nach rechts oben.
        while (row > 0 && col > 0) {
            row--;
            col--;
			if (token == board[row] [col]) {
				counterRupLdown += 1;
            } else {
				break;
            }
        }
		return counterRupLdown;
	}

    /** vereint die einzelnen Auswertungen (horizontal...)
	* @return Farbe des Spielers (Token.RED oder Toen.Yellow)
	* @param lastMoveRow Zeile in die der letzte Stein eingeworfen wurde
	* @param lastMoveCol Spalte in die der letzte Stein eingeworfen wurde
	*/
    public Token checkWinner(int lastMoveRow, int lastMoveCol) {
        int row = lastMoveRow;
        //System.out.println("checkWinner(): row=" + row);
        int col = lastMoveCol;
        //System.out.println("checkWinner(): col=" + col);
        Token token = whoseTurn;

        int counterHorizontal = getCounterHorizontal(row, col);
        int counterVertical = getCounterVertical(row, col);
		// zählt ob vier Tokens von einer Farbe in einer Diagonalen links
        // oben nach rechts unten liegen.
		int counterLupRdown = getCounterLupRdown(row, col);
        // Counter für Diagonale rechts oben nach links unten.
		int counterRupLdown = getCounterRupLdown(row, col);
/*
		System.out.println("counterRupLdown=" + counterRupLdown);
		System.out.println("counterLupRdown=" + counterLupRdown);
		System.out.println("counterVertical=" + counterVertical);
		System.out.println("counterHorizontal=" + counterHorizontal);
*/
        if ((counterRupLdown >= 4) || (counterLupRdown >= 4)
            	|| (counterVertical >= 4) || (counterHorizontal >= 4)) {
            //System.out.println(token + " hat gewonnen!");
			return token;
        } else {
			return Token.EMPTY;
        }
    }

	public Token[][] getBoard() {
		Token[][] boardCopy = new Token[Game.ROWS][Game.COLS];
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, boardCopy[i], 0, board[i].length);
		}

		return boardCopy;
	}

    public MoveEvent getLastMoveEvent() {
        return new MoveEvent(lastMoveEvent);
    }

    /*
    public void setLastMoveEvent(MoveEvent lastMoveEvent) {
        this.lastMoveEvent = lastMoveEvent;
    }
    */
/*
    public boolean isFirstState(){
		return isFirstState;
	}
*/
}
