package de.fhhn.viergewinnt.game;

/**
 * Konfiguratrion eines Spieles. Wer fängt an, wie ist die Spielstärke
 * @author $Author: kathrin $
 * @version $Revision: 1.1 $
 */

// leider ist in den Playern ein Zyklus über das modell drin. Daher kann ich keine
// Referenzen hier abspeichern :(
 public class GameConfiguration {
	public static final int HUMANPLAYER = 0;
	public static final int AIPLAYER = 1;
	public static final int REMOTEPLAYER = 2;

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

    public void setStrength(int strength) {
		this.strength = strength;
    }

    public int getStrength() {
		return strength;
    }
 }