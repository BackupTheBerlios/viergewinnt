package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Enthält einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.2 $
 * @since IOC
 */
class GraphNode {
	GraphNode parent;
	private AIGameState state;
	private ArrayList successors;

	public GraphNode(AIGameState state, GraphNode parent) {
		this.parent = parent;
		this.state = state; // XXX Annahme: state unveränderbar
		successors = new ArrayList();
	}

	public void addSuccessor(GraphNode node) {
		successors.add(node);
	}

	public ArrayList getSuccessors() {
		return new ArrayList(successors);
	}

	/**
	 * @return den Elternknoten, bzw. null, wenn das der Wurzelknoten ist
	 */
	public GraphNode getParent() {
		return parent; // XXX Kopie zurückgeben
	}
	
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
