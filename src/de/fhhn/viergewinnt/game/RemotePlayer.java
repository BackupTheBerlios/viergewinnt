package de.fhhn.viergewinnt.game;

/**
 * @author $Author: malte $
 * @version $Revision: 1.3 $
 * @since LCA
 * @stereotype View, Controller
 */
public class RemotePlayer extends Player {
    /** FIXME Pseudo-Konstruktor, damit der Compiler nicht meckert. */
    public RemotePlayer(Game model, Token color) {
        super(model, color);
    }
}
