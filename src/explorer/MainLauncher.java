/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import explorer.gui.MainFrame;

/**
 * The Class MainLauncher.
 */
public class MainLauncher {

	/** The map. */
	private static Map map;
	
	/** The Constant stepDuration. */
	private static final int stepDuration=1000;
	
	/** The frame. */
	private static MainFrame frame;
	
	/** The sem. */
	public static Semaphore sem=new Semaphore(0);
	
	/** The lock. */
	public static ReentrantLock lock = new ReentrantLock();
	
	/** The autoplay. */
	public static Boolean autoplay=true;
	
	/** The engine. */
	public static ExplorationEngine engine;
	
	public static LinkedList<Cell> path=new LinkedList<Cell>();
	
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
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		//Prepare the Map
		map=new Map();
		try {
			map.loadFromFile("test2");
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Error while reading map file.", "Eroare citire fisier", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
		//Run the graphics
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame=new MainFrame(map.cells);
					MainLauncher.sem.release();
					MainLauncher.debug("Graphics initialization done");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		});
		debug("Graphics thread started!");
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Start the exploration engine
		engine=new ExplorationEngine(map);
		frame.repaintMap();
		
		//While we have not found the goal, we keep exploring
		String moveDescription;
		while(!engine.isOnGoalState())
		{
			//We sleep a while
			try {
				Thread.sleep(stepDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lock.lock();
			//Save current position
			path.push(engine.currentCell);
			
			//Perform the next step and redraw
			moveDescription=engine.nextStep();
			frame.addMoveDescription(moveDescription);
			frame.repaintMap();
			lock.unlock();
		}
		
		//We have found the goal
		debug("Goal found!");
		frame.addMoveDescription("Goal cell found!");
	}
	
	/**
	 * Next move.
	 */
	public static void nextMove()
	{
		String moveDescription;
		//Save current position
		path.push(engine.currentCell);

		//Perform the next step and redraw
		moveDescription=engine.nextStep();
		frame.addMoveDescription(moveDescription);
		frame.repaintMap();
	}
	
	/**
	 * Previous move.
	 */
	public static void previousMove()
	{
		if(path.isEmpty())
		{
			frame.addMoveDescription("No previous moves");
			return;
		}
		//Perform the previous step and redraw
		engine.updateCurrent(path.pop());
		frame.addMoveDescription("Moved back");
		frame.repaintMap();
	}
}
