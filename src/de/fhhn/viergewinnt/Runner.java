package de.fhhn.viergewinnt;

import de.fhhn.viergewinnt.ui.gui.SwingUI;
import de.fhhn.viergewinnt.ui.textui.TextUI;
import de.fhhn.viergewinnt.game.Game;

/**
 * Beschreibung
 * 
 * @author		p.herk
 * @version	06.01.2003 / 12:58:34
 */
public class Runner {

//	private SwingUI gui;
	private TextUI tui;
	
	/**
	 * Konstruktor
	 * 
	 * @author		p.herk
	 * @version	06.01.2003 / 13:30:44
	 */
	Runner(String[] args) {
		
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("-t")) {
				guiRunner();
			} else if (args[1].equalsIgnoreCase("-g")) {
				tuiRunner();
			} else {
				System.out.println("USAGE: java VierGewinnt.jar [-t|-g]");
				System.out.println("       /t TextUI");
				System.out.println("       /g SwingUI (default)");
			}
		}
		guiRunner();	// Gui als Standard
	}
	
	/**
	 * GUI Runner
	 * 
	 * @author		p.herk
	 * @version	06.01.2003 / 13:30:55
	 */
	private void guiRunner() {
		new SwingUI();
		
	}

	/**
	 * TUI Runner
	 * 
	 * @author		p.herk
	 * @version	06.01.2003 / 13:30:55
	 */
	private void tuiRunner() {
		//
	}
	
	/**
	 * Einstiegspunkt
	 * 
	 * @author		p.herk
	 * @version	06.01.2003 / 13:30:11
	 */
	public static void main(String[] args) {
		new Runner(args);
	}
	
}
