package de.fhhn.viergewinnt.game;

import java.util.EventListener;

/**
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since LCA
 */
public interface MoveListener extends EventListener {
    void tokenMoved();
}
