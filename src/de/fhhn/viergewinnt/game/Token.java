package de.fhhn.viergewinnt.game;

/**
 * Konstanten, die gelbe und rote Spielsteine repräsentieren (
 * <a href="http://developer.java.sun.com/developer/Books/shiftintojava/page1.html#replaceenums">Details</a>
 * zur Implementierung).
 *
 * Achtung: wenn ein Token serialisiert werden soll  (z.B. um es im Rahmen des
 * Design Patterns "Command" über's Netz zu versenden), muss u.a. readResolve()
 * implementiert werden
 * (<a href="http://www.javaworld.com/javaworld/javatips/jw-javatip122.html">Details</a>)!
 * 
 * @author $Author: kathrin $
 * @version $Revision: 1.6 $
 * @since LCA 
 */
public class Token {
    public static final Token RED = new Token("red");
    public static final Token YELLOW = new Token("yellow");
    public static final Token EMPTY = new Token("empty");

    private final String name;

    private Token(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
