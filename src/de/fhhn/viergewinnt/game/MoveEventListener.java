package de.fhhn.viergewinnt.game;

import java.util.EventListener;

/**
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since LCA
 */
public interface MoveEventListener extends EventListener {
    void tokenMoved(MoveEvent m);
}
