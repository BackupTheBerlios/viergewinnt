package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enth�lt einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: kathrin $
 * @version $Revision: 1.5 $
 * @since IOC
 */
class GraphNode {
	private GraphNode parent;
	private AIGameState state;
	private ArrayList successors;

    /** Stellungsbewertung. */
	private int rating = 0;

    /**
     * Erzeugt einen neuen Knoten f�r den Spielgraph
     * @param state Spielzustand in diesem Knoten
     * @param parent Elternknoten
     */
	public GraphNode(AIGameState state, GraphNode parent) {
		this.parent = parent;
		this.state = state; // XXX Annahme: state unver�nderbar
		successors = new ArrayList();
	}

	/**
     * F�gt dem Knoten einen Nachfolger hinzu.
     * @param node enth�lt Folgezustand des aktuellen Spielzustandes
     */
	public void addSuccessor(GraphNode node) {
		successors.add(node);
	}

    /**
     * Gibt alle Nachfolger des Knotens zur�ck.
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
        // XXX Wertebereich pr�fen
		this.rating = rating;
	}

	/**
     * Gibt die Bewertung f�r den aktuellen Spielzustand zur�ck.
     * @return Bewertung
     */
	public int getRating() {
		return rating; // XXX Kopie zur�ck
	}

	/**
     * Gibt den Elternknoten zur�ck.
	 * @return den Elternknoten, bzw. null, wenn das der Wurzelknoten ist
	 */
	public GraphNode getParent() {
		return parent; // XXX Kopie zur�ckgeben
	}

	/**
     * Gibt den aktuellen Spielzustand zur�ck.
     * @return Spielzustand
     */
	public AIGameState getState() {
		return state; // XXX Annahme: state unver�nderbar
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

	/** Wird f�r's Hashen ben�tigt. */
	public boolean equals(Object other) {
		
		if (other != null && getClass() == other.getClass()) {
			GraphNode otherGraphNode = (GraphNode) other;
			// Identit�t h�ngt von state ab
			return state.equals(otherGraphNode.state);
		} else {
			return false;
		}
	}

	/** Wird f�r's Hashen ben�tigt. */
	public int hashCode() {
		// Hash-Wert h�ngt von state ab
		return state.hashCode();
	}
}
