package de.fhhn.viergewinnt.ui.textui;

import java.util.Observable;
import java.io.*;
import javax.swing.event.EventListenerList;
import de.fhhn.viergewinnt.ui.View;
import de.fhhn.viergewinnt.game.*;

/**
 * View für die Kommandozeilen-Schnittstelle.
 * @author $Author: malte $
 * @version $Revision: 1.2 $
 * @since IOC
 */
public class TextUI implements View {
    // XXX Hier steckt eine Menge Code drin, der fast genauso in SwingUI
    // vorkommt. Dieser Code-Duplikation sollte bei Gelegenheit zu Leibe
    // gerückt werden!
    //
    // Macht es Sinn, das TextUI als Thread zu implementieren?

    /** Farbe des Spielers. */
    private Token playerColor;

    /** Das Model. */
    private Game game;

    /** Hier werden die registrierten MoveListener verwaltet. */
    private EventListenerList listenerList;

    /** Liest Benutzereingaben. */
    private BufferedReader stdin =
        new BufferedReader(new InputStreamReader(System.in));

    /** Konstruktor. */
    public TextUI(Game model) /*throws IOException*/ {
        listenerList = new EventListenerList();
        game = model;
        game.addObserver(this); // beim Model als View registrieren
        playerColor = Token.RED; // FIXME Spieler immer rot
    }

    public void run() throws IOException {
        while (true) {
            drawBoard();
            int col = 0;
            while (col < 1 || col > 5) {
                System.out.println("Spielstein in welche Spalte (1-5)?");
                try {
                    col = Integer.parseInt(stdin.readLine()); // XXX IOException
                } catch (NumberFormatException e) {
                    // keine Zahl einegegeben -> neuer Schleifendurchlauf
                }
            }
            fireMoveEventTokenMoved(col - 1); // intern beginnen Spalten bei 0
        }
    }

    /** Gibt das aktuelle Spielbrett auf die Kommandozeile aus. */
    private void drawBoard() {
        Token[][] board = game.getBoard();
        for (int i = board.length - 1; i >= 0; i--) {
            //for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    //////////////////////////////////////////////////////////////////////
    // Die folgenden Methoden dienen der Kommunikation zwischen Model,
    // View und Controller

    /**
     * Wird von Game aufgerufen, wenn sich der Zustand des
     * Modells geändert hat.
     */
    public void update(Observable o, Object arg) {
        // Im Spiel hat sich was geändert -> neu zeichnen
        drawBoard();
    }

    /**
     * Hiermit kann sich ein Controller (Player) registrieren, um über
     * Benutzereingaben (also Spielzüge) benachrichtig zu werden.
     */
    public void addMoveEventListener(MoveEventListener listener) {
        System.out.println("Listener " + listener + " registriert");
        // XXX Code-Duplikation (s. SwingUI.java)
        listenerList.add(MoveEventListener.class, listener);
    }

    /** Entfernt einen registrierten Controller (Player). */
    public void removeMoveEventListener(MoveEventListener listener) {
        // XXX Code-Duplikation (s. SwingUI.java)
        listenerList.remove(MoveEventListener.class, listener);
    }

    /** Gibt ein neues MoveEvent an alle Controller. */
    protected void fireMoveEventTokenMoved(int column) {
        MoveEvent move = null; // XXX Warum ist das in SwingUI anders?
        // XXX Code-Duplikation (s. SwingUI.java)
        System.out.println("TextUI.fireMoveEventTokenMoved(): column=" +
            column);
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MoveEventListener.class) {
                // Lazily create the event:
                if (move == null) {
                    move = new MoveEvent(this, column);
                }
                ((MoveEventListener)listeners[i + 1]).tokenMoved(move);
            }
        }
        move = null;
    }

    //
    //////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws IOException {
        Game model = new Game(Token.RED); // FIXME: Rot fängt immer an
        View redView = new TextUI(model);
        Player redPlayer = new HumanPlayer(redView, model, Token.RED);
        Player yellowPlayer = new AIPlayer(model, Token.YELLOW);
        // XXX Mangels Nebenläufigkeit kann View erst jetzt gestartet werden
        TextUI tui = (TextUI)redView;
        tui.run();
    }
}
