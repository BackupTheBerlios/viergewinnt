package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enthält einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: p_herk $
 * @version $Revision: 1.8 $
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
