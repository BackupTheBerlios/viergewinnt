package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enth�lt einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since IOC
 */
class GraphNode {
	private GameState state;
	private ArrayList successors;

	public GraphNode(GameState state) {
		this.state = state; // XXX Annahme: state unver�nderbar
		successors = new ArrayList();
	}

	public void addSuccessor(GraphNode node) {
		successors.add(node);
	}

	public ArrayList getSuccessors() {
		return new ArrayList(successors);
	}
	
	public GameState getState() {
		return state; // XXX Annahme: state unver�nderbar
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
