package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enthält einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: kathrin $
 * @version $Revision: 1.4 $
 * @since IOC
 */
class GraphNode {
	private GraphNode parent;
	private AIGameState state;
	private ArrayList successors;

    /** Stellungsbewertung. */
	private int[] rating = new int[2]; // 0=red, 1=yellow

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
     * @param rating[0] = Bewertung für rot, rating[1] = Bewertung für gelb
     */
	public void setRating(int[] rating) {
        // XXX Wertebereich prüfen
		this.rating[0] = rating[0];
		this.rating[1] = rating[1];
	}

	/**
     * Gibt die Bewertung für den aktuellen Spielzustand zurück.
     * @return rating[0] = Bewertung für rot, rating[1] = Bewertung für gelb
     */
	public int[] getRating() {
		return rating; // XXX Kopie zurück
	}

	/**
     * Gibt den Elternknoten zurück.
	 * @return den Elternknoten, bzw. null, wenn das der Wurzelknoten ist
	 */
	public GraphNode getParent() {
		return parent; // XXX Kopie zurückgeben
	}

	/**
     * Gibt den aktuellen Spielzustand zurück.
     * @return Spielzustand
     */
	public AIGameState getState() {
		return state; // XXX Annahme: state unveränderbar
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
