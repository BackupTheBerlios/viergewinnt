package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.ui.View;
import de.fhhn.viergewinnt.ai.*;

/**
 * Gleichzeitig Controller und View.
 * @author $Author: p_herk $
 * @version $Revision: 1.28 $
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

        root = new GraphNode(new AIGameState(Token.RED), null); // FIXME: RED fängt immer an
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
			if (playerMove != null) {
            	executeMove(playerMove);
			}

            if (!AIGameState.isFinalState(root)) {// XXX wenn Spiel noch nicht zuende
	   	        MoveEvent m = calculateMove();
         		// den berechneten Spielzug an das Spiel übergeben. Falls er akzeptiert
		        // wird, diesen Zug im intern nachvollziehen
        		boolean isMoveEventValid = gameModel.accept(m);
		        if (isMoveEventValid) {
	    		    executeMove(m);
		        }
            } else {
             //   System.out.println("AIPlayer.update(): Spiel zuende");
            }
        } else {
           // System.out.println("AIPlayer.update(): KI nicht dran");
        }
    }

    private MoveEvent calculateMove() {
        GraphNode.expand(root, list, limit);
        ArrayList succNodes = root.getSuccessors();
        ListIterator iter = succNodes.listIterator();
        GraphNode lastRated;
        lastRated = (GraphNode) iter.next();
        while (iter.hasNext()) {
            GraphNode current = (GraphNode) iter.next();
            if (this.color == Token.RED) {
				if (lastRated.getRating() < current.getRating()) {
					lastRated = current;
				}
            } else {
				if (lastRated.getRating() > current.getRating()) {
					lastRated = current;
				}
            }
            
        }
        return lastRated.getState().getLastMoveEvent();
    }

    /**
     * Sucht im Spielbaum nach dem passenden nächsten Zustand und setzt
     * ihn als neuen root.
     * */
    private void executeMove(MoveEvent m) {
        //System.out.println("\tAIPlayer.executeMove(): m=" + m);
        if (m.getRow() == -1) { // ungültiger Move?
            throw new IllegalArgumentException("Move ungültig (row==-1)!");
        }

        // gesuchter Zustand = root.state + neuer Zug
        Token[][] successorsBoard = root.getState().getBoard();
        successorsBoard[m.getRow()][m.getColumn()] = m.getToken();
        Token nextTurn = root.getState().getWhoseTurn() == Token.RED ?
            Token.YELLOW : Token.RED;
        AIGameState successorsState = new AIGameState(nextTurn, successorsBoard, m);

        boolean foundSuccessor = false;
        ArrayList successors = root.getSuccessors();
        ListIterator iter = successors.listIterator();

        while (iter.hasNext()) {
            //System.out.println("\tAIPlayer.executeMove(): suche passenden Nachfolgerknoten...");
            GraphNode succ = (GraphNode) iter.next();
            AIGameState state = succ.getState();

			if (state.equals(successorsState)) {
				//System.out.println("\tAIPlayer.executeMove(): passender Nachfolger gefunden");
                root = succ;
                foundSuccessor = true;
                break;
            }
        }
        if (!foundSuccessor) {
			System.err.println("\tAIPlayer.executeMove(): kein passender Nachfolger gefunden -> Fehler!");
			throw new RuntimeException(); // besser assert false
        }
    }
}