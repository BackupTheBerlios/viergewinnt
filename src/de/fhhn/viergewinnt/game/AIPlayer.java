package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.ui.View;
/**
 * Gleichzeitig Controller und View.
 * 
 * @author $Author: manuel $
 * @version $Revision: 1.5 $
 * @since LCA
 * @stereotype View, Controller
 */
public class AIPlayer extends Player implements View {
    // Wenn KI da Random raus!
    private Random randomizer = new Random();
	
    public AIPlayer(Game g, Token color) {
        super(g, color);
        g.addObserver(this); // als View registrieren
    }

    /** Hier eigentlich �berfl�ssig. */
    public void addMoveEventListener(MoveEventListener listener) {
    }

    /** Hier eigentlich �berfl�ssig. */
    public void removeMoveEventListener(MoveEventListener listener) {
    }

    public void update(Observable observable, Object obj) {
		if (gameModel.getWhoseTurn() == color) {
        	System.out.println("AIPlayer.update(): Pseudo-Move");
			// XXX Token immer in erste Spalte stecken.

		MoveEvent m = new MoveEvent(this, randomizer.nextInt(6));
    	    m.setToken(color);
        	gameModel.accept(m);
        }
    }
}
