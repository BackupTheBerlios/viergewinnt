package de.fhhn.viergewinnt.ui.textui;

import java.io.*;
import de.fhhn.viergewinnt.game.*;

/**
 * Neuer Spiel Dialog
 * @author $Author: kathrin $
 * @version $Revision: 1.2 $
 * @since IOC
 */

 public class NewGameDialog {
	private GameConfiguration config;

	public NewGameDialog() {
		newDialog();
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

	private void newDialog() {
        /**
         * schön prozedual und häßlich ;) daher XXX und nohmal XXX
         */
		String[] players = new String[3];
        String[] kiStrength = new String[3];

        players[0] = "Spieler";
		players[1] = "KI";
		players[2] = "Netz";

		kiStrength[0] = "Schwach";
		kiStrength[1] = "Mittel";
		kiStrength[2] = "Stark";

		IO.write("Neues Spiel beginnen:");
		IO.write("\n---------------------");

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
			IO.write("Fehler: Netzcode noch nicht vollständig implementiert. Beende mich jetzt!");
            System.exit(1);
        }


        int strength = -1;
		while(strength < 0 || strength >= kiStrength.length) {
			try {
				IO.write(arrayToString(kiStrength, strength)+"\n");
				strength = IO.readInt("\nBitte Spielstärke auswählen: ");
            } catch (Exception e) {
				// neuer Durchlauf
            }
        }

		config = new GameConfiguration();
		config.setFirstPlayer(firstPlayer);
		config.setSecondPlayer(secondPlayer);
        config.setStrength(strength);
	}

    public GameConfiguration getGameConfiguration() {
		return config;
    }
}