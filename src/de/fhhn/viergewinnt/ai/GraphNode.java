package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enthält einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since IOC
 */
class GraphNode {
	private GameState state;
	private ArrayList successors;

	public GraphNode(GameState state) {
		this.state = state; // XXX Annahme: state unveränderbar
		successors = new ArrayList();
	}

	public void addSuccessor(GraphNode node) {
		successors.add(node);
	}

	public ArrayList getSuccessors() {
		return new ArrayList(successors);
	}
	
	public GameState getState() {
		return state; // XXX Annahme: state unveränderbar
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
