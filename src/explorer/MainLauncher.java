/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import explorer.gui.CellGraphics;
import explorer.gui.MainFrame;

/**
 * The Class MainLauncher.
 */
public class MainLauncher {

	/** The map. */
	private static Map map;
	
	private static ArrayList<CellGraphics> cells;
	
	/**
	 * Debug printing.
	 *
	 * @param text the text
	 */
	public static void debug(String text)
	{
		System.out.println("[DEBUG] "+text);
	}
	
	/**
	 * Launch the application.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		//Prepare the Map
		map=new Map();
		try {
			cells=map.loadFromFile("test1");
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Error while reading map file.", "Eroare citire fisier", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
		//Run the graphics
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame(cells);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
