package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Zustand des Spielfelds. Immutable.
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since IOC
 */
class GameState {
	private Token whoseTurn = Token.EMPTY;
	private int remainingEmptyHoles = 9;

	/** Zustand des Spielfelds. */
	private Token[][] board = new Token[Game.ROWS][Game.COLS];

	public GameState(Token whoseTurn) {
		this.whoseTurn = whoseTurn;
		// board initialisieren
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = Token.EMPTY;
			}
		}
	}

	public GameState(Token whoseTurn, Token[][] board) {
		this.whoseTurn = whoseTurn;
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
		}
	}

	public Token[][] getBoard() {
		Token[][] boardCopy = new Token[Game.ROWS][Game.COLS];
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, boardCopy[i], 0, board[i].length);
		}

		return boardCopy;
	}
}
