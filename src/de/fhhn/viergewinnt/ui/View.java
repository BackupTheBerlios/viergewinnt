package de.fhhn.viergewinnt.ui;

import java.util.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Interface für die Swing-UI und Text-UI. Views registrieren sich beim
 * Game als Observer und werden so über dessen Zustandsänderungen benachrichtigt.
 *
 * Bei einer View <em>kann</em> sich ein Controller (also ein Player) als
 * MoveEventListener registrieren.
 *
 * XXX Macht es wirklich Sinn, beide Kommunikationswege" (Observer/Event) hier
 * drin zu haben?
 * 
 * @author $Author: malte $
 * @version $Revision: 1.3 $
 * @since LCA
 */
public interface View extends Observer {
    void addMoveEventListener(MoveEventListener listener);

    void removeMoveEventListener(MoveEventListener listener);
}
