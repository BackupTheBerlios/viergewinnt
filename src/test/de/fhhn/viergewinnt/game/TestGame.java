package test.de.fhhn.viergewinnt.game;

import junit.framework.*;
import de.fhhn.viergewinnt.game.*;

/** JUnit TestCase f�r die Spiellogik.
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
        game = new Game(Token.RED); // rot f�ngt an
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

    /** 
     * Spielt ein Spiel mit den �bergeben Z�gen nach und gibt den Gewinner
     * zur�ck. Rot f�ngt immer an.
     * @param moves Abwechselnd rote und gelbe Spielz�ge (bzw. die
     * Spalten, in die ein Spielstein geworfen werden soll)
     */
    private Token play(int[] moves) {
        MoveEvent e;
        
        for (int i = 0; i < moves.length; i++) {
            e = new MoveEvent(this, moves[i]);
            
            if (i%2 == 0) { // Rot f�ngt an
                e.setToken(Token.RED);
            } else {
                e.setToken(Token.YELLOW);
            }
            
            game.accept(e);
        }
        return game.getWinner();
    }
    
}
