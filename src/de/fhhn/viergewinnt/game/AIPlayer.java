package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.ui.View;
/**
 * Gleichzeitig Controller und View.
 * 
 * @author $Author: malte $
 * @version $Revision: 1.4 $
 * @since LCA
 * @stereotype View, Controller
 */
public class AIPlayer extends Player implements View {
    public AIPlayer(Game g, Token color) {
        super(g, color);
        g.addObserver(this); // als View registrieren
    }

    /** Hier eigentlich überflüssig. */
    public void addMoveEventListener(MoveEventListener listener) {
    }

    /** Hier eigentlich überflüssig. */
    public void removeMoveEventListener(MoveEventListener listener) {
    }

    public void update(Observable observable, Object obj) {
		if (gameModel.getWhoseTurn() == color) {
        	System.out.println("AIPlayer.update(): Pseudo-Move");
			// XXX Token immer in erste Spalte stecken.
	        MoveEvent m = new MoveEvent(this, 0);
    	    m.setToken(color);
        	gameModel.accept(m);
        }
    }
}
