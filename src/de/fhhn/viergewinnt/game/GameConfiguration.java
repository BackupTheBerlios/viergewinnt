package de.fhhn.viergewinnt.game;

/**
 * Konfiguratrion eines Spieles. Wer fängt an, wie ist die Spielstärke
 * @author $Author: kathrin $
 * @version $Revision: 1.3 $
 */

// leider ist in den Playern ein Zyklus über das modell drin. Daher kann ich keine
// Referenzen hier abspeichern :(
 public class GameConfiguration {
	public static final int HUMANPLAYER = 0;
	public static final int AIPLAYER = 1;
	public static final int REMOTEPLAYER = 2;

	public static final int AIWEAK = AIPlayer.WEAK;
	public static final int AIMEDIUM = AIPlayer.MEDIUM;
//	public static final int AISTRONG = AIPlayer.STRONG;

	private int firstPlayer;
    private int secondPlayer;
    private int strength;

	public GameConfiguration() {
    }

    public void setFirstPlayer(int player) {
		firstPlayer = player;
    }

    public int getFirstPlayer() {
		return firstPlayer;
    }

    public void setSecondPlayer(int player) {
		secondPlayer = player;
    }

    public int getSecondPlayer() {
		return secondPlayer;
    }

    public void setStrength(int strength) { // jajajajajaja
		int selectedStrength = AIMEDIUM;
		
		switch(strength) {
			case(0):
				selectedStrength = AIWEAK;
				break;
			case(1):
				selectedStrength = AIMEDIUM;
				break;
//			case(2):
//				selectedStrength = AISTRONG;
//				break;
		}

		this.strength = selectedStrength;
    }

    public int getStrength() {
		return strength;
    }
 }