package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enth�lt einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.3 $
 * @since IOC
 */
class GraphNode {
	private GraphNode parent;
	private AIGameState state;
	private ArrayList successors;
	private int[] rating = new int[2]; // 0=red, 1=yellow

	public GraphNode(AIGameState state, GraphNode parent) {
		this.parent = parent;
		this.state = state; // XXX Annahme: state unver�nderbar
		successors = new ArrayList();
	}

	public void addSuccessor(GraphNode node) {
		successors.add(node);
	}

	public ArrayList getSuccessors() {
		return new ArrayList(successors);
	}

	public void setRating(int[] rating) {
		this.rating[0] = rating[0];
		this.rating[1] = rating[1];
	}

	public int[] getRating() {
		return rating; // XXX Kopie zur�ck
	}

	/**
	 * @return den Elternknoten, bzw. null, wenn das der Wurzelknoten ist
	 */
	public GraphNode getParent() {
		return parent; // XXX Kopie zur�ckgeben
	}
	
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
