package de.fhhn.viergewinnt.game;

import de.fhhn.viergewinnt.ui.View;

/**
 * Controller f�r einen menschlichen Spieler.
 * 
 * @author $Author: manuel $
 * @version $Revision: 1.6 $
 * @since LCA
 * @stereotype Controller
 */
public class HumanPlayer extends Player implements MoveEventListener {
    private View view;

    public HumanPlayer(View v, Game g, Token color) {
        super(g, color);
        view = v;
        //gameModel = g;
        view.addMoveEventListener(this);
    }

    public void tokenMoved(MoveEvent m) {
        m.setToken(color); // dem Token die richtige Farbe geben
        //System.out.println("HumanPlayer.tokenMoved(): move=" + m);
        gameModel.accept(m);
    }
}
