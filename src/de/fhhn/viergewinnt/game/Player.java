package de.fhhn.viergewinnt.game;

/**
 * Spieler mit Farbe (Controller).
 * "If the user decides to interact, the controller takes charge.
 * It watches for user input, such as clicking or moving the mouse or pressing
 * keyboard keys. It decides what the interaction means, and asks the model to
 * update its data and/or the view to change the way it displays the data."
 * Unterklassen als (Action)Listener bei SwingUI bzw. TextUI registrieren.
 *
 * 
 * @author $Author: malte $
 * @version $Revision: 1.11 $
 * @since LCA
 * @stereotype Controller
 */
public abstract class Player {
    /** @directed */
    protected Game gameModel;

    /** Die Farbe des Spielers. */
    protected Token color;

    public Player(Game g, Token color) {
        this.gameModel = g;
        this.color = color;
    }
}
