package de.fhhn.viergewinnt.ui.textui;

import java.util.Observable;
import java.io.*;
import javax.swing.event.EventListenerList;
import de.fhhn.viergewinnt.ui.View;
import de.fhhn.viergewinnt.game.*;

/**
 * View für die Kommandozeilen-Schnittstelle.
 * @author $Author: malte $
 * @version $Revision: 1.14 $
 * @since IOC
 */
public class TextUI implements View {
    // XXX Hier steckt eine Menge Code drin, der fast genauso in SwingUI
    // vorkommt. Dieser Code-Duplikation sollte bei Gelegenheit zu Leibe
    // gerückt werden!

    /** Farbe des Spielers. */
    private static Token playerColor;

    /** Das Model. */
    private Game game;

    /** Hier werden die registrierten MoveListener verwaltet. */
    private EventListenerList listenerList;

    /** Liest Benutzereingaben. */
    private BufferedReader stdin =
        new BufferedReader(new InputStreamReader(System.in));

    /** Konstruktor. */
    public TextUI(Game model) {
        listenerList = new EventListenerList();
        game = model;
        game.addObserver(this); // beim Model als View registrieren
        playerColor = Token.RED;
    }

    public void run() throws IOException {
		drawBoard(); // explizit aufrufen?
		do {
			int col = 0;
			while (col < 1 || col > Game.COLS) {
				IO.write(game.getLastMessage());
				
				try {
					col = IO.readInt("\nSpielstein in welche Spalte (1-" + Game.COLS + ")? ");
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
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MoveEventListener.class) {
                if (move == null) {
                    move = new MoveEvent(this, column);
                }
                ((MoveEventListener)listeners[i + 1]).tokenMoved(move);
            }
        }
    }

    //
    //////////////////////////////////////////////////////////////////////

    private void reregisterAsObserver() {
        // Sollte man eigentlich nicht machen...
        game.deleteObserver(this);
        game.addObserver(this);
    }

    public static void main(String[] args) throws IOException {
		Token[] availableTokens = new Token[2];
        availableTokens[0] = Token.RED;
		availableTokens[1] = Token.YELLOW;

		boolean wantToPlay = false;
		String selection = new String("magick");

        TextUI redView = null;
		do {			
			Game model = new Game(availableTokens[0]);
			redView = new TextUI(model);
	
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
					AIPlayer aiPlayer = new AIPlayer(config.getStrength(), model, availableTokens[i]);
                    if (config.getFirstPlayer() == GameConfiguration.AIPLAYER) {
                        aiPlayer.update(model, new Object());
                        playerColor = Token.YELLOW;
                    } else {
                        playerColor = Token.RED;
                    }
				} else {
                    throw new RuntimeException();
				}
			}
	
            redView.reregisterAsObserver();
            redView.run();

			while(!selection.equals("") && !selection.equals("j") && !selection.equals("n") && !selection.equals("J")) {
				try {
					selection = IO.promptAndRead("\nNoch einmal spielen? (J/n) ");
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
