package de.fhhn.viergewinnt.ui.textui;

import java.util.Observable;
import java.io.*;
import javax.swing.event.EventListenerList;
import de.fhhn.viergewinnt.ui.View;
import de.fhhn.viergewinnt.game.*;

/**
 * View für die Kommandozeilen-Schnittstelle.
 * @author $Author: manuel $
 * @version $Revision: 1.10 $
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
        //while (game.getWinner() == Token.EMPTY) {
		drawBoard(); // explizit aufrufen?
		do {
			//drawBoard();
			int col = 0;
			while (col < 1 || col > Game.COLS) {
				IO.write("System sagt: "+ game.getLastMessage());
				
				try {
					col = IO.readInt("\nSpielstein in welche Spalte (1-" + Game.COLS + ")? ");
					//col = Integer.parseInt(stdin.readLine()); // XXX IOException
				} catch (Exception e) {
					// keine Zahl einegegeben -> neuer Schleifendurchlauf
				}
			}
			fireMoveEventTokenMoved(col - 1); // intern beginnen Spalten bei 0
		} while (game.getWinner() == Token.EMPTY);
		
		IO.write("\n" + game.getLastMessage());
    }

    /** Gibt das aktuelle Spielbrett auf die Kommandozeile aus. */
    private void drawBoard() {
        /*
        Die Ausgabe sieht so aus:

         1 2 3 4 5 6 7
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        |O| | | | | | |
        |O| | | | | | |
        |X|X| | | | | |
        ---------------

        Eine schönere Ausgabe könnte man mit Unicode (bzw. diesen
        Rahmen-Symbolen) hinkriegen.
        */

        Token[][] board = game.getBoard();
        StringBuffer sb = new StringBuffer();

        // Spalten-Nummerierung
        for (int i = 0; i < board[0].length; i++) {
            sb.append(" " + (i + 1));
        }
        sb.append("\n");

        // Spielfeld
        for (int i = board.length - 1; i >= 0; i--) {
            sb.append("|");
            for (int j = 0; j < board[0].length; j++) {
                sb.append(board[i][j] + "|");
            }
            sb.append("\n");
        }
        sb.append("---------------");
        System.out.println(sb);
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
         if (game.getWhoseTurn() == playerColor) {
			 drawBoard();
		 }
    }

    /**
     * Hiermit kann sich ein Controller (Player) registrieren, um über
     * Benutzereingaben (also Spielzüge) benachrichtig zu werden.
     */
    public void addMoveEventListener(MoveEventListener listener) {
        //System.out.println("Listener " + listener + " registriert");
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
        MoveEvent move = null;
        // XXX Code-Duplikation (s. SwingUI.java)
        //System.out.println("TextUI.fireMoveEventTokenMoved(): column=" +
        //    column);
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
    }

    //
    //////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
		Token[] availableTokens = new Token[2];
        availableTokens[0] = Token.RED;
		availableTokens[1] = Token.YELLOW;

		boolean wantToPlay = false;
		String selection = new String("magick");

		do {			
			Game model = new Game(availableTokens[0]);
			View redView = new TextUI(model);
	
			NewGameDialog dialog = new NewGameDialog(args);
			GameConfiguration config = dialog.getGameConfiguration();
	
			int playerId;
	
			for(int i=0; i < 2; i++) {
				if(i==0) {
					playerId = config.getFirstPlayer();
				} else {
					playerId = config.getSecondPlayer();
				}
	
				if(playerId == GameConfiguration.HUMANPLAYER) { //zyklus!!! argh
					Player humanPlayer = new HumanPlayer(redView, model, availableTokens[i]);
				} else if(playerId == GameConfiguration.AIPLAYER) {
					Player aiPlayer = new AIPlayer(config.getStrength(), model, availableTokens[i]);
				} else {
					System.exit(1);
				}
			}
	
			// XXX Mangels Nebenläufigkeit kann View erst jetzt gestartet werden
			TextUI tui = (TextUI)redView;
			tui.run();

			while(!selection.equals("") && !selection.equals("y") && !selection.equals("n")) {
				try {
					selection = IO.promptAndRead("\nNoch einmal spielen? (Y/n) ");
					selection.toUpperCase();
				} catch (Exception e) {}
			}
			
			if(selection.equals("n")) {
				wantToPlay = false;
			} else {
				wantToPlay = true;
			}
			
		} while (wantToPlay);
		
		IO.write("\nBis bald ...\n\n");
    }
}
