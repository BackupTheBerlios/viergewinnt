package de.fhhn.viergewinnt.ui.textui;

import java.io.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Neuer Spiel Dialog
 * @author $Author: kathrin $
 * @version $Revision: 1.7 $
 * @since IOC
 */

 public class NewGameDialog {
	private GameConfiguration config;
	
	public NewGameDialog(String[] args) {
		config = new GameConfiguration();
		IO.write("Neues Spiel beginnen:");
		IO.write("\n---------------------");
		
		String selection = new String("magick");
		
		while(!selection.equals("") && !selection.equals("j") &&
              !selection.equals("n") && !selection.equals("N")) {
			try {
				selection = IO.promptAndRead("\nOptionen bearbeiten (j/N)? ");
				selection.toLowerCase();
			} catch (Exception e) {}
		}
		
		if(selection.equals("j")) {
			askUser();
		} else {
			config.setFirstPlayer(GameConfiguration.HUMANPLAYER);
			config.setSecondPlayer(GameConfiguration.AIPLAYER);
			config.setStrength(GameConfiguration.AIMEDIUM);
		}
	}

	private String arrayToString(String[] array, int selected) {
		String arrayString = new String("\n");

		for(int i=0; i < array.length; i++) {
			if(selected != i) {
				arrayString = arrayString + "["+ i + "] "+ array[i] + "\n";
            }
        }

        return arrayString;
    }

	private void askUser() {
		askForPlayers();
		askForStrength();
	}
	
	private void askForPlayers() {
		String[] players = new String[3];

        players[0] = "Spieler";
		players[1] = "KI";
		players[2] = "Netz";

		int firstPlayer = -1;
		int secondPlayer = -1;
		int selectedPlayer = -1;

		for(int i=0; i < 2; i++) {
			while(firstPlayer == secondPlayer || selectedPlayer < 0 || (selectedPlayer >= players.length)) {
				try {
					if(i == 0) {
						IO.write("\nWer soll beginnen?");
					} else {
						IO.write("\nGegen wen?");
					}
	
					IO.write(arrayToString(players, firstPlayer)+"\n");
					selectedPlayer = IO.readInt("\nBitte einen Spieler auswählen: ");
	
					if(i == 0) {
						firstPlayer = selectedPlayer;
					} else {
						secondPlayer = selectedPlayer;
					}
				} catch (Exception e) {
					//neuer durchlauf
				}
            }
            selectedPlayer = -1;
		}

        if(firstPlayer == 2 || secondPlayer == 2) {
			IO.write("Netzcode noch nicht implementiert. Spiel wird beendet.\n");
            System.exit(1);
            //throw new RuntimeException();
        }

		config.setFirstPlayer(firstPlayer);
		config.setSecondPlayer(secondPlayer);
	}

	private void askForStrength() {
		String[] kiStrength = new String[2];
		
		kiStrength[0] = "Schwach";
		kiStrength[1] = "Mittel";
		//kiStrength[2] = "Stark";

        int strength = -1;
		while(strength < 0 || strength >= kiStrength.length) {
			try {
				IO.write(arrayToString(kiStrength, strength)+"\n");
				strength = IO.readInt("\nBitte Spielstärke auswählen: ");
            } catch (Exception e) {
				// neuer Durchlauf
            }
        }
		
		config.setStrength(strength);
	}
	
    public GameConfiguration getGameConfiguration() {
		return config;
    }
}
