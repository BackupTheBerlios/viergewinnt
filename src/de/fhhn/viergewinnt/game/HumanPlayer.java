package de.fhhn.viergewinnt.game;

import de.fhhn.viergewinnt.ui.View;

/**
 * Controller für einen menschlichen Spieler.
 * @author $Author: malte $
 * @version $Revision: 1.4 $
 * @since LCA
 * @stereotype Controller
 */
public class HumanPlayer extends Player implements MoveEventListener {
    private View view;
    private Game gameModel;

    public HumanPlayer(View v, Game g) {
        view = v;
        gameModel = g;
        view.addMoveEventListener(this);
    }

    public void tokenMoved(MoveEvent m) {
        System.out.println("HumanPlayer.tokenMoved(): move=" + m);
        gameModel.accept(m);
    }
}
