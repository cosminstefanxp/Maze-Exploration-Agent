/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.awt.EventQueue;

import explorer.gui.MainFrame;

/**
 * The Class MainLauncher.
 */
public class MainLauncher {

	/**
	 * Launch the application.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
