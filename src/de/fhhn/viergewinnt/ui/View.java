package de.fhhn.viergewinnt.ui;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Interface f�r die Swing-UI und Text-UI.
 * @author $Author: malte $
 * @version $Revision: 1.2 $
 * @since LCA
 */
public interface View extends Observer {
    void addMoveEventListener(MoveEventListener listener);

    void removeMoveEventListener(MoveEventListener listener);
}
