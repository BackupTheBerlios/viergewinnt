package de.fhhn.viergewinnt.ai;

import java.util.*;
import de.fhhn.viergewinnt.game.Token;

/** 
 * Enthält einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.20 $
 * @since IOC
 */
public class GraphNode {
	private GraphNode parent;
	private AIGameState state;
	private ArrayList successors;

    /** Stellungsbewertung. */
	private int rating = 0;

    /**
     * Erzeugt einen neuen Knoten für den Spielgraph
     * @param state Spielzustand in diesem Knoten
     * @param parent Elternknoten
     */
	public GraphNode(AIGameState state, GraphNode parent) {
		this.parent = parent;
		this.state = state; // XXX Annahme: state unveränderbar
		successors = new ArrayList();
	}

    /**
     * Kopierkonstruktor.
     * @param node Ein Knoten, darf nicht null sein
     */
    public GraphNode(GraphNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node darf nicht null sein!");
        }
        if (this.parent != null) {
	        this.parent = new GraphNode(node.parent);
        }
        this.state = new AIGameState(node.state);
        if (successors != null) {
	        this.successors = new ArrayList(successors);
        }
        this.rating = node.rating;
    }

    	/**
     * Baut einen Spielgraphen auf.
     * @param node Wurzel des Spielgraphen
     * @param list Container für bereits erzeugte Knoten
     * @param limit Suchtiefe (beeinflusst die Spielstärke)
     */
	public static void expand(GraphNode node, GraphNodeList list, int limit) {
        ///////////////////////////////////////////////////////////////////////
		// Abbruchbedingungen für die Rekursion
		AIGameState nodeState = node.getState();
		if (AIGameState.isFinalState(node)) { // Endzustand erreicht?
            // Min-Max-Bewertung
            rateFinalState(nodeState, node);
			return;
		} else if (limit == 0) { // noch kein Endzustand erreicht, aber Baum zuende?
			// heuristische Stellungsbewertung
			int nodeRating = AIGameStateHeuristic.ratePosition(nodeState);
			node.setRating(nodeRating);
			return;
		}
        //
        ///////////////////////////////////////////////////////////////////////

		// Vorbereitung für die Min-Max-Bewertung
        int b = prepareMinMaxRating(nodeState);

        ///////////////////////////////////////////////////////////////////////
		// Nachfolgerzustände von nodeState berechnen und nodeState mit
        // Min-Max bewerten
		ArrayList succStates = AIGameState.calculateSuccessors(nodeState);
		ListIterator it = succStates.listIterator();

		while (it.hasNext()) {
			// Für alle Nachfolgerzustände von nodeState
			AIGameState succState = (AIGameState) it.next();
            GraphNode succNode = null;
			if (list.contains(succState)) { // schon berechnet?
                System.out.println("GraphNode.expand(): Nachfolger schon berechnet!");
				succNode = list.getNode(succState);
                if (!node.successors.contains(succNode)) {
                    System.out.println("GraphNode.expand(): Nachfolger noch nicht verbunden");
					node.addSuccessor(succNode);
                } else {
					System.out.println("GraphNode.expand(): Nachfolger schon verbunden");
                }
				//continue;
			} else {
				// neuer Nachfolger mit Nachfolgerzustand und aktuellem Knoten
				// als Vorgänger
				succNode = new GraphNode(succState, node);
				node.addSuccessor(succNode);
				list.add(succNode);
            }
			expand(succNode, list, limit - 1); // Rekursion!

			// Min-Max-Bewertung
            if (nodeState.getWhoseTurn() == Token.RED) {
                b = Math.max(b, succNode.getRating());
            } else {
                b = Math.min(b, succNode.getRating());
            }
		}
		node.setRating(b);
        //
        ///////////////////////////////////////////////////////////////////////

	}

    private static int prepareMinMaxRating(AIGameState nodeState){
        int b;
        if (nodeState.getWhoseTurn() == Token.RED) {
            b = Integer.MIN_VALUE;
        } else {
            b = Integer.MAX_VALUE;
        }
        return b;
    }

    /**
     *  Min-Max-Bewertung eines Endzustandes.
     */
    private static void rateFinalState(AIGameState nodeState, GraphNode node){
        Token winner = nodeState.checkWinner();
        if (winner == Token.RED) { // FIXME
            node.setRating(Integer.MAX_VALUE);
        } else if (winner == Token.YELLOW) { // FIXME
            node.setRating(Integer.MIN_VALUE);
        } else if (winner == Token.EMPTY) { // FIXME
            node.setRating(0);
        }
    }

	/**
     * Fügt dem Knoten einen Nachfolger hinzu.
     * @param node enthält Folgezustand des aktuellen Spielzustandes
     */
	public void addSuccessor(GraphNode node) {
		successors.add(node);
	}

    /**
     * Gibt alle Nachfolger des Knotens zurück.
     * @return Liste mit <code>GraphNode</code>s
     */
	public ArrayList getSuccessors() {
		return new ArrayList(successors);
	}

    /**
     * Setzt die Bewertung des Spielzustands dieses Knotens.
     * @param rating Bewertung
     */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
     * Gibt die Bewertung für den aktuellen Spielzustand zurück.
     * @return Bewertung
     */
	public int getRating() {
		return rating;
	}

	/**
     * Gibt den Elternknoten zurück.
	 * @return den Elternknoten, bzw. null, wenn das der Wurzelknoten ist
	 */
	public GraphNode getParent() {
        if (parent != null) {
            //try {
				return new GraphNode(parent); // Kopie zurückgeben
            //} catch (IllegalArgumentException e) {
            //    return null; // XXX Elternknoten ist der erste, deswegen die
            //}                // Exception. Nicht schön, funktioniert aber
        } else {
            return null;
        }
	}

    public void setParent(GraphNode parent) {
        this.parent = parent;
    }

	/**
     * Gibt den aktuellen Spielzustand zurück.
     * @return Spielzustand
     */
	public AIGameState getState() {
		return new AIGameState(state); //XXX  kopieren nötig?
	}
	
	public int getSuccessorAmount() {
		int amount = successors.size();
		ListIterator iter = successors.listIterator();
		while (iter.hasNext()) {
			GraphNode succ = (GraphNode) iter.next();
			amount += succ.getSuccessorAmount();
		}
		return amount;
	}

	/** Wird für's Hashen benötigt. */
	public boolean equals(Object other) {
		if (other != null && getClass() == other.getClass()) {
			GraphNode otherGraphNode = (GraphNode) other;
			// Identität hängt von state ab
			return state.equals(otherGraphNode.state);
		} else {
			return false;
		}
	}

	/** Wird für's Hashen benötigt. */
	public int hashCode() {
		// Hash-Wert hängt von state ab
		return state.hashCode();
	}
}
