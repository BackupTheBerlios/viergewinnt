package de.fhhn.viergewinnt.game;

import java.util.*;
import de.fhhn.viergewinnt.ui.View;
/**
 * @author $Author: malte $
 * @version $Revision: 1.3 $
 * @since LCA
 * @stereotype View, Controller
 */
public class AIPlayer extends Player implements View {
    public AIPlayer(Game g, Token color) {
        super(g, color);
    }

    public void addMoveEventListener(MoveEventListener listener) {
    }

    public void removeMoveEventListener(MoveEventListener listener) {
    }

    public void update(Observable observable, Object obj) {
    }
}
