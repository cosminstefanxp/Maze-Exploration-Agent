/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import explorer.Cell.Direction;
import explorer.Cell.Type;
import explorer.gui.CellGraphics;
import explorer.gui.MainFrame;

/**
 * The Class MainLauncher.
 */
public class MainLauncher {

	/** The map. */
	private static ArrayList<Map> maps;
	
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
	public static ExplorationEngine[] engines;
	
	/** The engines count. */
	public static int enginesCount;
	
	/** The path. */
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
	 * Load the map and game info from file and returns a list of maps (one for each engine).
	 *
	 * @param filename the filename
	 * @return the array list
	 * @throws FileNotFoundException the file not found exception
	 * @throws CloneNotSupportedException 
	 */
	public static ArrayList<Map> loadFromFile(String filename) throws FileNotFoundException {
		
		ArrayList<Map> maps=new ArrayList<Map>();

		MainLauncher.debug("Loading map data from " + filename);
		// Use buffering, reading one line at a time
		Scanner input = new Scanner(new FileReader(filename));


		// Get number of players
		int agents=input.nextInt();
		MainLauncher.debug("We have "+agents+" agents.");

		for(int i=0;i<agents;i++)
		{
			MainLauncher.debug("Reading start info for player "+i);
			maps.add(new Map(i));
			maps.get(i).maps=maps;
			
			// Get the starting position
			maps.get(i).startX = input.nextInt();
			maps.get(i).startY = input.nextInt();
			MainLauncher.debug("Start point: " + maps.get(i).startX + "," + maps.get(i).startY);
	
			// Get the starting hit points
			maps.get(i).hitpoints = input.nextInt();
			MainLauncher.debug("Starting hitpoints: " + maps.get(i).hitpoints);
			
			// Get the visibility range
			maps.get(i).range = input.nextInt();
			MainLauncher.debug("Visibility range: " + maps.get(i).range);
		}
		
		// Get the number of cells
		int cellsNo = input.nextInt();
		MainLauncher.debug("Number of cells: " + cellsNo);

		// Get the cells
		int posX, posY, typeV;
		int minXCell=Integer.MAX_VALUE;
		int minYCell=Integer.MAX_VALUE;
		int maxYCell=0;
		Type type;
		
		for (int i = 0; i < cellsNo; i++) {
			// Reading cell data
			posX = input.nextInt();
			posY = input.nextInt();
			typeV = input.nextInt();
			
			//Check for minimum
			if(posX<minXCell)
				minXCell=posX;
			if(posY<minYCell)
				minYCell=posY;
			if(posY>maxYCell)
				maxYCell=posY;
			
			// Get the type
			switch (typeV) {
			case 1:
				type = Type.Goal;
				break;
			case 0:
				type = Type.Empty;
				break;
			case -1:
				type = Type.Wall;
				break;
			case -2:
				type = Type.Trap;
				break;
			case 2:
				type = Type.Clue;
				break;
			case 3:
				type = Type.Exit;
				break;
			default:
				type = Type.Wall;
				break;
			}

			// Build the cell and read more data if necessary
			CellGraphics newCell = new CellGraphics(posX, posY, type);

			if (type == Type.Trap)
				newCell.probability = input.nextFloat();
			if (type == Type.Clue)
				newCell.hint = Direction.valueOf(input.next());


			MainLauncher.debug("New cell: " + newCell.toString());
			maps.get(0).cells.put(new Position(posX, posY), newCell);
			for(int j=1;j<agents;j++)
				maps.get(j).cells.put(new Position(posX, posY), newCell.clone());
		}
	
		
		//Set Cell Graphics minimum
		CellGraphics.minXCell=minXCell;
		CellGraphics.minYCell=minYCell;
		CellGraphics.maxYCell=maxYCell;
		
		input.close();
		
		return maps;
	}
	
	/**
	 * Launch the application.
	 * 
	 * @param args the arguments
	 * @throws InterruptedException 
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		//Prepare the Maps
		try {
			maps=loadFromFile("test5");
			enginesCount=maps.size();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Error while reading map file.", "Eroare citire fisier", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
		//Run the graphics
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame=new MainFrame(maps);
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
		
		//Start the exploration engines
		engines=new ExplorationEngine[maps.size()];
		for(int i=0;i<maps.size();i++)
			engines[i]=new ExplorationEngine(maps.get(i),i+1);
		frame.repaintMap();
		
		//While we have not found the goal, we keep exploring
		boolean finished=false;
		while(!finished)
		{
			//We sleep a while for the graphical viewer
			try {
				Thread.sleep(stepDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Lock and do the next move
			lock.lock();
			finished=nextMove();
			lock.unlock();
		}
		
		//We have found the goal
		debug("Exploration for all engines finished!");
		frame.addMoveDescription("Simulation finished!");
	}
	
	/**
	 * Next move.
	 *
	 * @return true, if finished
	 */
	public static boolean nextMove()
	{
		String moveDescription = null;
		//Save current position
		//path.push(engine.currentCell);

		//Perform the next step and redraw
		int finishCount=0;
		for(int i=0;i<maps.size();i++)
		{
			moveDescription=engines[i].nextStep();
			frame.addMoveDescription(moveDescription);
			//check engine battle
			if(!engines[i].finished && engines[i].currentCell.enemy!=null)
			{
				String desc=String.format("Engine %d vs Engine %d battle!\n",i+1,engines[i].currentCell.enemy);
				debug(String.format("Engine %d vs Engine %d battle on cell %s", i+1,engines[i].currentCell.enemy,engines[i].currentCell));
				
				//Check winner
				float winnerProb=Map.rand.nextFloat();
				float lifeRation=(float)engines[i].map.hitpoints/(engines[i].map.hitpoints+engines[engines[i].currentCell.enemy-1].map.hitpoints);
				
				if(winnerProb>lifeRation)	//this engine lost
				{
					debug("Engine "+engines[i].currentCell.enemy+" won the battle!");
					desc+="Engine "+engines[i].currentCell.enemy+" won the battle!";
					engines[i].finished=true;
					engines[i].map.notifyPosition(engines[i].currentCell, null);
				}
				else	//this engine won
				{
					debug("Engine "+(i+1)+" won the battle!");
					desc+="Engine "+(i+1)+" won the battle!";
					engines[engines[i].currentCell.enemy-1].finished=true;
					engines[engines[i].currentCell.enemy-1].map.notifyPosition(engines[engines[i].currentCell.enemy-1].currentCell, null);
				}
				
				frame.addMoveDescription(desc);
			}
			//if an engined found both the goal and the exit, the simulation is over
			if(engines[i].isFinished())
			{
				debug("Engine "+i+" successfully found the goal and exit!");
				frame.repaintMap();
				return true;
			}
			//count the number of engines that finished their exploration (for example no more cells to explore)
			if(engines[i].finished)
				finishCount++;
		}
		frame.repaintMap();
		
		//if all the engines are finished
		if(finishCount==enginesCount)
		{
			debug("None of the engines found both the goal and exit, but exploration is finished.");
			return true;
		}
		else
			return false;
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
		//engine.updateCurrent(path.pop());
		frame.addMoveDescription("Moved back");
		frame.repaintMap();
	}
}
