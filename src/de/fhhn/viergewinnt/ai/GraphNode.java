package de.fhhn.viergewinnt.ai;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/** 
 * Enth�lt einen Spielzustand und Kanten zu Nachfolgerknoten.
 *
 * @author $Author: malte $
 * @version $Revision: 1.24 $
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
	* Baut einen Spielgraphen auf.
	* @param node Wurzel des Spielgraphen
	* @param list Container f�r bereits erzeugte Knoten
	* @param limit Suchtiefe (beeinflusst die Spielst�rke)
	*/
	public static void expand(GraphNode node, GraphNodeList list, int limit) {
		///////////////////////////////////////////////////////////////////////
		// Abbruchbedingungen f�r die Rekursion
		AIGameState nodeState = node.getState();
		if (AIGameState.isFinalState(node)) { // Endzustand erreicht?
			// Min-Max-Bewertung
			rateFinalState(nodeState, node);
			return;
		} else if (
			limit == 0) { // noch kein Endzustand erreicht, aber Baum zuende?
			// heuristische Stellungsbewertung
			int nodeRating = AIGameStateHeuristic.ratePosition(nodeState);
			node.setRating(nodeRating);
			return;
		}
		//
		///////////////////////////////////////////////////////////////////////

		///////////////////////////////////////////////////////////////////////
		// Nachfolgerzust�nde von nodeState berechnen und nodeState mit
		// Min-Max bewerten
		ArrayList succStates = AIGameState.calculateSuccessors(nodeState);
		ListIterator it = succStates.listIterator();

		// Vorbereitung f�r die Min-Max-Bewertung
		int b = prepareMinMaxRating(nodeState);

		while (it.hasNext()) {
			// F�r alle Nachfolgerzust�nde von nodeState
			AIGameState succState = (AIGameState) it.next();
			GraphNode succNode = null;

			// neuer Nachfolger mit Nachfolgerzustand und aktuellem Knoten
			// als Vorg�nger
			succNode = new GraphNode(succState, node);
			node.addSuccessor(succNode);
			list.add(succNode);

			expand(succNode, list, limit - 1); // Rekursion!

			//	Min-Max-Bewertung
			if (nodeState.getWhoseTurn() == Token.RED) {
				  b = Math.max(b, succNode.getRating());
				//	b = evalMax(node, limit, Integer.MIN_VALUE, Integer.MAX_VALUE);
			} else {
				b = Math.min(b, succNode.getRating());
				//	b = evalMin(node, limit, Integer.MIN_VALUE, Integer.MAX_VALUE);
			}	
			
			node.setRating(b);
		}
		
		//
		///////////////////////////////////////////////////////////////////////

	}
	private static int evalMin(
		GraphNode node,
		int limit,
		int alpha,
		int beta) {
		
		int rating = 0;

		/////////////////////////////////////////////////////////////////////
		// Abbruchbedingungen f�r die Rekursion
		AIGameState nodeState = node.getState();
		if ((AIGameState.isFinalState(node))
			|| (limit == 0)) { // Endzustand erreicht?
			return node.getRating();
		}
		//
		///////////////////////////////////////////////////////////////////////

		int betaMin = beta;

		///////////////////////////////////////////////////////////////////////
		// Nachfolgerzust�nde von nodeState berechnen und nodeState mit
		// Min-Max bewerten
		ArrayList succNodes = node.getSuccessors();
		ListIterator it = succNodes.listIterator();
		GraphNode workNode;
		
		while (it.hasNext()) {
			workNode = (GraphNode)it.next();
			rating = evalMax(workNode, limit-1, alpha, betaMin);
			betaMin = Math.min(betaMin, rating);
			if (betaMin <= alpha) {
				//System.err.println("pruned tree in evalMin");
				break;
			}
		}
		//
		///////////////////////////////////////////////////////////////////////

		return betaMin;
	}
	
	private static int evalMax(
		GraphNode node,
		int limit,
		int alpha,
		int beta) {
		
		int rating = 0;

		/////////////////////////////////////////////////////////////////////
		// Abbruchbedingungen f�r die Rekursion
		AIGameState nodeState = node.getState();
		if ((AIGameState.isFinalState(node))
			|| (limit == 0)) { // Endzustand erreicht?
			return node.getRating();
		}
		//
		///////////////////////////////////////////////////////////////////////

		int alphaMax = alpha;

		///////////////////////////////////////////////////////////////////////
		// Nachfolgerzust�nde von nodeState berechnen und nodeState mit
		// Min-Max bewerten
		ArrayList succNodes = node.getSuccessors();
		ListIterator it = succNodes.listIterator();
		GraphNode workNode;
		
		while (it.hasNext()) {
			workNode = (GraphNode)it.next();
			rating = evalMin(workNode, limit-1, alphaMax, beta);
			alphaMax = Math.max(alphaMax, rating);
			if (alphaMax >= beta) {
				//System.err.println("pruned tree in evalMax");
				break;
			}
		}
		//
		///////////////////////////////////////////////////////////////////////

		return alphaMax;
	}

	private static int prepareMinMaxRating(AIGameState nodeState) {
		int b;
		if (nodeState.getWhoseTurn() == Token.RED) {
			b = Integer.MIN_VALUE;
		} else {
			b = Integer.MAX_VALUE;
		}
		return b;
	}

	/**
	 *  Min-Max-Bewertung eines Endzustandes.
	 */
	private static void rateFinalState(AIGameState nodeState, GraphNode node) {
		Token winner = nodeState.checkWinner();
		if (winner == Token.RED) { // FIXME
			node.setRating(Integer.MAX_VALUE);
		} else if (winner == Token.YELLOW) { // FIXME
			node.setRating(Integer.MIN_VALUE);
		} else if (winner == Token.EMPTY) { // FIXME
			node.setRating(0);
		}
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

	public void setParent(GraphNode parent) {
		this.parent = parent;
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

	public void deleteSuccessors() {
        successors = new ArrayList();
    }

    /** Nur zum testen. */
    public static void main(String[] args) {
        GraphNode root = new GraphNode(new AIGameState(Token.RED), null); // FIXME: RED f�ngt immer an
        GraphNodeList list = new GraphNodeList();
        int limit = de.fhhn.viergewinnt.game.AIPlayer.MEDIUM;
        // Wurzel initialisieren
        GraphNode.expand(root, list, limit);
		root.deleteSuccessors();
        GraphNode.expand(root, list, limit);
        System.out.println("Alles wird gut.");
    }
}
