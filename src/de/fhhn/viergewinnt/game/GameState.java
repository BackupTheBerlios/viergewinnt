package de.fhhn.viergewinnt.game;

import de.fhhn.viergewinnt.game.*;

/**
 * Zustand des Spielfelds. Immutable.
 * @author $Author: manuel $
 * @version $Revision: 1.15 $
 * @since IOC
 */
public class GameState {
    protected MoveEvent lastMoveEvent;

	private Token whoseTurn = Token.EMPTY;

	/** Zustand des Spielfelds. */
	protected Token[][] board = new Token[Game.ROWS][Game.COLS];

    /** Ist dieser Zustand der erste? */
    // private boolean isFirstState = false;

    /**
     * Konstruktor f�r den ersten Spielzustand ohne Vorg�nger.
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
     * Konstruktor f�r die Spielzust�nde nach dem ersten Zug.
     */
	protected GameState(Token whoseTurn, Token[][] board) {
		this.whoseTurn = whoseTurn;
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
		}
	}

    /**
     * Konstruktor f�r die Spielzust�nde nach dem ersten Zug.
     * @param last der Zug, der zu diesem Zustand gef�hrt hat
     */
	public GameState(Token whoseTurn, Token[][] board, MoveEvent last) {
		this(whoseTurn, board);
    	lastMoveEvent = new MoveEvent(last);
	}

    /**
     * Pr�ft ob ein Spieler gewonnen hat.
     * Vereint die einzelnen Auswertungen (horizontal, vertikal, ...)
     * 
     * XXX Was ist mit unentschieden? Was wird da zur�ckgegeben, wo wird das
     * gepr�ft?
     *
	 * @param lastMoveRow Zeile in die der letzte Stein eingeworfen wurde
	 * @param lastMoveCol Spalte in die der letzte Stein eingeworfen wurde
 	 * @return Farbe des Spielers der gewonnen hat (Token.RED oder Token.Yellow),
     * sonst Token.EMPTY
 	*/
    public Token checkWinner() { // XXX Parameter �berfl�ssig
   		int row = lastMoveEvent.getRow();
		int col = lastMoveEvent.getColumn();
		Token token = lastMoveEvent.getToken();

        int counterHorizontal = getCounterHorizontal(row, col, token);
        int counterVertical = getCounterVertical(row, col, token);
		// z�hlt ob vier Tokens von einer Farbe in einer Diagonalen links
        // oben nach rechts unten liegen.
		int counterLupRdown = getCounterLupRdown(row, col, token);
        // Counter f�r Diagonale rechts oben nach links unten.
		int counterRupLdown = getCounterRupLdown(row, col, token);

        if ((counterRupLdown >= 4) || (counterLupRdown >= 4)
            	|| (counterVertical >= 4) || (counterHorizontal >= 4)) {
            //System.out.println(token + " hat gewonnen!");
			return token;
        } else {
			return Token.EMPTY;
        }
    }

	/**
     * Z�hlt wie viele Steine horizotal aneinanderliegen.
     * 
	 * @param row Zeile in die der letzte Stein eingeworfen wurde
	 * @param col Spalte in die der letzte Stein eingeworfen wurde
     * @param token Farbe des zuletzt eingeworfenen Steins
	 * @return Anzahl der Steine die horizontal aneinanderliegen
	*/
	private int getCounterHorizontal(int row, int col, Token token) {
        int tmpRow = row;
        int tmpCol = col;
		int counterHorizontal = 1;

		// Auswertung horizontal linke H�lfte
		while (col > 0) {
            col--;
			if (token == board[row][col]) {
				counterHorizontal += 1;
            } else {
				break;
			}
        }

		// Auswertung horizontal rechte H�lfte
		row = tmpRow;
        col = tmpCol;

        while (col < Game.COLS - 1) {
            col++;
			if (token == board[row][col]) {
				counterHorizontal += 1;
            } else {
				break;
			}
        }
		return counterHorizontal;
	}

	/** z�hlt wie viele Steine vertical aneinanderliegen.
	* @return Anzahl der Steine die vertical aneinanderliegen
	* @param lastMoveRow Zeile in die der letzte Stein eingeworfen wurde
	* @param lastMoveCol Spalte in die der letzte Stein eingeworfen wurde
	*/
	private int getCounterVertical(int row, int col, Token token) {
		int counterVertical = 1;

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

	/**
     * Z�hlt ob vier Tokens von einer Farbe in einer Diagonalen links
     * oben nach rechts unten liegen.
	 * @param row Zeile in die der letzte Stein eingeworfen wurde
	 * @param col Spalte in die der letzte Stein eingeworfen wurde
     * @param token Farbe des zuletzt eingeworfenen Steins
  	 * @return Anzahl der Steine in einer Diagonalen links oben rechts unten
	 */
	private int getCounterLupRdown(int row, int col, Token token) {
        int tmpRow = row;
        int tmpCol = col;
		int counterLupRdown = 1;

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

		row = tmpRow;
        col = tmpCol;

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

	/**
     * Counter f�r Diagonale rechts oben nach links unten ermitteln.
	 * @param row Zeile in die der letzte Stein eingeworfen wurde
	 * @param col Spalte in die der letzte Stein eingeworfen wurde
     * @param token Farbe des zuletzt eingeworfenen Steins
	 * @return Anzahl der Steine in einer Diagonalen rechts oben links unten
	*/
	private int getCounterRupLdown(int row, int col, Token token) {
		int tmpRow = row;
		int tmpCol = col;
		int counterRupLdown = 1;

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
		row = tmpRow;
        col = tmpCol;

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

	public Token[][] getBoard() {
		Token[][] boardCopy = new Token[Game.ROWS][Game.COLS];
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, boardCopy[i], 0, board[i].length);
		}

		return boardCopy;
	}

    /**
     * @return den letzten Spielzug bzw. null, wenn es keinen letzten Spielzug
     * gibt (weil dies der erste Spielzustand ist)
     */
    public MoveEvent getLastMoveEvent() {
        if (lastMoveEvent != null) {
	        return new MoveEvent(lastMoveEvent);
        } else {
            return null;
        }
    }

    /**
     * @return true wenn in der obersten Spielbrettzeile kein Platz mehr ist
     */
	protected boolean boardIsFull() { // XXX Warum  protected und nicht private?
		// oberste Reihe voll?
		for (int i = 0; i < board[Game.ROWS - 1].length; i++) {
			if (board[Game.ROWS - 1][i] == Token.EMPTY) {
				return false;
			}
		}
		return true;
	}

    public Token getWhoseTurn() {
        return whoseTurn;
    }
}
