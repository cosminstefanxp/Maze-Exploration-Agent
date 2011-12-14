/*
 * Robot Explorer
 * 
 * Stefan-Dobrin Cosmin
 * 342C4
 */
package explorer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

import explorer.Cell.Visibility;


/**
 * The Class ExplorationEngine.
 */
public class ExplorationEngine {
	
	/** The Constant AGING_RATING. */
	public static final int AGING_RATING=-1;

	/** The map. */
	Map map;
	
	/** The visible cell. */
	HashSet<Cell> visibleCells;
	
	/** The known cells are the cells which I have either explored either observed. */
	HashSet<Cell> knownCells;
	
	/** The unvisited but known cells. */
	HashSet<Cell> unvisitedCells;
	
	/** The current position. */
	Cell currentCell;
	
	/**
	 * Instantiates a new exploration engine.
	 *
	 * @param map the map
	 */
	public ExplorationEngine(Map map) {
		super();
		
		//Prepare the fields
		this.map = map;
		this.currentCell=map.getCell(map.startX, map.startY);
		this.visibleCells=map.getVisibleCells(currentCell.x,currentCell.y);
		this.knownCells=new HashSet<Cell>(visibleCells);
		this.knownCells.add(currentCell);
		this.unvisitedCells=new HashSet<Cell>(visibleCells);
		this.unvisitedCells.remove(currentCell);
		
		//Mark the cells accordingly
		for(Cell cell:visibleCells)
			cell.visible=Visibility.Visible;
		currentCell.visible=Visibility.Robot;
	}
	
	/**
	 * Performs the next step.
	 *
	 * @return the text description of the action performed.
	 */
	public String nextStep()
	{
		MainLauncher.debug("Calculating next step from "+currentCell);
		//Get updated ratings
		updateCosts();
		MainLauncher.debug("Cells after ratings calculation "+knownCells);
		
		ArrayList<Cell> neigh=map.getNeighbours(currentCell.x, currentCell.y);
		this.updateCurrent(neigh.get(0));
		this.updateVisible();
		MainLauncher.debug("Moving to "+currentCell);
		return "Move to ["+currentCell.x+","+currentCell.y+"]"; 
	}
	
	/**
	 * Update visible.
	 */
	private void updateVisible()
	{
		//Mark the old cells accordingly
		this.visibleCells.remove(currentCell);
		for(Cell cell:visibleCells)
			cell.visible=Visibility.Explored;
		
		//Mark the new cells accordingly
		this.visibleCells=map.getVisibleCells(currentCell.x, currentCell.y);
		this.visibleCells.remove(currentCell);
		for(Cell cell:visibleCells)
			cell.visible=Visibility.Visible;
		
		//Add the new cells to the known list
		this.knownCells.addAll(visibleCells);
	}
	
	/**
	 * Update the current cell.
	 *
	 * @param newCell the new cell
	 */
	private void updateCurrent(Cell newCell)
	{
		this.currentCell.visible=Visibility.Explored;
		this.currentCell=newCell;
		newCell.visible=Visibility.Robot;
	}
	
	/**
	 * Checks if is goal.
	 *
	 * @return true, if is goal
	 */
	public boolean isGoal()
	{
		return false;
	}
	
	/**
	 * Update costs of all the known cells.
	 */
	private void updateCosts()
	{
		// initialize graph
		for (Cell cell : knownCells) {
			cell.cost = Integer.MAX_VALUE;
			cell.predecessor = null;
		}
		currentCell.cost = 0;

		// All nodes in the graph are unoptimized - thus are in Q
		PriorityQueue<Cell> Q = new PriorityQueue<Cell>(knownCells);
		while (!Q.isEmpty()) 
		{
			Cell current = Q.remove();
			if (current.cost == Integer.MAX_VALUE)
				break; // all remaining vertices are inaccessible from source

			// For every neighbour
			ArrayList<Cell> neigh = map.getNeighbours(current.x, current.y);
			for (Cell cell : neigh)
				// if it hasn't been eliminated from the queue
				if (Q.contains(cell)) {
					// Try to relax
					int dist = current.cost + cell.getDefaultRating();
					if (dist < cell.cost) {
						cell.cost = dist;
						cell.predecessor = current;
						// Reorder cell in the queue
						Q.remove(cell);
						Q.add(cell);
					}
				}
		}
	}
}
