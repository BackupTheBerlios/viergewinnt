package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.ui.View;
import de.fhhn.viergewinnt.ai.*;

/**
 * Gleichzeitig Controller und View.
 * 
 * @author $Author: kathrin $
 * @version $Revision: 1.6 $
 * @since LCA
 * @stereotype View, Controller
 */
public class AIPlayer extends Player implements View {
	// Spielbaum
    private GraphNode root;
    private GraphNodeList list;
	
    public AIPlayer(Game g, Token color) {
        super(g, color);
        g.addObserver(this); // als View registrieren
        root = new GraphNode(new AIGameState(Token.RED), null);
		list = new GraphNodeList();
    }

    /** Hier eigentlich überflüssig. */
    public void addMoveEventListener(MoveEventListener listener) {
    }

    /** Hier eigentlich überflüssig. */
    public void removeMoveEventListener(MoveEventListener listener) {
    }

    public void update(Observable observable, Object obj) {
		if (gameModel.getWhoseTurn() == color) {
			/* FIXME Funktioniert noch nicht
            root.getState().expand(root, list, 4);

            // Nachfolger mit höchster Bewertung suchen.
            ArrayList succNodes = root.getSuccessors();
            ListIterator iter = succNodes.listIterator();
			GraphNode maxRated;
			maxRated = (GraphNode) iter.next();

            while (iter.hasNext()) {
				GraphNode current = (GraphNode) iter.next();
                if (maxRated.getRating() < current.getRating()) {
					maxRated = current;
                }

            }
			MoveEvent m = new MoveEvent(this, -1); //FIXME
    	    m.setToken(color);
        	gameModel.accept(m);
            */
        }
    }
}
