package test.de.fhhn.viergewinnt.game;

import junit.framework.*;
import de.fhhn.viergewinnt.game.*;

/** JUnit TestCase. 
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

    public void testAccept() {
        fail("not implemented");
    }
}
