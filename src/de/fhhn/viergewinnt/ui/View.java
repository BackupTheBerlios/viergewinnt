package de.fhhn.viergewinnt.ui;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Interface für die Swing-UI und Text-UI.
 * @author $Author: malte $
 * @version $Revision: 1.1 $
 * @since LCA
 */
public interface View extends Observer {
    void addMoveListener(MoveListener listener);

    void removeMoveListener(MoveListener listener);
}
