package de.fhhn.viergewinnt.ai;

import java.util.*;

/** 
 * Container f�r bereits erzeugte Knoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.2 $
 * @since IOC
 */
class GraphNodeList {
	/** 
	 * Enth�lt alle bereits erzeugten Knoten.
	 * Eigentlich sollte das weder List noch Map sein, sondern Set. 
	 * Ein GraphNode enth�lt ein GameState. Beide haben bei uns denselben 
	 * Hash-Wert, weswegen auf die Elemente der HashMap sowohl �ber
	 * GraphNode.hasCode() als auch �ber GameState.hashCode(9 zugegriffen
	 * werden kann.
	 */
	private Map nodeList = new HashMap(); // XXX Start-Kapazit�t?
	
	/** F�gt node dem Container hinzu. */
	public void add(GraphNode node) {
		nodeList.put(new Integer(node.hashCode()), node);
	}

	/** 
	 * Gibt true zur�ck, wenn der Zustand aState im Container enthalten ist.
	 */
	public boolean contains(AIGameState aState) {
		return nodeList.containsKey(new Integer(aState.hashCode()));
	}
	
	/** Gibt den Knoten zur�ckt, der den Zustand aState enth�lt. */
	public GraphNode getNode(AIGameState aState) {
		return (GraphNode) nodeList.get(new Integer(aState.hashCode()));
	}
	
	/** Gibt die Anzahl der enthaltenen Knoten zur�ck. */
	public int size() {
		return nodeList.size();
	}
}
