package de.fhhn.viergewinnt.game;

import de.fhhn.viergewinnt.ui.View;

/**
 * Controller für einen menschlichen Spieler.
 * 
 * @author $Author: malte $
 * @version $Revision: 1.2 $
 * @since LCA
 * @stereotype Controller
 */
public class HumanPlayer extends Player implements MoveListener {
    private View view;
    private Game gameModel;

    public HumanPlayer(View v, Game g) {
        view = v;
        gameModel = g;
		view.addMoveListener(this);
    }

    public void tokenMoved(Move m) {
        System.out.println("HumanPlayer.tokenMoved(): move=" + m);

        gameModel.accept(m);
    }
}
