package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Zustand des Spielfelds. Immutable.
 * @author $Author: malte $
 * @version $Revision: 1.2 $
 * @since IOC
 */
public class GameState {
	protected Token whoseTurn = Token.EMPTY;
	protected int remainingEmptyHoles = 9;

	/** Zustand des Spielfelds. */
	protected Token[][] board = new Token[Game.ROWS][Game.COLS];

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

    public Token checkWinner(int lastMoveRow, int lastMoveCol) {
        // Diagonale prüfen links unten nach rechts oben
        int row = lastMoveRow;
        System.out.println("checkWinner(): row=" + row);
        int col = lastMoveCol;

        System.out.println("checkWinner(): col=" + col);
        Token token = whoseTurn;

        int counterHorizontal = 1;

        // Auswertung horizontal linke Hälfte
        while (col > 0) {
            col--;
			if (token == board[row][col]) {
				counterHorizontal += 1;
            }
        }
		row = lastMoveRow;
        col = lastMoveCol;

		// Auswertung horizontal rechte Hälfte
        while (col < Game.COLS - 1) {
            col++;
			if (token == board[row][col]) {
                System.out.println("Game.checkWinner(): horizontales gleiches Token gefunden: " + board[row][col]);
				counterHorizontal += 1;
            }
        }
		row = lastMoveRow;
        col = lastMoveCol;

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
		row = lastMoveRow;
        col = lastMoveCol;

		 // zählt ob vier Tokens von einer Farbe in einer Diagonalen links
        //oben nach rechts unten liegen.
		int counterLupRdown = 1;

		// linker oberer Teil der Diagonale von links oben nach rechts unten.
        while (row < Game.ROWS - 1 && col > 0) {
            row++;
            col--;
			if (token == board[row] [col]) {
				counterLupRdown += 1;
            } else {
				break; //eventuell Variable continue einführen, damit man aus
                		// for Schleife kommt sobald das if nicht mehr stimmt.
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
				break; //s.o.
            }
        }
		row = lastMoveRow;
        col = lastMoveCol;

        // Counter für Diagonale rechts oben nach links unten.
		int counterRupLdown = 1;

        // rechter oberer Teil der Diagonalen von links unten nach rchts oben.
		while (row < Game.ROWS - 1 && col < Game.COLS - 1) {
            row++;
            col++;
			if (token == board[row] [col]) {
				counterRupLdown += 1;
            } else {
				break; // s.o.
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
				break; // s.o.
            }
        }
		System.out.println("counterRupLdown=" + counterRupLdown);
		System.out.println("counterLupRdown=" + counterLupRdown);
		System.out.println("counterVertical=" + counterVertical);
		System.out.println("counterHorizontal=" + counterHorizontal);
        if ((counterRupLdown >= 4) || (counterLupRdown >= 4)
            	|| (counterVertical >= 4) || (counterHorizontal >= 4)) {
            System.out.println(token + " hat gewonnen!");
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
}
