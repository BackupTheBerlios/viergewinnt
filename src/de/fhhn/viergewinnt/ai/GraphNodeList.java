package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Container für bereits erzeugte Knoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since IOC
 */
class GraphNodeList {
	/** 
	 * Enthält alle bereits erzeugten Knoten.
	 * Eigentlich sollte das weder List noch Map sein, sondern Set. 
	 * Ein GraphNode enthält ein GameState. Beide haben bei uns denselben 
	 * Hash-Wert, weswegen auf die Elemente der HashMap sowohl über
	 * GraphNode.hasCode() als auch über GameState.hashCode(9 zugegriffen
	 * werden kann.
	 */
	private Map nodeList = new HashMap(); // XXX Start-Kapazität?
	
	/** Fügt node dem Container hinzu. */
	public void add(GraphNode node) {
		nodeList.put(new Integer(node.hashCode()), node);
	}

	/** 
	 * Gibt true zurück, wenn der Zustand aState im Container enthalten ist.
	 */
	public boolean contains(GameState aState) {
		return nodeList.containsKey(new Integer(aState.hashCode()));
	}
	
	/** Gibt den Knoten zurückt, der den Zustand aState enthält. */
	public GraphNode getNode(GameState aState) {
		return (GraphNode) nodeList.get(new Integer(aState.hashCode()));
	}
	
	/** Gibt die Anzahl der enthaltenen Knoten zurück. */
	public int size() {
		return nodeList.size();
	}
}
