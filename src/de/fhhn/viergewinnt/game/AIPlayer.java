package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.ui.View;
import de.fhhn.viergewinnt.ai.*;

/**
 * Gleichzeitig Controller und View.
 * @author $Author: kathrin $
 * @version $Revision: 1.9 $
 * @since LCA
 * @stereotype View, Controller
 */
public class AIPlayer extends Player implements View {
    /** Der Knoten im Spielbaum, an dem das Spiel gerade ist. */
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
        if (gameModel.getWhoseTurn() == color) { // KI ist dran
            //  den letzten Zug des Spielers im Spielbaum nachvollziehen
	        MoveEvent playerMove = gameModel.getLastMoveEvent();
            executeMove(playerMove);

            if (!root.getState().isFinalState(root)) {// XXX wenn Spiel noch nicht zuende
	   	        calculateMove();
            } else {
                System.out.println("AIPlayer.update(): Spiel zuende");
            }
        } else {
            System.out.println("AIPlayer.update(): KI nicht dran");
        }
    }

    private void calculateMove() {
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
        MoveEvent bestMove = maxRated.getState().getLastMoveEvent();
        int col = bestMove.getColumn();
        MoveEvent m = new MoveEvent(this, col);
        m.setToken(color);
        gameModel.accept(m);
        executeMove(m);
    }

    private void executeMove(MoveEvent m) {
        // richtigen Nachfolgernoten suchen
        ArrayList successors = root.getSuccessors();
        ListIterator iter = successors.listIterator();
        while (iter.hasNext()) {
            GraphNode succ = (GraphNode) iter.next();
            GameState state = succ.getState();
            Token[][] board = state.getBoard();
            if (board[m.getRow()][m.getColumn()] != Token.EMPTY) {
                root = succ;
                break;
            }
        }
    }
}
