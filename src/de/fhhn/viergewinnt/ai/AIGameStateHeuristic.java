package de.fhhn.viergewinnt.ai;

import de.fhhn.viergewinnt.game.*;

/** 
 * Die Bewetung der einzellen Stellungen
 *
 * @author $Author: kathrin $
 * @version $Revision: 1.2 $
 */
public class AIGameStateHeuristic {
    // speicher der verwendeten leeren Tokens, gewinnen kann man nur einmal
	private static boolean[][] emptyTokenBoard;
	private static int[] lastEmptyTokenPosition = new int[2];
	private static final int XAXIS = Game.ROWS;
	private static final int YAXIS = Game.COLS;
	
    /**
     * Heuristische Stellungsbewertung eines Zustandes
     * @param state der Spielzustand
	 * @return rating Bewertung
     */
	public static int ratePosition(GameState state) {
		emptyTokenBoard = new boolean[6][7]; // für diesen Zustand verwendete leere Tokens merken
		int[] rating = new int[2];
		
		int[] axisRating = rateAxes(state);
		int[] diagsRating = rateDiags(state);

		rating[0] = axisRating[0] + diagsRating[0];
		rating[1] = axisRating[1] + diagsRating[1];
		System.out.println("0 = "+rating[0]+"  1 =  "+rating[1]);

		if (state.getWhoseTurn() == Token.RED) {
			return rating[0] - rating[1];
        } else {
            return rating[1] - rating[0];
        }
	}

	/**
	 * Wird dieses leere Token schon einmal für einen anderen Tupel benutzt
	 * @param position die Position des Tokens
	 * @return boolean ja/nein
	 */	
	private static boolean useEmptyToken(int[] position) {
		if(emptyTokenBoard[position[1]][position[0]]) {
			return false;
		} else {
			emptyTokenBoard[position[1]][position[0]] = true;
			return true;
		}
	}

	/**
	 * Tokens zählen
	 * Zählt die Anzahl der Tokens für diesen Tupel, speichert den letzten leeren Token innerhalb dieses Tupels
	 * @param token der aktuelle Token
	 * @param position die Position des Tokens
	 * @param localValues die vorkommenden Tokens
	 * @return localValues gezählte Tokens
	 */	
	private static int[] countTokens(Token token, int[] position, int[] localValues) {
		if(token == Token.RED) {
			localValues[0]++;
		} else if (token == Token.YELLOW) {
			localValues[1]++;
		} else { // Token.EMPTY
			localValues[2]++;
			lastEmptyTokenPosition = position;
		}
		
		return localValues;
	}
	
	/**
	 * Gibt die letzte Position des leeren Tokens zurück
	 * @return lastEmptyTokenPosition die Position des letzten leeren Tokens
	 */
	private static int[] getLastEmptyTokenPosition() {
		return lastEmptyTokenPosition;
	}
	
	/**
	 * Bewertet die Anzahl der Tokens inerhalb des Tupels
	 * @param rating die aktuelle Bewertung des Tupels
	 * @param localValues die vorkommenden Tokens
	 * @return rating die neue Bewertung des Tupels
	 */
	private static int[] rateTuple(int[] rating, int[] localValues) {
		// wenn es nur einen leeren Token gibt und diesen nicht schon bei einer anderen Stellung verwendet wird
		if((localValues[2] == 1) && useEmptyToken(getLastEmptyTokenPosition())) {
			if(localValues[0] == 0) { // MAX
				rating[0]++;
			} else if (localValues[1] == 0) { // MIN
				rating[1]++;	
			}
		}
		
		return rating;
	}

	/**
	 * Bewertet die beiden Achsen
	 * @param state der Spielzustand
	 * @return rateing die Bewertung
	 */
	private static int[] rateAxes(GameState state) {
		int[] rating = new int[2];
		int[] xAxisRating = new int[2];
		int[] yAxisRating = new int[2];
		
		xAxisRating = rateAxis(state, state.getBoard().length);
		yAxisRating = rateAxis(state, state.getBoard()[0].length);
		
		rating[0] = xAxisRating[0] + yAxisRating[0];
		rating[1] = xAxisRating[1] + yAxisRating[1];
		
		return rating;
	}
	
