package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.ui.View;
import de.fhhn.viergewinnt.ai.*;

/**
 * Gleichzeitig Controller und View.
 * @author $Author: malte $
 * @version $Revision: 1.23 $
 * @since LCA
 * @stereotype View, Controller
 */
public class AIPlayer extends Player implements View {
    /** Die Suchtiefe im Spielbaum. */
    private int limit;
	
	public static final int WEAK=2;
	public static final int MEDIUM=3;
	public static final int STRONG=4;
	
    /** Der Knoten im Spielbaum, an dem das Spiel gerade ist. */
    private GraphNode root;
    private GraphNodeList list;

    public AIPlayer(int strength, Game g, Token color) {
        super(g, color);
        g.addObserver(this); // als View registrieren

        // Weil Rot immer anfängt, muss im 0-ten Zustand Gelb dran sein
        root = new GraphNode(new AIGameState(Token.YELLOW), null);
        list = new GraphNodeList();

        limit = strength;
        // Wurzel initialisieren
        GraphNode.expand(root, list, limit);
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

            if (!AIGameState.isFinalState(root)) {// XXX wenn Spiel noch nicht zuende
	   	        MoveEvent m = calculateMove();
         		// den berechneten Spielzug an das Spiel übergeben. Falls er akzeptiert
		        // wird, diesen Zug im intern nachvollziehen
        		boolean isMoveEventValid = gameModel.accept(m);
		        if (isMoveEventValid) {
	    		    executeMove(m);
		        }
            } else {
                System.out.println("AIPlayer.update(): Spiel zuende");
            }
        } else {
            System.out.println("AIPlayer.update(): KI nicht dran");
        }
    }

    private MoveEvent calculateMove() {
        GraphNode.expand(root, list, limit);
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
        return maxRated.getState().getLastMoveEvent();
    }

    private void executeMove(MoveEvent m) {
        System.out.println("\tAIPlayer.executeMove(): m=" + m);
        if (m.getRow() == -1) { // ungültiger Move?
            throw new IllegalArgumentException("Move ungültig (row==-1)!");
        }
        // richtigen Nachfolgernoten suchen
        boolean foundSuccessor = false;
        ArrayList successors = root.getSuccessors();
        ListIterator iter = successors.listIterator();
        while (iter.hasNext()) {
            System.out.println("\tAIPlayer.executeMove(): suche passenden Nachfolgerknoten...");
            GraphNode succ = (GraphNode) iter.next();
            AIGameState state = succ.getState();
            //Token[][] board = state.getBoard();
            //if (board[m.getRow()][m.getColumn()] != Token.EMPTY) {
			if (state.getLastMoveEvent().equals(m)) {
				System.out.println("\tAIPlayer.executeMove(): passender Nachfolger gefunden");
                root = succ;
                foundSuccessor = true;
                break;
            }
        }
        if (!foundSuccessor) {
			System.out.println("\tAIPlayer.executeMove(): kein passender Nachfolger gefunden -> Fehler!");
			throw new RuntimeException(); // besser assert false
        }
    }
}