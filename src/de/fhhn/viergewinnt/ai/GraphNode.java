package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enth�lt einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: p_herk $
 * @version $Revision: 1.9 $
 * @since IOC
 */
public class GraphNode {
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
		this.rating = rating;
	}

	/**
     * Gibt die Bewertung f�r den aktuellen Spielzustand zur�ck.
     * @return Bewertung
     */
	public int getRating() {
		return rating;
	}

	/**
     * Gibt den Elternknoten zur�ck.
	 * @return den Elternknoten, bzw. null, wenn das der Wurzelknoten ist
	 */
	public GraphNode getParent() {
        if (parent != null) {
            //try {
				return new GraphNode(parent); // Kopie zur�ckgeben
            //} catch (IllegalArgumentException e) {
            //    return null; // XXX Elternknoten ist der erste, deswegen die
            //}                // Exception. Nicht sch�n, funktioniert aber
        } else {
            return null;
        }
	}

	/**
     * Gibt den aktuellen Spielzustand zur�ck.
     * @return Spielzustand
     */
	public AIGameState getState() {
		return new AIGameState(state); //XXX  kopieren n�tig?
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