	/**
	 * Bewertet eine Achse
	 * @param state der Spielzustand
	 * @param dimension die Achse
	 * @return rating die Bewertung
	 */
	private static int[] rateAxis(GameState state, int dimension) throws IllegalArgumentException {
		Token[][] board = state.getBoard();
		int[] rating = new int[2];
		int touple = 0;
		
		if(dimension == XAXIS) {
			touple = 4;
		} else if(dimension == YAXIS) {
			touple = 3;
		} else {
			throw new IllegalArgumentException("Dimension nicht richtig");
		}
		
		for(int i=0; i < dimension; i++) {
			for(int j=0; j < touple; j++) { // über alle Tupel in dieser Achse
				int[] localValues = new int[3]; // MAX, MIN, EMPTY
				int[] position = new int[2];
				
				for(int k=0; k < 4; k++) { // vierer Tupel betrachten: scanline
					if(dimension == XAXIS) { // Zeilen
						position[0] = j+k; // X
						position[1] = i; // Y
					} else if(dimension == YAXIS) { // Spalten
						position[0] = i; // X
						position[1] = j+k; // Y
					}
					
					localValues = countTokens(board[position[1]][position[0]],
                        position, localValues);
				}
				
				rating = rateTuple(rating, localValues);
			}
		}
		
		return rating;
	}
	
	/**
	 * Bewertet die beiden Diagonalen
	 * von links unten nach rechts oben (leftDiagRating)
	 * von rechts unten nach links oben (rightDiagRating)
	 * @param state ser Spielzustand
	 * @return rate die Bewertung
	 */
	private static int[] rateDiags(GameState state) {
		int[] rating = new int[2];
		int[] leftDiagRating = new int[2];
		int[] rightDiagRating = new int[2];
		
		int[][] leftStartPoints = {
								new int[] {0,0},
								new int[] {0,1},
								new int[] {0,2},
								new int[] {1,0},
								new int[] {1,1},
								new int[] {2,0},
								new int[] {2,1},
								new int[] {2,2},
								new int[] {3,0}
		};
		
		int[][] rightStartPoints = {
								new int[] {6,0},
								new int[] {6,1},
								new int[] {6,2},
								new int[] {5,0},
								new int[] {5,1},
								new int[] {4,0},
								new int[] {4,1},
								new int[] {4,2},
								new int[] {3,0}
		};
		
		leftDiagRating = rateDiag(state, leftStartPoints, 1);
		rightDiagRating = rateDiag(state, rightStartPoints, -1);
		
		rating[0] = leftDiagRating[0] + rightDiagRating[0];
		rating[1] = leftDiagRating[1] + rightDiagRating[1];
		
		return rating;
	}
	
	/**
	 * Bewertet eine Diagonale
	 * @param state der Spielzustand
	 * @param startPoints sie Startpunkte der Diagonalen
	 * @param direction die Richtung der Diagonalen
	 * @return rating die Bewertung
	 */
	private static int[] rateDiag(GameState state, int[][] startPoints, int direction) {
		Token[][] board = state.getBoard();
		int[] rating = new int[2];

		for(int i=0; i < startPoints.length; i++) { // Startpunkte "abgrasen"
			int[] localValues = new int[3]; // MAX, MIN, EMPTY
			int[] position = new int[2];
			
			for(int j=0; j < 4; j++) { // vierer Tupel betrachten: scanline
				position[0] = startPoints[i][0] + (j * direction); // X
				position[1] = startPoints[i][1] + j; // Y
				
				localValues = countTokens(board[position[1]][position[0]], position, localValues);
			 }
			 
			 rating = rateTuple(rating, localValues);
		}
		
		return rating;
	}
}
