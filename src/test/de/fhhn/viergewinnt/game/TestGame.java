package test.de.fhhn.viergewinnt.game;

import junit.framework.*;
import de.fhhn.viergewinnt.game.*;

/** JUnit TestCase für die Spiellogik.
 * @testfamily JUnit
 * @testkind testcase
 * @testsetup Default TestCase
 * @testedclass de.fhhn.viergewinnt.game.Game*/
public class TestGame extends TestCase {
    private Game game;

    /** Constructs a test case with the given name. */
    public TestGame(String name) {
        super(name);

    }

    /**
     * Sets up the fixture, for example, open a network connection.
     * This method is called before a test is executed.
     */
    protected void setUp() {
        game = new Game(Token.RED); // rot fängt an
    }

    /**
     * Tears down the fixture, for example, close a network connection.
     * This method is called after a test is executed.
     */
    protected void tearDown() {
        // Write your code here
    }

    /** Rot legt 4 Steine horizontal nebeneinander und gewinnt. */
    public void testRedWinsHorizontal() {
        int[] moves = {0, 0, 1, 1, 2, 2, 3};
        assertTrue(play(moves) == Token.RED);
    }

	public void testRedPreventsYellowFromWinningAndWinsVertical() {
		int[] moves = {1, 0, 1, 0, 1, 0, 0, 0, 1};
        assertTrue(play(moves) == Token.RED);
	}
	
	/** Vier unzusammenhängende rote Steine in einer Zeile. */
	public void testFourDisconnectedRedTokensInOneRow() {
		int[] moves = {0, 1, 2, 0, 3, 0, 4};
        assertTrue(play(moves) == Token.EMPTY);
	}
	
	public boolean compareBoards(Token[][] expectedBoard, Token[][] resultBoard) {
        boolean valid = true;
		for(int i=0; i < expectedBoard.length; i++) {
			for(int j=0; j < expectedBoard[i].length; j++) {
				if(expectedBoard[i][j] != resultBoard[i][j]) {
					valid = false;
                }
            }
        }
        return valid;
    }

	public void testNegativeMoveIndex() {
        int[] moves = {-1};
		try {
        	play(moves);
			assertTrue(false); // wird nie erreicht
        } catch (IllegalArgumentException ie) {
			assertTrue(true);
        }
	}

	public void testMoveIndexOutOfBounds() {
        int[] moves = {10};
		try {
        	play(moves);
       		assertTrue(false);
        } catch (IllegalArgumentException ie) {
			assertTrue(true);
        }
	}

    /** 
     * Spielt ein Spiel mit den übergeben Zügen nach und gibt den Gewinner
     * zurück. Rot fängt immer an.
     * @param moves Abwechselnd rote und gelbe Spielzüge (bzw. die
     * Spalten, in die ein Spielstein geworfen werden soll)
     */
    private Token play(int[] moves) {
        MoveEvent e;
        
        for (int i = 0; i < moves.length; i++) {
            e = new MoveEvent(this, moves[i]);
            
            if (i%2 == 0) { // Rot fängt an
                e.setToken(Token.RED);
            } else {
                e.setToken(Token.YELLOW);
            }
            
            game.accept(e);
        }
        return game.getWinner();
    }
    
}
